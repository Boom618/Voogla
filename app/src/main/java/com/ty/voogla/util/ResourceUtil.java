package com.ty.voogla.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.TypedValue;
import com.ty.voogla.base.MainApp;

import java.io.File;


/**
 * @author TY
 */
public class ResourceUtil {

    /**
     * 获取Application Context
     *
     * @return
     */
    public static Context getContext() {
        return MainApp.getContext();
    }

    /**
     * 获取 Resources
     *
     * @return
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics()));

    }

    /**
     * 获取String
     *
     * @param strId
     * @return
     */
    public static String getString(int strId) {
        return getResources().getString(strId);
    }

    /**
     * 获取Color
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }


    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.KITKAT) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }


    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            // MediaProvider
            if (isMediaDocument(uri)) {
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
                // DownloadsProvider
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**-----------------------------------------------------------------------------*/

    /**
     * Uri to file.
     *
     * @param uri        The uri.
     * @param columnName The name of the target column.
     *                   <p>e.g. {@link MediaStore.Images.Media#DATA}</p>
     * @return file
     */
    public static File uri2File(@NonNull final Uri uri, final String columnName) {
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        }
        CursorLoader cl = new CursorLoader(ResourceUtil.getContext());
        cl.setUri(uri);
        cl.setProjection(new String[]{columnName});
        Cursor cursor = null;
        try {
            cursor = cl.loadInBackground();
            int columnIndex = cursor.getColumnIndexOrThrow(columnName);
            cursor.moveToFirst();
            return new File(cursor.getString(columnIndex));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

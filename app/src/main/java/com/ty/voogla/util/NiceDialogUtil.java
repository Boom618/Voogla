//package com.ty.voogla.util;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.support.v4.app.FragmentManager;
//import android.widget.TextView;
//import com.shehuan.nicedialog.BaseNiceDialog;
//import com.shehuan.nicedialog.NiceDialog;
//import com.shehuan.nicedialog.ViewConvertListener;
//import com.shehuan.nicedialog.ViewHolder;
//import com.ty.voogla.R;
//import com.ty.voogla.widght.NormalSelectionDialog;
//
//import java.util.List;
//
///**
// * @author TY on 2018/12/21.
// * <p>
// * dialog 基于 DialogFragment
// */
//public class NiceDialogUtil {
//
//    public static void dialog(FragmentManager manager) {
//        NiceDialog.init()
//                .setLayoutId(R.layout.item_box_link_batch)
//                .setConvertListener(new ViewConvertListener() {
//                    @Override
//                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
//                        dialog.dismiss();
//
//                    }
//                })
//                .setShowBottom(true)
//                .setHeight(310)
//                .show(manager);
//    }
//
//    public static void selectBatch(Context context, final List<String> data, final TextView textView) {
//        NormalSelectionDialog.Builder builder = new NormalSelectionDialog.Builder(context);
//
//        builder.setlTitleVisible(true)
//                .setTitleText("选择批次号")
//                .setOnItemListener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        textView.setText(data.get(which));
//                        dialog.dismiss();
//                    }
//                })
//                .build()
//                .setDatas(data)
//                .show();
//    }
//}

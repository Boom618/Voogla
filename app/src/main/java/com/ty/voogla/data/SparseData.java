package com.ty.voogla.data;

import android.util.SparseArray;
import com.ty.voogla.bean.sendout.QrCodeListData;

/**
 * @author TY on 2019/1/27.
 */
public class SparseData {

    private static SparseArray<SparseArray<QrCodeListData>> codeList = new SparseArray<>(10);
    private static SparseArray<QrCodeListData> code = new SparseArray<>(20);


    public static void setCodeList(int position, SparseArray<QrCodeListData> code){
        codeList.put(position,code);
    }


    public static SparseArray<QrCodeListData> getCodeList(){
        if (code == null) {
            code = new SparseArray<>();
            code.put(0,new QrCodeListData());
        }
        return code;
    }


}

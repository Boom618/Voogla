package com.ty.voogla.bean

import android.util.SparseArray
import com.ty.voogla.bean.sendout.QrCodeListData
import java.io.Serializable

/**
 * @author  TY
 * @Date:   2019/5/9 10:29
 * @Description: 出库暂存数据
 */
class SendStorageData : Serializable {

    //
    var boxSizeList = SparseArray<Int>()
    var proSizeList = SparseArray<Int>()
    // 是否暂存标记
    var isStorage = false

    //    var listDetail: MutableList<QrCodeListData> = mutableListOf()
    var hashMap: HashMap<Int, ArrayList<QrCodeListData>> = hashMapOf()
}
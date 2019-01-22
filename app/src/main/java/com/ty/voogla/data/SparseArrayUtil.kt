package com.ty.voogla.data

import android.content.Context
import android.util.SparseArray
import com.ty.voogla.bean.sendout.QrCodeListData
import java.io.*


/**
 * @author TY on 2019/1/22.
 */
object SparseArrayUtil {

    private val qrCodeList = SparseArray<MutableList<QrCodeListData>>()

    /**
     * 存 已出库/生产入库 查看详情的二维码
     * @param position
     * @param dataMutableList
     */
//    @JvmStatic
//    fun putQrCodeListData(position: Int, dataMutableList: MutableList<QrCodeListData>) {
//        qrCodeList.put(position, dataMutableList)
//    }
//
//    @JvmStatic
//    fun getQrCodeListData(position: Int): MutableList<QrCodeListData> {
//        return qrCodeList.get(position)
//    }

    /**
     * File 存 SparseArray<QrCodeListData>
     */
    @JvmStatic
    fun putQrCodeList(context:Context,sparseArray: List<QrCodeListData>) {

        var outputStream: ObjectOutputStream? = null
        try {
            val file = File(context.getDir("data", Context.MODE_PRIVATE), "sparse")
            outputStream = ObjectOutputStream(FileOutputStream(file))
//            outputStream = ObjectOutputStream(FileOutputStream("code.txt"))
            outputStream.writeObject(sparseArray)
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 获取 SparseArray<QrCodeListData> 数据
     */
    @JvmStatic
    fun getQrCodeList(context:Context): List<QrCodeListData> {

        var inputStreamReader: ObjectInputStream? = null
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sparse")
        inputStreamReader = ObjectInputStream(FileInputStream(file))
//        inputStreamReader = ObjectInputStream(FileInputStream("code.txt"))
        val readObject = inputStreamReader.readObject() as List<QrCodeListData>
        inputStreamReader.close()
        return readObject

    }
}

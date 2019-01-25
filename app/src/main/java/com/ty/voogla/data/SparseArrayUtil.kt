package com.ty.voogla.data

import android.content.Context
import android.util.SparseArray
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean
import com.ty.voogla.bean.sendout.QrCodeListData
import java.io.*
import java.util.ArrayList


/**
 * @author TY on 2019/1/22.
 */
object SparseArrayUtil {


    /**
     * File 存 List<QrCodeListData>
     *     MutableList<InBoxCodeDetailInfosBean>
     */
    @JvmStatic
    fun putQrCodeList(context: Context, sparseArray: MutableList<InBoxCodeDetailInfosBean>) {

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
     * 获取 List<QrCodeListData> 数据
     */
    @JvmStatic
    fun getQrCodeList(context: Context): MutableList<InBoxCodeDetailInfosBean> {

        var inputStreamReader: ObjectInputStream? = null
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sparse")
        inputStreamReader = ObjectInputStream(FileInputStream(file))
//        inputStreamReader = ObjectInputStream(FileInputStream("code.txt"))
        val readObject = inputStreamReader.readObject() as MutableList<InBoxCodeDetailInfosBean>
        inputStreamReader.close()
        return readObject

    }

    //    -----------------------------------------------------------


    /**
     * 接收 QrCodeListData （出库查看）
     */
    @JvmStatic
    fun putQrCodeListLook(context:Context,boxInfos: MutableList<QrCodeListData>) {

        var outputStream: ObjectOutputStream? = null
        try {
            val file = File(context.getDir("data", Context.MODE_PRIVATE), "sendLook")
            outputStream = ObjectOutputStream(FileOutputStream(file))
//            outputStream = ObjectOutputStream(FileOutputStream("code.txt"))
            outputStream.writeObject(boxInfos)
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

    @JvmStatic
    fun getQrCodeListLook(context:Context): MutableList<QrCodeListData>{
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sendLook")
        val inputStream = ObjectInputStream(FileInputStream(file))
        val list = inputStream.readObject() as MutableList<QrCodeListData>
        inputStream.close()
        return list
    }
}

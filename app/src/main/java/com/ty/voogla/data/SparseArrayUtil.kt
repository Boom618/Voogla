package com.ty.voogla.data

import android.content.Context
import android.support.v4.util.ArrayMap
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
        var readObject = mutableListOf<InBoxCodeDetailInfosBean>()
        try {
            val file = File(context.getDir("data", Context.MODE_PRIVATE), "sparse")
            inputStreamReader = ObjectInputStream(FileInputStream(file))
//        inputStreamReader = ObjectInputStream(FileInputStream("code.txt"))
            readObject = inputStreamReader.readObject() as MutableList<InBoxCodeDetailInfosBean>
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStreamReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return readObject

    }

    @JvmStatic
    fun clearInBoxCode(context: Context) {
        val temp: MutableList<InBoxCodeDetailInfosBean> = mutableListOf()
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sparse")
        val outputStream = ObjectOutputStream(FileOutputStream(file))
//            outputStream = ObjectOutputStream(FileOutputStream("code.txt"))
        outputStream.writeObject(temp)
        outputStream.flush()
    }

    //    -----------------------------------------------------------


    /**
     * 接收 QrCodeListData （出库查看）
     */
    @JvmStatic
    fun putQrCodeListLook(context: Context, boxInfos: MutableList<QrCodeListData>) {

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
    fun getQrCodeListLook(context: Context): MutableList<QrCodeListData> {
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sendLook")
        val inputStream = ObjectInputStream(FileInputStream(file))
        val list = inputStream.readObject() as MutableList<QrCodeListData>
        inputStream.close()
        return list
    }

    /**  ----------------------------------------------------------- 发货出库明细 */


    @JvmStatic
    fun putQrCodeSend(context: Context, boxInfos: HashMap<Int, ArrayList<QrCodeListData>>) {

        var outputStream: ObjectOutputStream? = null
        try {
            val file = File(context.getDir("data", Context.MODE_PRIVATE), "sendDetail")
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
    fun getQrCodeSend(context: Context): HashMap<Int, ArrayList<QrCodeListData>> {
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sendDetail")
        val inputStream = ObjectInputStream(FileInputStream(file))
        val list = inputStream.readObject() as HashMap<Int, ArrayList<QrCodeListData>>
        inputStream.close()
        return list
    }

    @JvmStatic
    fun clearCode(context: Context) {
        val hashMap = HashMap<Int, ArrayList<QrCodeListData>>()
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "sendDetail")
        val outputStream = ObjectOutputStream(FileOutputStream(file))
        outputStream.writeObject(hashMap)
        outputStream.flush()
        outputStream.close()
    }

    /**  ----------------------------------------------------------- 出库 所有产品码 */

    @JvmStatic
    fun putOwnProCode(context: Context, ownPro: HashMap<String, String>) {

        var outputStream: ObjectOutputStream? = null
        try {
            val file = File(context.getDir("data", Context.MODE_PRIVATE), "ownProCode")
            outputStream = ObjectOutputStream(FileOutputStream(file))
            outputStream.writeObject(ownPro)
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
    fun getOwnProCode(context: Context): HashMap<String, String> {
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "ownProCode")
        val inputStream = ObjectInputStream(FileInputStream(file))
        val list = inputStream.readObject() as HashMap<String, String>
        inputStream.close()
        return list
    }

    @JvmStatic
    fun clearOwnProCode(context: Context) {
        val hashMap = HashMap<String, String>()
        val file = File(context.getDir("data", Context.MODE_PRIVATE), "ownProCode")
        val outputStream = ObjectOutputStream(FileOutputStream(file))
        outputStream.writeObject(hashMap)
        outputStream.flush()
        outputStream.close()
    }

}

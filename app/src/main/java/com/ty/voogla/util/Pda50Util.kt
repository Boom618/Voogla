package com.ty.voogla.util

import android.content.Context
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeReader

/**
 * @author TY on 2019/1/11.
 */
object Pda50Util {

    private var manager: AidcManager? = null
    private var barcodeReader: BarcodeReader? = null

    fun getBarcodeReader(context: Context): BarcodeReader? {
        val temp = ResourceUtil.getContext()
        ToastUtil.showToast("tempContext = $temp")

        AidcManager.create(context) { aidcManager ->
            manager = aidcManager

            barcodeReader = manager!!.createBarcodeReader()
        }

        return barcodeReader
    }
}

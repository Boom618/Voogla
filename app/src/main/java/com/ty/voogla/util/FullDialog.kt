package com.ty.voogla.util

import android.content.Context
import com.ty.voogla.widght.LoadingDialog

/**
 * @author TY on 2019/4/23.
 * 加载 Dialog
 */
object FullDialog {


    @JvmStatic
    fun showLoading(context: Context, loadingMsg: String): LoadingDialog {
        val dialog = LoadingDialog.Builder(context)
            .setMessage(loadingMsg)
            .setCancelable(false)
            .create()
        dialog.show()
        return dialog
    }


}
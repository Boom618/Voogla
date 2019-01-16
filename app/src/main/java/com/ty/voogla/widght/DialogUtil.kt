package com.ty.voogla.widght

import android.content.Context
import android.widget.TextView
import com.ty.voogla.util.ToastUtil

/**
 * @author TY on 2019/1/8.
 */
object DialogUtil {

    /**
     * 发货出库
     * 删除该 item
     *
     * @param context
     */
    fun deleteItemDialog(context: Context, listener: NormalAlertDialog.onNormalOnclickListener) {
        val dialog = NormalAlertDialog.Builder(context)
            .setTitleVisible(true)
            .setTitleText("温馨提示？")
            .setRightButtonText("确认")
            .setLeftButtonText("取消")
            .setContentText("确认窜货")
            .setRightListener(listener)
            .setLeftListener { dialog -> dialog.dismiss() }
            .build()

        dialog.show()
    }

    /**
     * 选择产品名称(生产入库)
     */
    fun selectProName(context: Context, goodData: MutableList<String>, specData: MutableList<String>,goodView: TextView,specView: TextView) {
        val selectDialog = NormalSelectionDialog.Builder(context)
//            .setlTitleVisible(true)
//            .setTitleText("产品名称")
            .setOnItemListener { dialog, which ->
                dialog.dismiss()
                goodView.text = goodData[which]
                specView.text = specData[which]
                ToastUtil.showToast(goodData[which])

            }
            .build()
            .setDatas(goodData)
            .show()
    }

    /**
     * 选择产品名称(发货出库)
     */
    fun selectSendName(context: Context, data: MutableList<String>) {
        val selectDialog = NormalSelectionDialog.Builder(context)
//            .setlTitleVisible(true)
//            .setTitleText("产品名称")
            .setOnItemListener { dialog, which ->
                dialog.dismiss()
                ToastUtil.showToast(data[which])

            }
            .build()
            .setDatas(data)
            .show()
    }
}
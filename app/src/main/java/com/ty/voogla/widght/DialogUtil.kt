package com.ty.voogla.widght

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.ty.voogla.connector.SelectGoods
import com.ty.voogla.data.SharedP
import com.ty.voogla.util.ToastUtil

/**
 * @author TY on 2019/1/8.
 */
object DialogUtil {


    /**
     * 关闭软键盘
     *
     * @param context
     * @param view
     */
    @JvmStatic
    fun hideInputWindow(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 发货出库
     * 删除该 item
     *
     * @param titleText  标题 "温馨提示"
     * @param pointContent 内容
     */
    @JvmStatic
    fun leftRightDialog(
        context: Context,
        titleText: String,
        pointContent: String,
        listener: NormalAlertDialog.onNormalOnclickListener
    ) {
        val dialog = NormalAlertDialog.Builder(context)
            .setTitleVisible(true)
            .setTitleText(titleText)
            .setRightButtonText("确认")
            .setLeftButtonText("取消")
            .setContentText(pointContent)
            .setRightListener(listener)
            .setLeftListener { dialog -> dialog.dismiss() }
            .build()

        dialog.show()
    }

    /**
     * 选择产品名称(生产入库)
     */
    fun selectProName(
        context: Context,
        goodData: MutableList<String>,
        specData: MutableList<String>,
        goodView: TextView,
        specView: TextView,
        select: SelectGoods
    ) {
        val selectDialog = NormalSelectionDialog.Builder(context)
//            .setlTitleVisible(true)
//            .setTitleText("产品名称")
            .setOnItemListener { dialog, which ->

                val temp = SharedP.getGoodNo(context)
                if (temp != -1 && temp != which) {

                    // 选择不同商品
                    leftRightDialog(context, "温馨提示","重置数据？", NormalAlertDialog.onNormalOnclickListener {
                        select.removeGoods()
                        it.dismiss()
                        dialog.dismiss()
                        goodView.text = goodData[which]
                        specView.text = specData[which]
                        SharedP.putGoodNo(context, which)
                        ToastUtil.showSuccess(goodData[which])
                    })
                } else {
                    // 首次选择 or 选择的是同一个商品
                    dialog.dismiss()
                    goodView.text = goodData[which]
                    specView.text = specData[which]
                    SharedP.putGoodNo(context, which)
                    ToastUtil.showSuccess(goodData[which])
                }

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
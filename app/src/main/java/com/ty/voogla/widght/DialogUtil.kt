package com.ty.voogla.widght

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.ty.voogla.connector.SelectGoods
import com.ty.voogla.data.SharedP
import com.ty.voogla.data.SimpleCache
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
     * @param activity  Ac 关闭页面
     * @param titleText  标题 "温馨提示"
     * @param pointContent 内容
     * @param listener 监听
     * @param finishBack 是否关闭当前 Ac
     */
    @JvmStatic
    fun leftRightDialog(
        activity: AppCompatActivity,
        titleText: String,
        pointContent: String,
        listener: NormalAlertDialog.onNormalOnclickListener,
        finishBack: Boolean = false
    ) {
        val dialog = NormalAlertDialog.Builder(activity)
            .setTitleVisible(true)
            .setTitleText(titleText)
            .setRightButtonText("确认")
            .setLeftButtonText("取消")
            .setContentText(pointContent)
            .setRightListener(listener)
            .setLeftListener { dialog ->
                dialog.dismiss()
                if (finishBack) {
                    activity.finish()
                    SimpleCache.clearKey("storage")
                }
            }
            .build()

        dialog.show()
    }

    /**
     * 选择产品名称(生产入库)
     */
    fun selectProName(
        activity: AppCompatActivity,
        goodData: MutableList<String>,
        specData: MutableList<String>,
        goodView: TextView,
        specView: TextView,
        select: SelectGoods
    ) {
        NormalSelectionDialog.Builder(activity)
//            .setlTitleVisible(true)
//            .setTitleText("产品名称")
            .setOnItemListener { dialog, which ->

                val temp = SharedP.getGoodNo(activity)
                if (temp != -1 && temp != which) {

                    // 选择不同商品
                    leftRightDialog(activity, "温馨提示", "重置数据？", NormalAlertDialog.onNormalOnclickListener {
                        select.removeGoods()
                        it.dismiss()
                        dialog.dismiss()
                        goodView.text = goodData[which]
                        specView.text = specData[which]
                        SharedP.putGoodNo(activity, which)
                        ToastUtil.showSuccess(goodData[which])
                    })
                } else {
                    // 首次选择 or 选择的是同一个商品
                    dialog.dismiss()
                    goodView.text = goodData[which]
                    specView.text = specData[which]
                    SharedP.putGoodNo(activity, which)
                    ToastUtil.showSuccess(goodData[which])
                }

            }
            .build()
            .setDatas(goodData)
            .show()
    }
}
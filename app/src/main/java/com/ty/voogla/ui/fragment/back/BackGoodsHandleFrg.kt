package com.ty.voogla.ui.fragment.back

import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.base.BaseSupFragment
import com.ty.voogla.util.ToastUtil

/**
 * @author TY on 2019/4/18.
 * 退货操作处理
 */
class BackGoodsHandleFrg : BaseSupFragment() {
    override val fragmentLayout: Int
        get() = R.layout.fragment_back_goods_handle

    override fun onBaseCreate(view: View): View {
        return view
    }


    override fun onStart() {
        super.onStart()
        initToolBar(R.string.back_goods, "保存", View.OnClickListener {
            //            start()
            ToastUtil.showSuccess("保存")
        })
    }


    companion object {
        fun newInstance(): BackGoodsHandleFrg {
            val fragment = BackGoodsHandleFrg()

            val bundle = Bundle()
            bundle.putString("type", "type")
            fragment.arguments = bundle

            return fragment
        }
    }
}
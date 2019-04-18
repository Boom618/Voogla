package com.ty.voogla.ui.fragment.back

import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.base.BaseSupFragment

/**
 * @author TY on 2019/4/18.
 * 查看退货明细页
 */
class BackGoodsLookFrg : BaseSupFragment() {
    override val fragmentLayout: Int
        get() = 0

    override fun onBaseCreate(view: View): View {
        return view
    }


    override fun onStart() {
        super.onStart()
        initToolBar(R.string.back_goods_look)
    }


    companion object {
        fun newInstance(): BackGoodsLookFrg {
            val fragment = BackGoodsLookFrg()

            val bundle = Bundle()
            bundle.putString("type", "type")
            fragment.arguments = bundle

            return fragment
        }
    }
}
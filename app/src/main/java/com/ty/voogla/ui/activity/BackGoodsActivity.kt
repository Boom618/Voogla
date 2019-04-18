package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.BaseSupActivity
import com.ty.voogla.ui.fragment.back.BackGoodsFragment
import com.ty.voogla.util.ToastUtil

/**
 * @author TY on 2019/4/18.
 * 退货模块
 * 继承 yo key word  SupportActivity
 */
class BackGoodsActivity : BaseSupActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_back_goods

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        val fragment = findFragment(BackGoodsFragment::class.java)
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, BackGoodsFragment.newInstance("type"))
        }
    }

    override fun initTwoView() {
    }

    /**
     * 监听 back 键
     */
    override fun onBackPressedSupport() {

        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            finish()
        }
    }
}
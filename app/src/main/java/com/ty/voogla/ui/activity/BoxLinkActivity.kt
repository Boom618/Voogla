package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.NiceDialogUtil
import kotlinx.android.synthetic.main.activity_box_link.*

import java.util.ArrayList

/**
 * @author TY on 2018/12/20.
 *
 *
 * 箱码绑定
 */
class BoxLinkActivity : BaseActivity(), VooglaContract.View {

    private var batchString: MutableList<String>? = null


    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_box_link


    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {

        batchString = ArrayList()
        batchString!!.add("批次号一")
        batchString!!.add("批次号二")

    }

    override fun initTwoView() {


        tv_select_batch!!.setOnClickListener { v ->
            //NiceDialogUtil.dialog(getSupportFragmentManager());
            NiceDialogUtil.selectBatch(v.context, batchString, tv_select_batch)
        }


    }

    override fun showSuccess() {

    }

    override fun showError(msg: String) {

    }
}

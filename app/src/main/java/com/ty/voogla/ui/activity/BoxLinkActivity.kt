package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_box_link.*

/**
 * @author TY on 2018/12/20.
 *
 *
 * 箱码绑定
 */
class BoxLinkActivity : BaseActivity(), VooglaContract.View {

//    lateinit var batchString: MutableList<String>


    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_box_link


    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {


    }

    override fun initTwoView() {

        initToolBar(R.string.box_link)


        search_view.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val tempString = v.text.toString().trim { it <= ' ' }
                ToastUtil.showToast(tempString)
            }
            true
        }


        tv_select_batch!!.setOnClickListener { v ->
            //NiceDialogUtil.dialog(getSupportFragmentManager());
//            NiceDialogUtil.selectBatch(v.context, batchString, tv_select_batch)
        }

        bt_start.setOnClickListener { gotoActivity(BoxLinkDetailActivity::class.java) }

    }

    override fun showSuccess() {

    }

    override fun showError(msg: String) {

    }
}

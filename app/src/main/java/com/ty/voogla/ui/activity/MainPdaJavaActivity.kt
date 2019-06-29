package com.ty.voogla.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.produce.ProductListInfoData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter

/**
 * @author TY on 2019/1/12.
 */
class MainPdaJavaActivity : BaseActivity(), View.OnClickListener, VooglaContract.View<ProductListInfoData> {

    private val userInfo = SimpleCache.userInfo

    private val presenter = VooglaPresenter(this)
    override val activityLayout: Int
        get() = R.layout.activity_main_pda

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        // 获取产品列表
        presenter.getProductListInfo(SimpleCache.userInfo.companyNo,"update")
    }

    override fun initOneData() {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    override fun initTwoView() {

        val product = findViewById<ImageView>(R.id.image_product)
        val send = findViewById<ImageView>(R.id.image_send)
        val user = findViewById<ImageView>(R.id.image_user)
        val back = findViewById<Button>(R.id.back_goods)
        val IM = userInfo.roleNo.contains(CodeConstant.USER_PDA_IM)
        val OM = userInfo.roleNo.contains(CodeConstant.USER_PDA_OM)
        // 权限管理  IM && OM
        if (IM && OM) {
            product.visibility = View.VISIBLE
            send.visibility = View.VISIBLE
        } else {
            if (IM) {
                product.visibility = View.VISIBLE
                send.visibility = View.GONE
            } else if (OM) {
                product.visibility = View.GONE
                send.visibility = View.VISIBLE
            }
        }
        setViewOnClickListener(this, product, send, user, back)


    }

    // 产品列表数据
    override fun showSuccess(data: ProductListInfoData) {
        // P  层 保存 SimpleCache.putProductList(data)
    }

    override fun showError(msg: String?) {

    }

    override fun showResponse(response: ResponseInfo?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.image_product -> gotoActivity(ProduceIntoActivity::class.java)
            R.id.image_send -> gotoActivity(SendOutActivity::class.java)
            R.id.image_user -> gotoActivity(UserContentActivity::class.java)
            R.id.back_goods -> gotoActivity(BackGoodsActivity::class.java)
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}

package com.ty.voogla.ui.fragment.into

import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.base.BaseSupFragment
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.produce.ProductListInfoData
import com.ty.voogla.connector.SelectGoods
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.util.ToastUtil

/**
 * @author TY on 2019/4/23.
 * 生产入库 Frg 替换 Ac( ProduceIntoDetailActivity )
 */
class ProduceIntoDetailFrg : BaseSupFragment() , VooglaContract.View<ProductListInfoData>, SelectGoods {



    override val fragmentLayout: Int
        get() = R.layout.activity_product_into_detail

    override fun onBaseCreate(view: View): View {
        return view
    }

    override fun showSuccess(data: ProductListInfoData?) {

    }

    override fun showError(msg: String?) {
        ToastUtil.showError(msg)
    }

    override fun showResponse(response: ResponseInfo?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun removeGoods() {
    }


    companion object {
        fun newInstance(type: String): ProduceIntoDetailFrg {
            val fragment = ProduceIntoDetailFrg()

            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle

            return fragment
        }
    }
}
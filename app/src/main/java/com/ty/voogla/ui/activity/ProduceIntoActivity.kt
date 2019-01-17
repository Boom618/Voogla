package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProductIntoAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.produce.ProductIntoData
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.SimpleCache
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.NormalAlertDialog
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_product_into.*

/**
 * @author TY on 2018/12/20.
 *
 *
 * 生产入库（PDA 端）
 */
class ProduceIntoActivity : BaseActivity(), VooglaContract.ListView<ProductIntoData.ListBean> {

    lateinit var adapter: ProductIntoAdapter

    // 企业编号  归属单位
    private val companyNo = SimpleCache.getUserInfo().companyNo
    private val companyAttr = SimpleCache.getUserInfo().companyAttr

    // 回调成功标志
    private var isDelete = false
    private var itemPosition = 0

    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_product_into


    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {
        presenter.getProduceList(companyNo)
        isDelete = false
    }

    override fun initTwoView() {

        initToolBar(R.string.produce_into)


        search_view.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val tempString = v.text.toString().trim { it <= ' ' }
                ToastUtil.showToast(tempString)
            }
            true
        }



        tv_product.setOnClickListener { gotoActivity(ProduceIntoDetailActivity::class.java) }

    }

    override fun showSuccess(data: MutableList<ProductIntoData.ListBean>) {

        if (isDelete) {
            data.removeAt(itemPosition)
            adapter.notifyItemRemoved(itemPosition)
            adapter.notifyItemRangeChanged(itemPosition, data.size - itemPosition)
        } else {
            LayoutInit.initLayoutManager(this, recycler_view_pro)

            adapter = ProductIntoAdapter(this, R.layout.item_produce_into, data)
            recycler_view_pro.adapter = adapter

            adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
                override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {

                    DialogUtil.deleteItemDialog(view.context, "确认删除", NormalAlertDialog.onNormalOnclickListener {

                        presenter.deleteProduct(companyNo, data[position].inBatchNo, companyAttr)
                        isDelete = true
                        itemPosition = position
                        it.dismiss()
                    })
                    return true
                }

                override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {

                }

            })
        }

    }

    override fun showError(msg: String) {

        ToastUtil.showToast(msg)

    }
}

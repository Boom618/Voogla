package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProductIntoAdapter
import com.ty.voogla.base.BaseSupActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.produce.ProductIntoData
import com.ty.voogla.constant.CodeConstant.*
import com.ty.voogla.constant.TipString
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ResourceUtil
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.LoadingDialog
import com.ty.voogla.widght.NormalAlertDialog
import com.ty.voogla.widght.SpaceItemDecoration
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_product_into.*

/**
 * @author TY on 2018/12/20.
 *
 *
 * 生产入库（PDA 端）
 */
class ProduceIntoActivity : BaseSupActivity(), VooglaContract.ListView<ProductIntoData.ListBean> {

    private lateinit var adapter: ProductIntoAdapter

    // 企业编号  归属单位
    private val companyNo = SimpleCache.userInfo.companyNo

    // 回调成功标志
    private var isDelete = false
    private var itemPosition = 0

    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_product_into


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        LayoutInit.initLayoutManager(this, recycler_view_pro)
        recycler_view_pro.addItemDecoration(SpaceItemDecoration(ResourceUtil.dip2px(ITEM_DECORATION), false))
    }

    override fun initOneData() {
    }

    override fun initTwoView() {

        initToolBar(R.string.produce_into)
        // 列表 batchNo 传空
        presenter.getProduceList(companyNo, "")
        isDelete = false

        // 搜索
        search_view.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val batchNo = v.text.toString().trim { it <= ' ' }

                presenter.getProduceList(companyNo, batchNo)
                DialogUtil.hideInputWindow(v.context, v)
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
            adapter = ProductIntoAdapter(this, R.layout.item_produce_into, data)
            recycler_view_pro.adapter = adapter

            adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
                override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {

//                    DialogUtil.leftRightDialog(
//                        this@ProduceIntoActivity,
//                        TipString.tips,
//                        "确认删除",
//                        NormalAlertDialog.onNormalOnclickListener {
//
//                            presenter.deleteProduct(companyNo, data[position].inBatchNo, companyAttr)
//                            isDelete = true
//                            itemPosition = position
//                            it.dismiss()
//                        })
                    return true
                }

                override fun onItemClick(view: View, holder: RecyclerView.ViewHolder?, position: Int) {
                    val layout = view.findViewById<View>(R.id.layout)
                    when (layout.visibility) {
                        View.VISIBLE -> layout.visibility = View.GONE
                        View.GONE -> layout.visibility = View.VISIBLE
                    }
                }

            })
        }

    }

    override fun showResponse(response: ResponseInfo) {

    }

    override fun showError(msg: String) {

        ToastUtil.showError(msg)

    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }
}

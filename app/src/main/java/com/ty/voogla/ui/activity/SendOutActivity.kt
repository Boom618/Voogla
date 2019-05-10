package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ResourceUtil
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.LoadingDialog
import com.ty.voogla.widght.SpaceItemDecoration
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_send_out.*

/**
 * @author TY on 2019/1/14.
 * 发货出库
 */
class SendOutActivity : BaseActivity(), VooglaContract.ListView<SendOutListData.ListBean> {

    private val presenter = VooglaPresenter(this)
    private val listData: MutableList<SendOutListData.ListBean> = mutableListOf()

    private var companyNo: String? = null

    private var adapter: SendOutAdapter? = null

    override val activityLayout: Int
        get() = R.layout.activity_send_out

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        refreshLayout_out.setColorSchemeResources(
            android.R.color.holo_blue_light, android.R.color.holo_red_light,
            android.R.color.holo_orange_light, android.R.color.holo_green_light
        )
        refreshLayout_out.setOnRefreshListener {
            presenter.getSendOutList2(companyNo, "01", "")
        }
        LayoutInit.initLayoutManager(this, recyclerView_out)
        recyclerView_out.addItemDecoration(SpaceItemDecoration(ResourceUtil.dip2px(CodeConstant.ITEM_DECORATION)))

        adapter = SendOutAdapter(this, R.layout.item_send_out, listData)
        recyclerView_out.adapter = adapter

        adapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
                return true
            }

            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                val layout = view.findViewById<View>(R.id.layout)
                when (layout.visibility) {
                    View.VISIBLE -> layout.visibility = View.GONE
                    View.GONE -> layout.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)
        companyNo = SimpleCache.userInfo.companyNo
        // 全部 list
        presenter.getSendOutList2(companyNo, "01", "")

    }

    private val goodsNameList = mutableListOf<String>()
    private val goodsNoList = mutableListOf<String>()
    override fun initTwoView() {
        // 产品列表（登录已存）
        try {
            val productList = SimpleCache.productList
            val list = productList.list!!
            val size = list.size
            for (i in 0 until size) {
                goodsNameList.add(list[i].goodsName)
                goodsNoList.add(list[i].goodsNo)
            }
            spinner_view.attachDataSource(goodsNameList)
            spinner_view.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {

                    presenter.getSendOutList2(companyNo, "01", goodsNoList[position])
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun showSuccess(data: MutableList<SendOutListData.ListBean>) {
        refreshLayout_out.isRefreshing = false
        listData.clear()
        listData.addAll(data)
        adapter?.notifyDataSetChanged()
    }

    override fun showError(msg: String) {
        refreshLayout_out.isRefreshing = false
        ToastUtil.showError(msg)
    }

    override fun showResponse(response: ResponseInfo) {
        ToastUtil.showSuccess(response.msg)
    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

}

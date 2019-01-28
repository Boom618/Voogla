package com.ty.voogla.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutAdapter
import com.ty.voogla.base.BaseFragment
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.NormalAlertDialog
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.content_list_fragment.*
import kotlinx.android.synthetic.main.content_list_fragment.view.*

/**
 * @author TY on 2019/1/28.
 *
 * 待发货
 */
class WaitShippFragment : BaseFragment(), VooglaContract.ListView<SendOutListData.ListBean> {


    private val presenter = VooglaPresenter(this)
    private val listData: MutableList<SendOutListData.ListBean> = mutableListOf()

    private var companyNo: String? = null

    private var adapter: SendOutAdapter? = null

    private var deliveryState = "01"

    /**
     * 下拉刷新 flag
     */
    private var refresh = false

    override val fragmentLayout: Int
        get() = R.layout.content_list_fragment

    override fun onBaseCreate(view: View): View {

        // 设置 Header 样式
        view.refreshLayout!!.setRefreshHeader(MaterialHeader(context!!))
        // 设置 Footer 为 球脉冲 样式
        view.refreshLayout!!.setRefreshFooter(BallPulseFooter(context!!).setSpinnerStyle(SpinnerStyle.Scale))

        LayoutInit.initLayoutManager(context!!, view.recyclerView)
        adapter = SendOutAdapter(context!!, R.layout.item_send_out, listData)
        view.recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {

                val deliveryNo = listData[position].deliveryNo!!
                if (listData[position].deliveryState == "02") {
                    DialogUtil.deleteItemDialog(
                        view.context,
                        "温馨提示",
                        "确认删除",
                        NormalAlertDialog.onNormalOnclickListener {

                            presenter.deleteSendOut(companyNo, deliveryNo)

                            it.dismiss()
                        })
                }
                return true
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
            }

        })

        return view
    }

    // 可见时 开始加载数据
    override fun loadData() {

        companyNo = SimpleCache.getUserInfo().companyNo


        deliveryState = arguments!!.getString("type")!!
        //    发货状态(01待发货,02已发货,03已收货)
        presenter.getSendOutList2(companyNo, deliveryState)
    }

    override fun onResume() {
        super.onResume()

        refreshLayout!!.setOnRefreshListener { refreshLayout ->
            // 传入 false 表示刷新失败
            refreshLayout.finishRefresh(1000)
            // 刷新数据
            presenter.getSendOutList2(companyNo, deliveryState)
            refresh = true
        }
        refreshLayout!!.setOnLoadMoreListener { refreshLayout ->
            // 传入 false 表示刷新失败
            refreshLayout.finishLoadMore(1000)
            ToastUtil.showToast("没有更多数据了")
        }
    }

    override fun showSuccess(data: MutableList<SendOutListData.ListBean>) {

        listData.addAll(data)
        adapter!!.notifyDataSetChanged()
    }

    override fun showError(msg: String) {
    }

    override fun showResponse(response: ResponseInfo?) {
    }

    companion object {

        fun newInstance(tag: String): WaitShippFragment {
            val fragment = WaitShippFragment()
            val args = Bundle()
            args.putString("type", tag)
            fragment.arguments = args
            return fragment
        }
    }
}
package com.ty.voogla.ui.fragment.back

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.ty.voogla.R
import com.ty.voogla.base.BaseSupFragment
import com.ty.voogla.constant.TipString
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import kotlinx.android.synthetic.main.fragment_back_good.view.*

/**
 * @author TY on 2019/4/18.
 * 退货主页
 */
class BackGoodsFragment : BaseSupFragment() {
    override val fragmentLayout: Int
        get() = R.layout.fragment_back_good

    override fun onBaseCreate(view: View): View {

        setSearch(view.goods_search)
        return view
    }

    private fun setSearch(searchView:EditText) {
        searchView.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val batchNo = v.text.toString().trim { it <= ' ' }

                //presenter.getProduceList(companyNo, batchNo)
                DialogUtil.hideInputWindow(v.context, v)
            }
            true
        }
    }

//    ----------------------   回调 ----------------------

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        // 懒加载
        // 同级Fragment场景、ViewPager场景均适用
        ToastUtil.showSuccess("退货主页 onLazyInitView")
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        // 当对用户可见时 回调
        // 不管是 父Fragment还是子Fragment 都有效！
        initToolBar(R.string.back_goods_list, TipString.retrueGoods, View.OnClickListener {
            start(BackGoodsHandleFrg.newInstance())
        })
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        // 当对用户不可见时 回调
        // 不管是 父Fragment还是子Fragment 都有效！
        ToastUtil.showSuccess("退货主页 onSupportInvisible")
    }


    companion object {
        fun newInstance(type: String): BackGoodsFragment {
            val fragment = BackGoodsFragment()

            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle

            return fragment
        }
    }

}
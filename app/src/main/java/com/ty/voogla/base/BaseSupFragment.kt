package com.ty.voogla.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ty.voogla.R
import me.yokeyword.fragmentation.SupportFragment


/**
 * @author TY
 */
abstract class BaseSupFragment : SupportFragment() {

    /**
     * Fragment Layout
     *
     * @return
     */
    protected abstract val fragmentLayout: Int


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(fragmentLayout, container, false)

        // onBaseCreate(view);
        return onBaseCreate(view)
    }

    /**
     * 初始化 View
     * @param view
     * @return
     */
    protected abstract fun onBaseCreate(view: View): View

    @JvmOverloads
    protected fun initToolBar(midId: Int = 0, rightText: String? = null, listener: View.OnClickListener? = null) {

        // 左边返回
        _mActivity.findViewById<View>(R.id.iv_back).setOnClickListener {
            _mActivity.onBackPressed()
        }

        // 中间标题
        val midText = _mActivity.findViewById<TextView>(R.id.tv_title)
        if (midId == 0) midText.text = "" else midText.setText(midId)

        // 右边监听事件
        val right = _mActivity.findViewById<TextView>(R.id.tv_right)
        if (!rightText.isNullOrEmpty()) {
            right.text = rightText
        }

        if (null == listener) {
            right.visibility = View.GONE
        } else {
            right.setOnClickListener(listener)
        }
    }


}

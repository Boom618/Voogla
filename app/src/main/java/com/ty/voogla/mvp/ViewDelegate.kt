package com.ty.voogla.mvp

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author TY on 2019/1/4.
 * 视图层代理的基类
 */
abstract class ViewDelegate : BaseView {

    private val mView: SparseArray<View> = SparseArray(10)

    override lateinit var rootView: View

    abstract val rootLayoutId: Int


    override fun create(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle) {

        rootView = inflater.inflate(rootLayoutId, container, false)
    }

    override fun initWidget() {
    }

    fun <T : View> get(id: Int): T {
        return bindView(id)
    }

    private fun <T : View> bindView(id: Int): T {

        val view: T = rootView.findViewById(id)
        mView.put(id, view)
        return view
    }

    /**
     * vararg 可变长度
     */
    fun setViewOnClickListener(listener: View.OnClickListener, vararg ids: Int) {
        if (ids.isNotEmpty()) {
            for (id in ids) {
                get<View>(id).setOnClickListener(listener)
            }
        }
    }


}
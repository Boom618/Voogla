package com.ty.voogla.base


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.data.ACache


/**
 * @author TY
 */
abstract class BaseFragment : Fragment() {

    /**
     * 功能列表：
     * 1、ButterKnife 初始化、解绑
     * 2、Layout
     * 3、
     */
    protected var mCache: ACache? = null

    /**
     * Fragment Layout
     *
     * @return
     */
    protected abstract val fragmentLayout: Int

    //是否可见
    protected var isVisble = false
    // 标志位，标志Fragment已经初始化完成。
    var isPrepared = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(fragmentLayout, container, false)

        mCache = ACache.get(view.context)
        // onBaseCreate(view);
        return onBaseCreate(view)
    }

    /**
     * 初始化 View
     * @param view
     * @return
     */
    protected abstract fun onBaseCreate(view: View): View

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel

    }

    /**
     *  Fragment 懒加载
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (userVisibleHint) {
            isVisble = true
            onVisible()
        } else {
            isVisble = false
            onInVisible()
        }
    }

    /**
     * 可见时 加载数据
     */
    private fun onVisible() {
        loadData()
    }



    private fun onInVisible() {}

    protected abstract fun loadData()

    /**
     * 重置 ACache 中保存的的数据
     */
    private fun clearCache() {
    }

    override fun onDestroy() {
        super.onDestroy()

        clearCache()

    }
}

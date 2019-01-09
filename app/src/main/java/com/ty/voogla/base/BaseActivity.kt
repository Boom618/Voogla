package com.ty.voogla.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.util.ACache

/**
 * @author TY
 */
abstract class BaseActivity : AppCompatActivity() {

//    private var mUnbinder: Unbinder? = null
    private var mCache: ACache? = null

    /**
     * Activity Layout 布局
     *
     * @return
     */
    protected abstract val activityLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayout)

//        mUnbinder = ButterKnife.bind(this)
        mCache = ACache.get(application)

        initOneData()

        onBaseCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        initTwoView()
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * 初始化 View
     *
     * @param savedInstanceState
     */
    protected abstract fun onBaseCreate(savedInstanceState: Bundle?)

    /**
     * 初始化 Data
     */
    protected abstract fun initOneData()

    /**
     * 初始化 View
     */
    protected abstract fun initTwoView()


    /**
     * 打开一个Activity 默认 不关闭当前activity
     * @param clz                    跳转类
     * @param isCloseCurrentActivity 关闭当前页
     */
    @JvmOverloads
    fun gotoActivity(clz: Class<*>, isCloseCurrentActivity: Boolean = false, ex: Bundle? = null) {
        val intent = Intent(this, clz)
        if (ex != null) {
            intent.putExtras(ex)
        }
        startActivity(intent)
        if (isCloseCurrentActivity) {
            finish()
        }
    }


    @JvmOverloads
    protected fun initToolBar(midId: Int = 0,  rightText: String? = null,listener: View.OnClickListener? = null) {

        // 左边返回
        findViewById<View>(R.id.iv_back).setOnClickListener {
            clearCache()
            finish()
        }

        // 中间标题
        val midText = findViewById<TextView>(R.id.tv_title)
        if (midId == 0) {
            midText.text = ""
        } else {
            midText.setText(midId)
        }

        // 右边监听事件
        val right = findViewById<TextView>(R.id.tv_right)
        if (rightText.isNullOrEmpty()) {
            right.text = rightText
        }

        if (null == listener) {
            right.visibility = View.GONE
        } else {
            right.setOnClickListener(listener)
        }

    }


    /**
     * 多个 View 设置点击事件
     * vararg 可变长度
     */
    fun setViewOnClickListener(listener: View.OnClickListener, vararg views: View) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view.setOnClickListener(listener)
            }
        }
    }

    /**
     * 重置 ACache 中保存的的数据
     */
    private fun clearCache() {}


    override fun onDestroy() {
        super.onDestroy()
        clearCache()

//        mUnbinder!!.unbind()
    }
}

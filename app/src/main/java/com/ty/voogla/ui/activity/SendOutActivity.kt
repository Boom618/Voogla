package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.ui.fragment.WaitShippFragment
import kotlinx.android.synthetic.main.activity_send_out.*

/**
 * @author TY on 2019/1/14.
 * 发货出库
 */
class SendOutActivity : BaseActivity() {

    private val mFragment = mutableListOf<Fragment>()

    override val activityLayout: Int
        get() = R.layout.activity_send_out

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)

        mFragment.add(WaitShippFragment.newInstance("01"))
        mFragment.add(WaitShippFragment.newInstance("02"))
        mFragment.add(WaitShippFragment.newInstance("03"))

        val mAdapter = MyPagerAdapter(supportFragmentManager, mFragment)
        viewpager.adapter = mAdapter

        stl.setViewPager(viewpager)

    }

    override fun initTwoView() {}


    class MyPagerAdapter(fm: FragmentManager, private val mFragment: List<Fragment>) : FragmentPagerAdapter(fm) {

        //private val mTitles = arrayOf("未发货", "已发货", "已收货")
        override fun getCount(): Int {
            return mFragment.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return mFragment[position]
        }
    }
    companion object {
        val mTitles = arrayOf("未发货", "已发货", "已收货")
    }

}

package com.ty.voogla.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author TY on 2019/1/4.
 */
interface BaseView{

    fun create(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle)

    fun initWidget()

    val rootView: View
}
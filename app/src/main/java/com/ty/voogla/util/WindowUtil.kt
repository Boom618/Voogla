package com.ty.voogla.util

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author TY on 2019/1/7.
 * 系统屏幕属性
 */
object WindowUtil {

    /**
     * 获取屏幕宽度
     * 手持机尺寸    screenHeight = 800  screenWidth = 480   display = 1.5  dpi = 240
     * p20 手机尺寸  screenHeight = 2240 screenWidth = 1080  display = 3.0  dpi = 480
     * 低（120dpi）、中（160dpi）、高（240dpi）和超高（320dpi）
     */
    fun getScreenWidth(): Int {
        // 方式一： 利用 Context 取值
//        val wm = MainApp.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val point = Point()
//        wm.defaultDisplay.getRealSize(point)
        // 方式二： 利用 Resources 取值（Resources 不能获取资源信息）
        return Resources.getSystem().displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    /**
     * 获取屏幕密度
     */
    fun getScreenDensity(): Float {
        return Resources.getSystem().displayMetrics.density
    }

    /**
     * 获取屏幕 Dpi
     * 低（120dpi）、中（160dpi）、高（240dpi）和超高（320dpi）
     */
    fun getScreenDensityDpi(): Int {
        return Resources.getSystem().displayMetrics.densityDpi
    }

    /**
     * dp 转 px,不需要 Context
     */
    fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)
    }
}
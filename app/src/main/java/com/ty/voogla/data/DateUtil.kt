package com.ty.voogla.data

import com.ty.voogla.constant.CodeConstant

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author TY on 2019/1/22.
 */
object DateUtil {

    /**
     * 时间戳 转 String
     *
     * @param date
     * @return
     */
    @JvmStatic
    fun getTime(date: Date): String {//可根据需要自行截取数据显示
        val format = SimpleDateFormat(CodeConstant.DATE_SIMPLE_H_M_S, Locale.CHINA)
        return format.format(date)
    }
}

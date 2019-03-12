package com.ty.voogla.data;

import com.ty.voogla.constant.CodeConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author TY on 2019/1/22.
 */
public class DateUtil {

    /**
     * 时间戳 转 String
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(CodeConstant.DATE_SIMPLE_H_M_S, Locale.CHINA);
        return format.format(date);
    }
}

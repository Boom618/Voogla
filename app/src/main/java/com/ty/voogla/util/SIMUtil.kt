package com.ty.voogla.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi

/**
 * @author  TY
 * @Date:   2019/6/13 15:33
 * @Description: 判断是否有 SIM 卡
 */
object SIMUtil {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ServiceCast")
    fun hasSimCard(context: Context): Boolean {

        val telMgr = context.getSystemService(Context.TELECOM_SERVICE) as TelephonyManager
        val result = when (telMgr.simState) {
            TelephonyManager.SIM_STATE_ABSENT -> false
            TelephonyManager.SIM_STATE_UNKNOWN -> false
            else -> true
        }
        return result
    }
}
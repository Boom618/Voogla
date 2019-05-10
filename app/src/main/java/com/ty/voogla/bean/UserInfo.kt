package com.ty.voogla.bean

import java.io.Serializable

/**
 * @author TY on 2018/12/20.
 */
class UserInfo : Serializable{


    /**
     * companyNo : P000001
     * userNo : 000001
     * userName : 超级管理员
     * companyName : 新华网溯源中国平台
     * companyZip : null
     * companyAttr : P000001
     * roleNo : superadmin@dms;
     */

    var companyNo: String? = null
    var userNo: String? = null
    var userName: String? = null
    var companyName: String? = null
    var companyZip: String? = null
    var companyAttr: String? = null
    var roleNo = ""
    var sessionID: String? = null
}

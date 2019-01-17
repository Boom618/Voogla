package com.ty.voogla.bean.produce

/**
 * @author TY on 2019/1/16.
 *
 * 二维码解码
 */
class DecodeCode {
    var status = 500
    var msg = ""
    var result: ResultBean? = null

    class ResultBean {
        var code: String? = null
    }
}
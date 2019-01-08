package com.ty.voogla.net.gson

import com.google.gson.*

import java.lang.reflect.Type

/**
 * desc:long值的类型转换器
 * author: XingZheng
 * date:2016/12/1
 * @author TY
 */

class LongDefault0Adapter : JsonSerializer<Long>, JsonDeserializer<Long> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Long? {
        try {
            //定义为long类型,如果后台返回""或者null,则返回0
            if (" " == json.asString || json.asString == "" || json.asString == "null") {
                return 0L
            }
        } catch (ignore: Exception) {
        }

        try {
            return json.asLong
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }

    }

    override fun serialize(src: Long?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src!!)
    }
}
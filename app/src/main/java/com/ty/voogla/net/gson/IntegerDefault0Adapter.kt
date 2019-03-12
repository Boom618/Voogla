package com.ty.voogla.net.gson

import com.google.gson.*

import java.lang.reflect.Type

/**
 * desc:int值的类型转换器
 * author: XingZheng
 * date:2016/12/1
 * @author TY
 */

class IntegerDefault0Adapter : JsonSerializer<Int>, JsonDeserializer<Int> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int? {
        try {
            //定义为int类型,如果后台返回""或者null,则返回0
            if (json.asString == " " || json.asString == "" || json.asString == "null") {
                return 0
            }
        } catch (ignore: Exception) {
        }

        try {
            return json.asInt
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }

    }

    override fun serialize(src: Int?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src!!)
    }
}
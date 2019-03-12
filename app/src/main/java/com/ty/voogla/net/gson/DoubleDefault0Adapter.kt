package com.ty.voogla.net.gson

import com.google.gson.*

import java.lang.reflect.Type

/**
 * desc:double值的类型转换器
 * author: XingZheng
 * date:2016/12/1
 * @author TY
 */

class DoubleDefault0Adapter : JsonSerializer<Double>, JsonDeserializer<Double> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Double? {
        try {
            //定义为double类型,如果后台返回""或者null,则返回0.00
            if (json.asString == " " || json.asString == "" || json.asString == "null") {
                return 0.00
            }
        } catch (ignore: Exception) {
        }

        try {
            return json.asDouble
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }

    }

    override fun serialize(src: Double?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src!!)
    }
}

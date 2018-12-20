package com.ty.voogla.net.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * desc:double值的类型转换器
 * author: XingZheng
 * date:2016/12/1
 * @author TY
 */

public class StringDefault0Adapter implements JsonSerializer<String>, JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            //定义为double类型,如果后台返回""或者null,则返回0.00
            if (json.getAsString().equals(" ") || json.getAsString().equals("") || json.getAsString().equals("null")) {
                return "";
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsString();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}

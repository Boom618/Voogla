package com.ty.voogla.net.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * desc:int值的类型转换器
 * author: XingZheng
 * date:2016/12/1
 * @author TY
 */

public class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            //定义为int类型,如果后台返回""或者null,则返回0
            if (json.getAsString().equals(" ") ||json.getAsString().equals("") || json.getAsString().equals("null")) {
                return 0;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
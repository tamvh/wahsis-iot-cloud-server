/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt;

import com.wahsis.iot.common.JsonParserUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author haint3
 */
public class MqttHelper {
    
    private static final Gson gson = new Gson();
    
    public static JsonObject serializeFromPayload(byte[] payload) {
        return JsonParserUtil.parseJsonObject(new String(payload));
    }
    
    public static <T extends Object> T serializeFromPayload(byte[] payload, Class<T> classOfT) {
        return gson.fromJson(new String(payload), classOfT);
    }    
}

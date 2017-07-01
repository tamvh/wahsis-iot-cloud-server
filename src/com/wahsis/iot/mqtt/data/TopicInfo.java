/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt.data;

import com.wahsis.iot.common.AppConst;

/**
 *
 * @author haint3
 */
public class TopicInfo {
    
    private String _gatewayId = "";
    private String _appKey = "";
    private String _apiName = "";
    private String _reqrespText = "";
    
    public TopicInfo(String appKey, String deviceId, String apiName, String reqespText) {
        _appKey = appKey;
        _gatewayId = deviceId;
        _apiName = apiName;
        _reqrespText = reqespText;
    }
    
    public TopicInfo(String topic) {
        String[] lists = topic.split("/");
        
        if (lists.length == 5) {
            _appKey = lists[0];
            _gatewayId = lists[2];
            _apiName = lists[3];
            _reqrespText = lists[4];
        }
    }
    
    public String getTopicString() {
        return String.format(AppConst.TOPIC_FORMAT, _appKey, _gatewayId, _apiName, _reqrespText);
    }

    
}

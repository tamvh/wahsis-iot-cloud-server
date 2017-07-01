/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.controller.mqtt;

import com.wahsis.iot.mqtt.MqttHelper;
import com.wahsis.iot.mqtt.data.IMqttMessage;
import com.wahsis.iot.mqtt.data.MsgUpdateState;
import com.wahsis.iot.mqtt.data.TopicInfo;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author haint3
 */
public class UpdateStateController implements IMqttMessageListener {

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        TopicInfo topicInfo = new TopicInfo(topic);
        IMqttMessage<MsgUpdateState> updateState = MqttHelper.serializeFromPayload(mm.getPayload(), new IMqttMessage<MsgUpdateState>().getClass());
        
    }
}

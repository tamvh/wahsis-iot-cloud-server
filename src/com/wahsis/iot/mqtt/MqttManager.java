/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt;

import com.wahsis.iot.mqtt.data.IoTDeviceInfo;
import com.wahsis.iot.common.AppConst;
import com.wahsis.iot.common.Config;
import com.wahsis.iot.common.DeviceType;
import com.wahsis.iot.controller.mqtt.UpdateStateController;
import com.wahsis.iot.mqtt.data.CardInfo;
import com.wahsis.iot.mqtt.data.IMqttMessage;
import com.wahsis.iot.mqtt.data.TopicInfo;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author haint3
 */
public class MqttManager {
    
    private static final Logger logger = Logger.getLogger(MqttManager.class);
    private static final Lock   _createLock = new ReentrantLock();
    private static MqttManager  _instance = null;
    private static Gson         _gson = new Gson();
    
    private GbcMqttSubscriber   _subscriber = null;
    private GbcMqttPublisher    _publisher = null;
    private Map<String, IoTDeviceInfo>  _iotDeviceMap = new HashMap<String, IoTDeviceInfo>();
    private Map<String, CardInfo>       _cardMap = new HashMap<String, CardInfo>();
    
    public static MqttManager getInstance() {
        if (_instance == null) {
            _createLock.lock();
            try {
                if (_instance == null) {
                    _instance = new MqttManager();
                }
            } finally {
                _createLock.unlock();
            }
        }
        return _instance;
    }
    
    public void start() {
        try {
            String uri = Config.getParam("mqtt", "uri");
            logger.info("mqtt broker = " + uri);
            
            _publisher = new GbcMqttPublisher();
            _subscriber = new GbcMqttSubscriber();
            
            _publisher.start(uri);
            _subscriber.start(uri);
            
            initialize();
            
        } catch (MqttException ex) {
            logger.error("MqttManager.start: " + ex.getMessage(), ex);
        }
    }
    
    public void stop() {
        if (_subscriber != null) {
            _subscriber.stop();
        }
        
        if (_publisher != null) {
            _publisher.stop();
        }
    }
    
    public void initialize() {
        if (_subscriber == null) {
            return;
        }
        
        //load all card, furture not load, read from redis
        _cardMap.put("0441AFDAFA4380", 
                new CardInfo("0441AFDAFA4380", "baohv"));
        _cardMap.put("fftest", 
                new CardInfo("fftest", "test"));
        
        //load all device
        _iotDeviceMap.put("gateway1", 
                new IoTDeviceInfo("gateway1", "gateway1", "", "vng-cloud", 1, DeviceType.GATEWAY, true));
        _iotDeviceMap.put("reader1", 
                new IoTDeviceInfo("gateway1", "reader1", "door1", "vng-cloud", 1, DeviceType.READER, true));
        _iotDeviceMap.put("door1", 
                new IoTDeviceInfo("gateway1", "door1", "reader1", "vng-cloud", 1, DeviceType.DOOR, true));
        
        //subscribe topic by type
        _iotDeviceMap.forEach((deviceId, deviceInfo) -> {
            if (deviceInfo.getType() == DeviceType.GATEWAY) {
                
                //update status
                String topic = String.format(AppConst.TOPIC_FORMAT, 
                        deviceInfo.getAppKey(), deviceInfo.getGatewayId(),
                        "update_status", "request");
                _subscriber.subscribe(topic, new UpdateStateController());
            }
        });
    }
    
    public IoTDeviceInfo getDevice(String deviceId) {
        return _iotDeviceMap.get(deviceId);
    }
    
    public CardInfo getCard(String cardId) {
        return _cardMap.get(cardId);
    }
    
    public void publish(TopicInfo topic, IMqttMessage msg) {
        String topicString = topic.getTopicString();
        String data = _gson.toJson(msg);
        _publisher.publish(topicString, data);
    }
}

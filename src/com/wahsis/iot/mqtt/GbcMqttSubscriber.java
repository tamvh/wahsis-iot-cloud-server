/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author haint3
 */
public class GbcMqttSubscriber {
    private static final Logger logger = Logger.getLogger(GbcMqttSubscriber.class);
    private static final int    DEFAULT_QoS = 1;
    private MemoryPersistence   _persistence = new MemoryPersistence();
    private String              _clientId = "";
    private MqttClient          _mqttClient = null;
    
    public boolean start(String uri) throws MqttException {
        _clientId = "sub" + System.identityHashCode(this);
        _mqttClient = new MqttClient(uri, _clientId, _persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        _mqttClient.connect(connOpts);
        logger.info("GbcMqttSubscriber connect success: broker = " + uri);
        return true;
    }
    
    public void stop() {
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                _mqttClient.disconnect(2000);
            } catch (MqttException ex) {
                logger.error("GbcMqttSubscriber.stop: " + ex.getMessage(), ex);
            }
        }
    }
    
    public boolean subscribe(String topic, IMqttMessageListener messageListener) {
        return subscribe(topic, DEFAULT_QoS, messageListener);
    }
    
    public boolean subscribe(String topic, int qos, IMqttMessageListener messageListener) {
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                _mqttClient.subscribe(topic, qos, messageListener);
                return true;
            } catch (MqttException ex) {
                logger.error("GbcMqttSubscriber.subscribe: " + ex.getMessage(), ex);
            }
        }
        return false;
    }
    
    public boolean subscribe(String[] topic, IMqttMessageListener[] messageListener) {
        int[] qos = new int[topic.length];
        for (int i = 0; i < qos.length; i++) {
                qos[i] = 1;
        }
        
        return subscribe(topic, qos, messageListener);
    }
    
    public boolean subscribe(String[] topic, int[] qos, IMqttMessageListener[] messageListener) {
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                _mqttClient.subscribe(topic, qos, messageListener);
                return true;
            } catch (MqttException ex) {
                logger.error("GbcMqttSubscriber.subscribe array: " + ex.getMessage(), ex);
            }
        }
        return false;
    }
    
    public void unSubscribe(String topic) {
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                _mqttClient.unsubscribe(topic);
            } catch (MqttException ex) {
                logger.error("GbcMqttSubscriber.unSubscribe: " + ex.getMessage(), ex);
            }
        }
    }
    
    public void unSubscribe(String[] topics) {
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                _mqttClient.unsubscribe(topics);
            } catch (MqttException ex) {
                logger.error("GbcMqttSubscriber.unSubscribe array: " + ex.getMessage(), ex);
            }
        }
    }
}

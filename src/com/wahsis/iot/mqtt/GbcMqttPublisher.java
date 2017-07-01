/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author haint3
 */
public class GbcMqttPublisher {
    private static final Logger logger = Logger.getLogger(GbcMqttPublisher.class);
    private static final int    DEFAULT_QoS = 0;
    private MemoryPersistence   _persistence = new MemoryPersistence();
    private String              _clientId = "";
    private MqttClient          _mqttClient = null;
    
    public boolean start(String uri) throws MqttException {
        _clientId = "pub" + System.identityHashCode(this);
        _mqttClient = new MqttClient(uri, _clientId, _persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        _mqttClient.connect(connOpts);
        logger.info("GbcMqttPublisher connect success: broker = " + uri);
        return true;
    }
    
    public void stop() {
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                _mqttClient.disconnect(2000);
            } catch (MqttException ex) {
                logger.error("GbcMqttPublisher.stop: " + ex.getMessage(), ex);
            }
        }
    }
    
    public boolean publish(String topic, String data) {
        return publish(topic, data.getBytes(), DEFAULT_QoS, false);
    }
    
    public boolean publish(String topic, byte[] data) {
        return publish(topic, data, DEFAULT_QoS, false);
    }
    
    public boolean publish(String topic, byte[] data, int qos, boolean retain) {
        
        if (_mqttClient != null && _mqttClient.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(data);
                message.setQos(qos);
                message.setRetained(retain);
                _mqttClient.publish(topic, message);
                return true;
            } catch (MqttException ex) {
                logger.error("GbcMqttPublisher.publish: " + ex.getMessage(), ex);
            }
        }
        return false;
    }
}

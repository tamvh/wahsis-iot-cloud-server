/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.task;

import com.wahsis.iot.controller.LightController;
import com.wahsis.iot.data.Light;
import com.wahsis.iot.common.MessageType;
import com.wahsis.iot.controller.NotifyController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author diepth
 */
public class AddLogTask implements Runnable {
    
    private static AddLogTask _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static final Gson _gson = new Gson();
    private static final BlockingQueue<AddLogMessage> msgQueue = new LinkedBlockingQueue<AddLogMessage>();
    private static final int MSG_ADD_OPEN_DOOR_LOG = 1;
    private static final int MSG_ADD_SWITCH_LIGHT_LOG = 2;
    private static final int MSG_QUIT = 3;
    private static final int MSG_ADD_DIM_LIGHT_LOG = 4;
    private static final int MSG_ADD_DIM_LIGHT_GROUP_LOG = 5;
    private static final int MSG_ADD_SWITCH_LIGHT_GROUP_LOG = 6;
    private Thread th = null;
    private Boolean start = false;
    
    public static AddLogTask getInstance()  {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new AddLogTask();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    @Override
    public void run(){
        
        AddLogMessage msg = null;
        while (true) {
            
            try {
                msg = msgQueue.take();
                if (msg != null) {
                    if (msg.type == MSG_ADD_SWITCH_LIGHT_LOG) { 
                        
                        Light light = Light.class.cast(msg.data);
                        
                        JsonObject jsonData = new JsonObject();
                
                        jsonData.addProperty("light_code", light.getLight_code());
                        jsonData.addProperty("on_off", light.getOn_off());
                        jsonData.addProperty("brightness", light.getBrightness());

                        String postData = _gson.toJson(jsonData);
                        
                        LightController controller = new LightController();
                        controller.updateOnOff(postData);
                        
                    }
                    else if (msg.type == MSG_ADD_DIM_LIGHT_LOG) { 
                        
                        Light light = Light.class.cast(msg.data);
                        
                        JsonObject jsonData = new JsonObject();
                
                        jsonData.addProperty("light_code", light.getLight_code());
                        jsonData.addProperty("brightness", light.getBrightness());

                        String postData = _gson.toJson(jsonData);
                        
                        LightController controller = new LightController();
                        controller.updateBrightness(postData);
                        
                    }
                    else if(msg.type == MSG_ADD_DIM_LIGHT_GROUP_LOG){
                        Map<String,Integer> mapdata= Map.class.cast(msg.data);
                        int area_id = mapdata.get("area_id");
                        int brightness = mapdata.get("brightness");
                        JsonObject jsonData = new JsonObject();
                
                        jsonData.addProperty("area_id", area_id);
                        jsonData.addProperty("brightness", brightness);

                        String postData = _gson.toJson(jsonData);
                        
                        LightController controller = new LightController();
                        controller.updateBrightnessGroup(postData);
                    }
                    else if(msg.type == MSG_ADD_SWITCH_LIGHT_GROUP_LOG){
                         Map<String,Integer> mapdata= Map.class.cast(msg.data);
                        int area_id = mapdata.get("area_id");
                        int on_off = mapdata.get("on_off");
                        int brightness = mapdata.get("brightness");
                        JsonObject jsonData = new JsonObject();
                
                        jsonData.addProperty("area_id", area_id);
                        jsonData.addProperty("on_off", on_off);
                        jsonData.addProperty("brightness", brightness);

                        String postData = _gson.toJson(jsonData);
                        
                        LightController controller = new LightController();
                        controller.updateOnOffArea(postData);
                    }
                    else if (msg.type == MSG_QUIT) { 
                        break;
                    }
                }
            } catch (Exception ex) {
                  logger.error("NotifyController.sendMessageToClient: " + ex.getMessage(), ex);
            }
        }
    }
    
    public void start() {
        
        if (th == null) {
            start = true;
            th = new Thread (this);
            th.start ();
        }
    }
    
    public void stop() {
        
        try {
            start = false;
            AddLogMessage msg = new AddLogMessage(MSG_QUIT, null);
            msgQueue.offer(msg);
            th.join();
        } catch (Exception e) {
        }
    }
    
    public void addSwitchLightMessage(Light data) {
        
        AddLogMessage msg = new AddLogMessage(MSG_ADD_SWITCH_LIGHT_LOG, data);
        msgQueue.offer(msg);
    }
    
    public void addSwitchLightGroupMessage(Map data) {
        
        AddLogMessage msg = new AddLogMessage(MSG_ADD_SWITCH_LIGHT_GROUP_LOG, data);
        msgQueue.offer(msg);
    }
    
    public void addDimLightMessage(Light data) {
        
        AddLogMessage msg = new AddLogMessage(MSG_ADD_DIM_LIGHT_LOG, data);
        msgQueue.offer(msg);
    }
    
    public void addDimGroupMessage(Map data) {
        
        AddLogMessage msg = new AddLogMessage(MSG_ADD_DIM_LIGHT_GROUP_LOG, data);
        msgQueue.offer(msg);
    }
    
    private class AddLogMessage {
        
        private int type;
        private Object data;
        
        public AddLogMessage(int type, Object data) {
            this.type = type;
            this.data = data;
        }
    }
}

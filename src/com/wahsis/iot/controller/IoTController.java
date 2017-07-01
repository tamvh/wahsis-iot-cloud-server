/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.controller;

import com.wahsis.iot.common.AppConst;
import com.wahsis.iot.common.CommonModel;
import com.wahsis.iot.common.JsonParserUtil;
import com.wahsis.iot.common.CommonFunction;
import com.wahsis.iot.data.Device;
import com.wahsis.iot.data.Reader;
import com.wahsis.iot.data.Gateway;
import com.wahsis.iot.data.Door;
import com.wahsis.iot.data.Light;
import com.wahsis.iot.data.Tag;
import com.wahsis.iot.data.OpenDoorLog;
import com.wahsis.iot.model.DeviceModel;
import com.wahsis.iot.model.GatewayModel;
import com.wahsis.iot.model.ReaderModel;
import com.wahsis.iot.model.DoorModel;
import com.wahsis.iot.model.LightModel;
import com.wahsis.iot.model.TagModel;
import com.wahsis.iot.task.AddLogTask;
import com.wahsis.iot.mqtt.GbcMqttPublisher;
import com.wahsis.iot.mqtt.MqttManager;
import com.wahsis.iot.mqtt.data.CardInfo;
import com.wahsis.iot.mqtt.data.IMqttMessage;
import com.wahsis.iot.mqtt.data.IoTDeviceInfo;
import com.wahsis.iot.mqtt.data.MsgChangeState;
import com.wahsis.iot.mqtt.data.TopicInfo;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author haint3
 */
public class IoTController extends HttpServlet {
    protected static final Logger logger = Logger.getLogger(IoTController.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }
    
    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            processs(req, resp);
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".handle: " + ex.getMessage(), ex);
        }
    }
    
    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String content = "";
       
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        
        switch (cmd) {
            case "turn_on":
                logger.info(getClass().getSimpleName() + ".processs " + "turn_on: " + data);
                content = turnOn(data);
                break;
        }
        
        CommonModel.out(content, resp);
    }
    
    private String turnOn(String data) {
        String content = "";
        
        JsonObject jsonObj = JsonParserUtil.parseJsonObject(data);
        //if (jsonObj.has("gateway_id")) {
        if (jsonObj != null) {
            content = turnOnFromGateway(jsonObj);
        } else {
             content = CommonModel.FormatResponse(-1, "Invalid parameter");
        }
            
        
        return content;
    }
    
    private String turnOnFromGateway(JsonObject jobj) {
        
        String content = "";
        
        try {
            if (jobj.has("gateway_id") == false ||
                    jobj.has("card_id") == false ||
                    jobj.has("device_id") == false) {
               content = CommonModel.FormatResponse(-1, "Invalid parameter");
               logger.error("turnOnFromGateway: Invalid parameter");
                return content;
            }

            String gatewayId = jobj.get("gateway_id").getAsString();
            String card_id = jobj.get("card_id").getAsString();
            String device_id = jobj.get("device_id").getAsString();

            /*
            CardInfo cardInfo = MqttManager.getInstance().getCard(card_id);
            if (cardInfo == null || cardInfo.getCardId() == null) {
                content = CommonModel.FormatResponse(1, "Card id invalid", "");
                return content;
            }

            IoTDeviceInfo gatewayInfo = MqttManager.getInstance().getDevice(gatewayId);
            if (gatewayInfo == null || gatewayInfo.getDeviceId() == null) {
                content = CommonModel.FormatResponse(1, "Gateway id invalid", "");
                return content;
            }

            IoTDeviceInfo deviceInfo = MqttManager.getInstance().getDevice(device_id);
            if (deviceInfo == null || deviceInfo.getDeviceId() == null) {
                content = CommonModel.FormatResponse(1, "Device id invalid", "");
                return content;
            }
            */


            /*
            if (deviceInfo.getState() == AppConst.TURN_ON) {
                content = "";
                return content;
            }
            */

           // deviceInfo.setState(AppConst.TURN_ON);

            Gateway gateway = GatewayModel.getInstance().getGateway(gatewayId);
            if (gateway == null) {
                content = CommonModel.FormatResponse(1, "Gateway id is invalid");
                logger.error("turnOnFromGateway: Gateway id is invalid");
                return content;
            }

            Reader reader = ReaderModel.getInstance().getReader(device_id);
            if (reader == null) {
                content = CommonModel.FormatResponse(1, "Reader id is invalid");
                logger.error("turnOnFromGateway: Reader id is invalid");
                return content;
            }

            Door door = DoorModel.getInstance().getDoorFromDoorValue(reader.getDoor_mask());
            if (door == null) {
                content = CommonModel.FormatResponse(2, "Reader was not assigned to any door");
                logger.error("turnOnFromGateway: Reader was not assigned to any door");
                return content;
            }

            Tag tag = TagModel.getInstance().getTag(card_id);
            if (tag == null) {
                content = CommonModel.FormatResponse(1, "Card id is invalid");
                logger.error("turnOnFromGateway: Card id is invalid");
                return content;
            }
            
            //check tag_code valid:card not expired and status=1
        //    if(!TagModel.getInstance().checkValidTagCode(card_id)){
        //        content = CommonModel.FormatResponse(1, "Card id is expired or not active");
        //        return content;
        //    }

            if ((tag.getDoor_mask() & door.getDoor_value()) == 0) {
                content = CommonModel.FormatResponse(3, "The tag is not allowed");
                logger.error("turnOnFromGateway: The tag is not allowed");
                return content;
            }

            TopicInfo topic = new TopicInfo("vng-cloud", gatewayId, "change_state", "request");
            IMqttMessage<MsgChangeState> mqttMsg = new IMqttMessage<>();
            //MsgChangeState changeState = new MsgChangeState(deviceInfo.getRelativeId(), AppConst.TURN_ON);
            MsgChangeState changeState = new MsgChangeState(door.getDoor_code(), AppConst.TURN_ON);
            mqttMsg.setMsgData(changeState);
            mqttMsg.setErrCode(0);
            mqttMsg.setMsg("");
            mqttMsg.setMsgId(1);
            MqttManager.getInstance().publish(topic, mqttMsg);

            JsonObject jsonRes = new JsonObject();
            jsonRes.addProperty("device_id", door.getDoor_code());
            jsonRes.addProperty("new_state", AppConst.TURN_ON);

            content = CommonModel.FormatResponse(0, "", jsonRes);

            OpenDoorLog openDoorLog = new OpenDoorLog();

            openDoorLog.setTag_code(card_id);
            openDoorLog.setDoor_name(door.getDoor_name());
            //openDoorLog.setEmployee_account(tag.getEmployee_account());
            openDoorLog.setEmployee_id(tag.getEmployee_id());
            openDoorLog.setOpen_date_time(CommonFunction.getCurrentDateTime());
        //    openDoorLog.setCheck_type(reader.getPosition());
            
            AddLogTask.getInstance().addOpenDoorMessage(openDoorLog);
            logger.info("turnOnFromGateway: open door successfully");
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".turnOnFromGateway: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(-1, ex.getMessage());
        }
        
        return content;
    }
}

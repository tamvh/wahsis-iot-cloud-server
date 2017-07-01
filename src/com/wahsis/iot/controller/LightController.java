/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.controller;

import com.wahsis.iot.common.AppConst;
import com.wahsis.iot.common.CommonModel;
import com.wahsis.iot.common.JsonParserUtil;
import com.wahsis.iot.common.MessageType;
import com.wahsis.iot.data.Gateway;
import com.wahsis.iot.data.Light;
import com.wahsis.iot.model.AreaModel;
import com.wahsis.iot.model.LightModel;
import com.wahsis.iot.model.GatewayModel;
import com.wahsis.iot.task.AddLogTask;
//import com.gbc.iot.model.
import com.wahsis.iot.mqtt.MqttManager;
import com.wahsis.iot.mqtt.data.IMqttMessage;
import com.wahsis.iot.mqtt.data.MsgChangeState;
import com.wahsis.iot.mqtt.data.MsgChangeStateGroup;
import com.wahsis.iot.mqtt.data.TopicInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author diepth
 */
public class LightController extends HttpServlet {

    protected final Logger logger = Logger.getLogger(this.getClass());
    private static final Gson _gson = new Gson();

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

        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String content = "";

        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        switch (cmd) {
            case "switch_on_off":
                content = switchOnOff(data);
                break;
            case "change_brightness":
                content = changeBrightness(data);
                break;
            case "update_on_off":
                content = updateOnOff(data);
                break;
            case "update_brightness":
                content = updateBrightness(data);
                break;
            case "change_brightness_group":
                content = changeBrightnessGroup(data);
                break;
            case "switch_on_off_area":
                content = switchOnOffArea(data);
                break;
            case "update":
                break;
            case "insert":
                content = updateLightItem(data);
                break;

        }

        CommonModel.out(content, resp);
    }

    public String updateLightItem(String data) {

        String content;
        int ret = -1;

        try {

            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {

                Light light = new Light();
                light = _gson.fromJson(jsonObject.get("light").getAsJsonObject(), Light.class);
                int light_area_id = (int) light.getArea_id();
                IMqttMessage<MsgChangeState> mqttMsg = new IMqttMessage<>();
                MsgChangeState changeState = new MsgChangeState(light.getLight_code(), light_area_id);
                mqttMsg.setMsgData(changeState);
                mqttMsg.setErrCode(0);
                mqttMsg.setMsg("");
                mqttMsg.setMsgId(5);//5: update light's area_id

                List<Gateway> listGateway = GatewayModel.getInstance().getGatewayList();

                for (Gateway gateway : listGateway) {
                    TopicInfo topic = new TopicInfo("vng-cloud", gateway.getGateway_code(), "switch_on_off", "request");
                    MqttManager.getInstance().publish(topic, mqttMsg);
                }

                content = CommonModel.FormatResponse(0, "", "light", light);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateLightItem: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String switchOnOffArea(String data) {

        String content;
        int ret = -1;

        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int area_id = jsonObject.get("area_id").getAsInt();
                int on_off = jsonObject.get("on_off").getAsInt();
                int brightness = AreaModel.getInstance().getBrightnessArea(area_id);

                if (brightness >= 0) {
                    IMqttMessage<MsgChangeStateGroup> mqttMsg = new IMqttMessage<>();
                    if (on_off == 0) {
                        brightness = 0;
                    } else {
                        if (brightness == 0) {
                            brightness = 50;
                        }
                    }
                    //-----public msg to mqtt chanel-----
                    MsgChangeStateGroup changeStateGroup = new MsgChangeStateGroup(area_id, brightness);
                    mqttMsg.setMsgData(changeStateGroup);
                    mqttMsg.setErrCode(0);
                    mqttMsg.setMsg("");
                    mqttMsg.setMsgId(4);

                    List<Gateway> listGateway = GatewayModel.getInstance().getGatewayList();

                    for (Gateway gateway : listGateway) {
                        TopicInfo topic = new TopicInfo("wahsis-cloud", gateway.getGateway_code(), "switch_on_off", "request");
                        MqttManager.getInstance().publish(topic, mqttMsg);
                    }
                    
                    Map mapdata = new HashMap();
                    mapdata.put("area_id", area_id);
                    mapdata.put("brightness", brightness);
                    mapdata.put("on_off", on_off);
                    AddLogTask.getInstance().addSwitchLightGroupMessage(mapdata);
                    //----- end public msg to mqtt chanel-----
                    
                    content = CommonModel.FormatResponse(0, "", "brightness", brightness);
                } else {
                    content = CommonModel.FormatResponse(-1, "");
                }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".switchOnOff: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String switchOnOff(String data) {

        String content = null;
        int ret = -1;

        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {

                Light light = new Light();
                light = _gson.fromJson(jsonObject.get("light").getAsJsonObject(), Light.class);
                int brightness = LightModel.getInstance().getBrightness(light.getLight_code());
                if (brightness >= 0) {
                    IMqttMessage<MsgChangeState> mqttMsg = new IMqttMessage<>();
                    if (light.getOn_off() == 0) {
                        brightness = 0;
                    } else {
                        if (brightness == 0) {
                            brightness = 50;
                        }
                    }

                    //-----public msg to mqtt chanel-----
                    MsgChangeState changeState = new MsgChangeState(light.getLight_code(), brightness);
                    mqttMsg.setMsgData(changeState);
                    mqttMsg.setErrCode(0);
                    mqttMsg.setMsg("");
                    mqttMsg.setMsgId(3);

                    List<Gateway> listGateway = GatewayModel.getInstance().getGatewayList();
                    for (Gateway gateway : listGateway) {
                        TopicInfo topic = new TopicInfo("wahsis-cloud", gateway.getGateway_code(), "switch_on_off", "request");
                        MqttManager.getInstance().publish(topic, mqttMsg);
                    }
                    //----- end public msg to mqtt chanel-----

                    light.setBrightness(brightness);
                    content = CommonModel.FormatResponse(0, "", "light", light);

                    AddLogTask.getInstance().addSwitchLightMessage(light);
                } else {
                    content = CommonModel.FormatResponse(-1, "");
                }

            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".switchOnOff: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String changeBrightness(String data) {

        String content;
        int ret = -1;

        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {

                Light light = new Light();
                light = _gson.fromJson(jsonObject.get("light").getAsJsonObject(), Light.class);

                //-----public msg to mqtt chanel-----
                IMqttMessage<MsgChangeState> mqttMsg = new IMqttMessage<>();
                MsgChangeState changeState = new MsgChangeState(light.getLight_code(), light.getBrightness());
                mqttMsg.setMsgData(changeState);
                mqttMsg.setErrCode(0);
                mqttMsg.setMsg("");
                mqttMsg.setMsgId(3);

                List<Gateway> listGateway = GatewayModel.getInstance().getGatewayList();

                for (Gateway gateway : listGateway) {
                    TopicInfo topic = new TopicInfo("wahsis-cloud", gateway.getGateway_code(), "switch_on_off", "request");
                    MqttManager.getInstance().publish(topic, mqttMsg);
                }
                //----- end public msg to mqtt chanel-----

                content = CommonModel.FormatResponse(0, "", "light", light);

                AddLogTask.getInstance().addDimLightMessage(light);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".changeBrightness: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String changeBrightnessGroup(String data) {

        String content;
        int ret = -1;

        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int area_id = jsonObject.get("area_id").getAsInt();
                int brightness = jsonObject.get("brightness").getAsInt();

                IMqttMessage<MsgChangeStateGroup> mqttMsg = new IMqttMessage<>();
                MsgChangeStateGroup changeState = new MsgChangeStateGroup(area_id, brightness);
                mqttMsg.setMsgData(changeState);
                mqttMsg.setErrCode(0);
                mqttMsg.setMsg("");
                mqttMsg.setMsgId(4);//4: update brightness for group

                List<Gateway> listGateway = GatewayModel.getInstance().getGatewayList();
                //listGateway.forEach((gateway) -> {
                //    TopicInfo topic = new TopicInfo("vng-cloud", gateway.getGateway_code(), "change_state", "request");
                //    MqttManager.getInstance().publish(topic, mqttMsg);
                //});

                for (Gateway gateway : listGateway) {
                    TopicInfo topic = new TopicInfo("vng-cloud", gateway.getGateway_code(), "switch_on_off", "request");
                    MqttManager.getInstance().publish(topic, mqttMsg);
                }

                content = CommonModel.FormatResponse(0, "", "brightness", brightness);
                Map mapdata = new HashMap();
                mapdata.put("area_id", area_id);
                mapdata.put("brightness", brightness);
                AddLogTask.getInstance().addDimGroupMessage(mapdata);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".changeBrightnessGroup: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String updateBrightness(String data) {

        String content;
        int ret = -1;

        try {

            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                String lightCode = jsonObject.get("light_code").getAsString();
                int brightness = jsonObject.get("brightness").getAsInt();
                int on_off = 0;
                if (brightness == 0) {
                    on_off = 0;
                } else {
                    on_off = 1;
                }

                ret = LightModel.getInstance().updateBrightness(lightCode, on_off, brightness);
                content = CommonModel.FormatResponse(ret, "");

                JsonObject jsonMain = new JsonObject();
                JsonObject jsonData = new JsonObject();

                jsonData.addProperty("light_code", lightCode);
                jsonData.addProperty("brightness", brightness);
                jsonData.addProperty("on_off", on_off);

                jsonMain.addProperty("msg_type", MessageType.MSG_TYPE_GET_DATA_5);
                jsonMain.add("dt", jsonData);
                String sendData = _gson.toJson(jsonMain);

                NotifyController.sendMessageToClient(sendData);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateBrightness: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String updateOnOff(String data) {

        String content;
        int ret = -1;

        try {

            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                String lightCode = jsonObject.get("light_code").getAsString();
                int onOff = jsonObject.get("on_off").getAsInt();
                int brightness = jsonObject.get("brightness").getAsInt();
                if (onOff == 0) {
                    LightModel.getInstance().updateOnOff(lightCode, onOff);
                } else {
                    ret = LightModel.getInstance().updateBrightness(lightCode, onOff, brightness);
                }

                content = CommonModel.FormatResponse(ret, "");

                JsonObject jsonMain = new JsonObject();
                JsonObject jsonData = new JsonObject();

                jsonData.addProperty("light_code", lightCode);
                jsonData.addProperty("on_off", onOff);
                jsonData.addProperty("brightness", brightness);

                jsonMain.addProperty("msg_type", MessageType.MSG_TYPE_GET_DATA_4);
                jsonMain.add("dt", jsonData);
                String sendData = _gson.toJson(jsonMain);

                NotifyController.sendMessageToClient(sendData);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateOnOff: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String updateOnOffArea(String data) {

        String content;
        int ret = -1;

        try {

            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int area_id = jsonObject.get("area_id").getAsInt();
                int onOff = jsonObject.get("on_off").getAsInt();
                int brightness = jsonObject.get("brightness").getAsInt();
                ret = LightModel.getInstance().updateOnOffAllLightByAreaID(area_id, onOff);
                content = CommonModel.FormatResponse(ret, "");

                JsonObject jsonMain = new JsonObject();
                JsonObject jsonData = new JsonObject();

                jsonData.addProperty("area_id", area_id);
                jsonData.addProperty("on_off", onOff);
                jsonData.addProperty("brightness", brightness);

                jsonMain.addProperty("msg_type", MessageType.MSG_SWITCH_LIGHT_GROUP);
                jsonMain.add("dt", jsonData);
                String sendData = _gson.toJson(jsonMain);

                NotifyController.sendMessageToClient(sendData);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateOnOff: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String updateBrightnessGroup(String data) {

        String content;
        int ret = -1;

        try {

            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int area_id = jsonObject.get("area_id").getAsInt();
                int brightness = jsonObject.get("brightness").getAsInt();
                AreaModel.getInstance().updateAreaBrightness(area_id, brightness);
                LightModel.getInstance().updateBrightnessAllLightByAreaID(area_id, brightness);
                content = CommonModel.FormatResponse(ret, "");

                JsonObject jsonMain = new JsonObject();
                JsonObject jsonData = new JsonObject();

                jsonData.addProperty("area_id", area_id);
                jsonData.addProperty("brightness", brightness);

                jsonMain.addProperty("msg_type", MessageType.MSG_RELOAD_DIM_GROUP);
                jsonMain.add("dt", jsonData);
                String sendData = _gson.toJson(jsonMain);

                NotifyController.sendMessageToClient(sendData);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateOnOff: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.controller;

import com.wahsis.iot.common.AppConst;
import com.wahsis.iot.common.CommonModel;
import com.wahsis.iot.common.CommonService;
import com.wahsis.iot.common.JsonParserUtil;
import com.wahsis.iot.common.MessageType;
import com.wahsis.iot.data.Light;
import com.wahsis.iot.model.LightModel;
import com.wahsis.iot.task.AddLogTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wahsis.iot.database.SQLConnFactory;
import com.wahsis.iot.model.AreaModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                int light_area_id = (int)light.getArea_id();

                content = CommonModel.FormatResponse(0, "", "light", light);
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateLightItem: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }

        return content;
    }

    public String switchOnOffArea(String data) {
        String content = null;
       

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
                String company_id = null;
                Light light = new Light();
                light = _gson.fromJson(jsonObject.get("light").getAsJsonObject(), Light.class);
                if(jsonObject.equals("company_id")) {
                    company_id = jsonObject.get("company_id").toString();
                }
                
                if(light!= null && !company_id.isEmpty()) {
                    ret = LightModel.getInstance().updateOnOffByID(company_id, light);
//                    AddLogTask.getInstance().addSwitchLightMessage(light);
                }
                else {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                }
                if(ret == 0) {
                    content = CommonModel.FormatResponse(ret, "switch on off success");
                } else {
                    content = CommonModel.FormatResponse(ret, "switch on off faile");
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

                // push data to crestrol cpu

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

                // push data to crestrol cpu

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

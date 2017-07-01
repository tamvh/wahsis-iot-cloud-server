/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.controller;

import com.wahsis.iot.common.CommonModel;
import com.wahsis.iot.common.JsonParserUtil;
import com.wahsis.iot.common.AppConst;
import com.wahsis.iot.model.DeviceModel;
import com.wahsis.iot.model.GatewayModel;
import com.wahsis.iot.model.ReaderModel;
import com.wahsis.iot.model.DoorModel;
import com.wahsis.iot.model.LightModel;
import com.wahsis.iot.model.TagModel;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author diepth
 */
public class CommonController extends HttpServlet {
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
       
        logger.info(getClass().getSimpleName() + ".processs: " + pathInfo);
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        
        switch (cmd) {
            case "reload_device":
                content = reloadDevice(data);
                break;
        }
        
        CommonModel.out(content, resp);
    }
    
    private String reloadDevice(String data) {
        
        String content;
        int ret = -1;
        
        try {
         
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {
                int deviceType = jsonObject.get("device_type").getAsInt();
                switch (deviceType) {
                    case AppConst.DEVICE_GATEWAY:
                        ret = GatewayModel.getInstance().reloadGateway();
                        break;
                    case AppConst.DEVICE_READER:
                        ret = ReaderModel.getInstance().reloadReader();
                        break;
                    case AppConst.DEVICE_DOOR:
                        ret = DoorModel.getInstance().reloadDoor();
                        break;
                    case AppConst.DEVICE_LIGHT:
                        ret = LightModel.getInstance().reloadLight();
                        break;
                    case AppConst.DEVICE_TAG:
                        TagModel.getInstance().reloadTag();
                        break;
                        
                }
                content = CommonModel.FormatResponse(ret, "");
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".reloadDevice: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }
}

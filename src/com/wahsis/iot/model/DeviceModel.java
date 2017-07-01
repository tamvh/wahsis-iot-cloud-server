/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.common.AppConst;
import com.wahsis.iot.data.Device;
import com.wahsis.iot.database.MySqlFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author diepth
 */
public class DeviceModel {
    
    private static DeviceModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static final Map<String, Device> mapDeviceInfo = Collections.synchronizedMap(new LinkedHashMap<String, Device>());
    
     public static DeviceModel getInstance()  {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new DeviceModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
     
    private static String getDeviceTableName() {
        String tableName;
        
        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`device`";
        
        return tableName;
    }
    
    public Boolean checkExist(String deviceCode) {
        
        Device device = mapDeviceInfo.get(deviceCode);
        if (device != null)
            return true;
        
        return false;
    } 
    
    public Device getDevice(String deviceCode) {
        
        Device device = mapDeviceInfo.get(deviceCode);
        return device;
    }
}

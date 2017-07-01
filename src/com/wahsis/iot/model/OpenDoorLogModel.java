/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.OpenDoorLog;
import com.wahsis.iot.database.MySqlFactory;
import java.io.IOException;
import java.sql.Connection;
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
public class OpenDoorLogModel {
    
    private static OpenDoorLogModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    
     public static OpenDoorLogModel getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new OpenDoorLogModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
     
    private static String getOpenDoorLogTableName() {
        String tableName;
        
        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`open_door_log`";
        
        return tableName;
    }
    
    public int addOpenDoorLog(OpenDoorLog openDoorLog) {
         
        Connection connection = null;
        Statement stmt = null;  
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String openDoorLogTableName = getOpenDoorLogTableName();
            
            queryStr = String.format("INSERT INTO %s(`tag_code`, `employee_account`, `door_name`, `check_type`, `open_date_time`) VALUES('%s', '%s', '%s', %d, '%s')", openDoorLogTableName, openDoorLog.getTag_code(), openDoorLog.getEmployee_account(), openDoorLog.getDoor_name(), openDoorLog.getCheck_type(), openDoorLog.getOpen_date_time());
            
            stmt.execute(queryStr);
            ret = 0;

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".addOpenDoorLog: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
}

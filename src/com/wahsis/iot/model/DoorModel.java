/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.Door;
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
public class DoorModel {
    
    private static DoorModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static Map<Long, Door> _mapDoorInfo = Collections.synchronizedMap(new LinkedHashMap<Long, Door>());

    public static DoorModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new DoorModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    private static String getDoorTableName() {
        String tableName;

        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`door`";

        return tableName;
    }
    
    private int loadDoor(Map<Long, Door> mapDoor) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String doorTableName =  getDoorTableName();
            String queryStr;
            String deviceCode;
            long doorValue;
            Door door;
            
            // load door list
            queryStr = String.format("SELECT `door_id`, `door_code`, `door_name`, `door_value` FROM %s", doorTableName);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            
            if (rs != null) {
                while (rs.next()) {
                    door = new Door();
                    deviceCode = rs.getString("door_code");
                    
                    door.setDoor_id(rs.getLong("door_id"));
                    door.setDoor_code(deviceCode);
                    door.setDoor_name(rs.getString("door_name"));
                    
                    doorValue = rs.getLong("door_value");
                    door.setDoor_value(doorValue);
                    
                    mapDoor.put(doorValue, door);
                }
                
                ret = 0;
            }
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadDoor: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
    
    public int loadDoor() {
        return loadDoor(_mapDoorInfo);
    }
    
    public int reloadDoor() {
        int ret;
        
        Map<Long, Door> mapDoor = Collections.synchronizedMap(new LinkedHashMap<Long, Door>());
        ret = loadDoor(mapDoor);
        _mapDoorInfo = mapDoor;
        
        return ret;
    }
    
     //public Door getDoor(String doorCode) {
    //    
    //    Door door = mapDoorInfo.get(doorCode);
    //    return door;
    //}
    
    public Door getDoorFromDoorValue(long doorValue) {
        
        Door door = _mapDoorInfo.get(doorValue);
        return door;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.Area;
import com.wahsis.iot.database.MySqlFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author sinhnd
 */
public class AreaModel {
    
    private static AreaModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private int AREA_ACTIVE = 1;
    public static AreaModel getInstance()  {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new AreaModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
     
    
    private static String getAreaTableName() {
        String tableName;

        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`area`";

        return tableName;
    }
    
    public int getBrightnessArea(int area_id) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getAreaTableName();
            String queryStr;
            
            queryStr = String.format("SELECT `area_brightness` FROM %s WHERE `area_id` = %d AND `active` = %d", 
                    tableName, 
                    area_id, 
                    AREA_ACTIVE);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
             if (rs != null) {
                if (rs.next()) {
                    ret = rs.getInt("area_brightness");   
                }   
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getBrightness: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public int updateAreaBrightness(int area_id, int brightness) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs;
        int ret = -1;

        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String tableAreaName = getAreaTableName();

            queryStr = String.format("SELECT `area_id` FROM %1$s WHERE `area_id` = %2$d", tableAreaName,  area_id);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null && rs.next()) {
           
                queryStr = String.format("UPDATE %1$s SET `area_brightness`=%2$d WHERE area_id=%3$d",
                        tableAreaName, brightness, area_id);;
                stmt.execute(queryStr);
                ret = 0;
            }

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".updateAreaBrightness: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
}

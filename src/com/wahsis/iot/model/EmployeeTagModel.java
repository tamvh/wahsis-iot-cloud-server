/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.wahsis.iot.data.EmployeeTag;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.EmployeeTag;
import com.wahsis.iot.database.MySqlFactory;

/**
 *
 * @author diepth
 */
public class EmployeeTagModel {
    
    private static EmployeeTagModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static  Map<String, EmployeeTag> _mapEmployeeTag = Collections.synchronizedMap(new LinkedHashMap<String, EmployeeTag>());
    
     public static EmployeeTagModel getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new EmployeeTagModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    private static String getEmployeeTagTableName() {
        String tableName;
        
        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`employee_tag`";
        
        return tableName;
    }
     
    public int loadData() {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String employeeTagTableName =  getEmployeeTagTableName();
            String tagCode;
            EmployeeTag employeeTag;
            
            queryStr = String.format("SELECT `tag_id`, `tag_code`, `employee_code`, `employee_account`, `employee_name`, `avatar_url`, `door_mask` FROM %s", employeeTagTableName);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            
            Map<String, EmployeeTag> mapEmployeeTag = Collections.synchronizedMap(new LinkedHashMap<String, EmployeeTag>());
            if (rs != null) {
                while (rs.next()) {
                    employeeTag = new EmployeeTag();
                    tagCode = rs.getString("tag_code");
                    
                    employeeTag.setTag_id(rs.getLong("tag_id"));
                    employeeTag.setTag_code(tagCode);
                    employeeTag.setEmployee_code(rs.getString("employee_code"));
                    employeeTag.setEmployee_account(rs.getString("employee_account"));
                    employeeTag.setEmployee_name(rs.getString("employee_name"));
                    employeeTag.setAvatar_url(rs.getString("avatar_url"));
                    employeeTag.setDoor_mask(rs.getLong("door_mask"));
                   
                    mapEmployeeTag.put(tagCode, employeeTag);
                }
                
                ret = 0;
            }
            _mapEmployeeTag = mapEmployeeTag;
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadData: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
    
    public int reloadTag() {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String employeeTagTableName =  getEmployeeTagTableName();
            String tagCode;
            EmployeeTag employeeTag;
            
            queryStr = String.format("SELECT `tag_id`, `tag_code`, `employee_code`, `employee_account`, `employee_name`, `avatar_url`, `door_mask` FROM %s", employeeTagTableName);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            
             if (rs != null) {
                while (rs.next()) {
                    employeeTag = new EmployeeTag();
                    tagCode = rs.getString("tag_code");
                    
                    employeeTag.setTag_id(rs.getLong("tag_id"));
                    employeeTag.setTag_code(tagCode);
                    employeeTag.setEmployee_code(rs.getString("employee_code"));
                    employeeTag.setEmployee_account(rs.getString("employee_account"));
                    employeeTag.setEmployee_name(rs.getString("employee_name"));
                    employeeTag.setAvatar_url(rs.getString("avatar_url"));
                    employeeTag.setDoor_mask(rs.getLong("door_mask"));
                   
                    _mapEmployeeTag.put(tagCode, employeeTag);
                }
                
                ret = 0;
            }
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadData: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
    
    public Boolean checkExist(String tagCode) {
        
        EmployeeTag employeeTag = _mapEmployeeTag.get(tagCode);
        if (employeeTag != null)
            return true;
        
        return false;
    } 
    
    public EmployeeTag getEmployeeTag(String tagCode) {
        
        EmployeeTag employeeTag = _mapEmployeeTag.get(tagCode);
        return employeeTag;
    }
}

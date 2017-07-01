/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.database.MySqlFactory;
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
public class EmployeeModel {
    
    private static EmployeeModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
 //   private static  Map<String, Employee> _mapEmployeeTag = Collections.synchronizedMap(new LinkedHashMap<String, Employee>());
    
     public static EmployeeModel getInstance() {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new EmployeeModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    private static String getEmployeeTableName() {
        String tableName;
        
        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`employee`";
        
        return tableName;
    }
     
    public String getEmployeeAcoountFromId(long employeeID){
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String epyaAccount = "";
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String employeeTableName =  getEmployeeTableName();
            //Tag tag;
            
            queryStr = String.format("SELECT employee_account FROM %s WHERE employee_id = %d", employeeTableName, employeeID);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
       
             if (rs != null) {
                  if (rs.next()) {
                      epyaAccount = rs.getString("employee_account");
                  }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".getEmployeeAcoountFromId: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return epyaAccount;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.Gateway;
import com.wahsis.iot.database.MySqlFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author diepth
 */
public class GatewayModel {
    
    private static GatewayModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static Map<String, Gateway> _mapGatewayInfo = Collections.synchronizedMap(new LinkedHashMap<String, Gateway>());

    public static GatewayModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new GatewayModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    private static String getGatewayTableName() {
        String tableName;

        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`gateway`";

        return tableName;
    }
    
    private int loadGateway(Map<String, Gateway> mapGateway) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String gatewayTableName =  getGatewayTableName();
            String queryStr;
            String deviceCode;
            Gateway gateway;
            
            // load gateway list
            queryStr = String.format("SELECT `gateway_id`, `gateway_code`, `gateway_name` FROM %s", gatewayTableName);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            
            if (rs != null) {
                while (rs.next()) {
                    gateway = new Gateway();
                    deviceCode = rs.getString("gateway_code");
                    
                    gateway.setGateway_id(rs.getLong("gateway_id"));
                    gateway.setGateway_code(deviceCode);
                    gateway.setGateway_name(rs.getString("gateway_name"));
                    
                    mapGateway.put(deviceCode, gateway);
                }
                
                ret = 0;
            }
            
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadGateway: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
    
    public int loadGateway() {
        return loadGateway(_mapGatewayInfo);
    }
    
    public int reloadGateway() {
        int ret;
        
        Map<String, Gateway> mapGateway = Collections.synchronizedMap(new LinkedHashMap<String, Gateway>());
        ret = loadGateway(mapGateway);
        if (ret == 0)
            _mapGatewayInfo = mapGateway;
        
        return ret;
    }
    
    public Gateway getGateway(String gatewayCode) {
        
        Gateway gateway = _mapGatewayInfo.get(gatewayCode);
        return gateway;
    }
    
    public List<Gateway> getGatewayList() {
        List<Gateway> listGateway = new ArrayList<>(_mapGatewayInfo.values());
        
        return listGateway;
    }
}

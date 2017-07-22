/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.CommonService;
import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.Light;
import com.wahsis.iot.database.MySqlFactory;
import com.wahsis.iot.database.MsSqlFactory;
import com.wahsis.iot.database.SQLConnFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class LightModel {
    
    private static LightModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static Map<String, Light> _mapLightInfo = Collections.synchronizedMap(new LinkedHashMap<String, Light>());
    private int LIGHT_ACTIVE = 1;
   
    public static LightModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new LightModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    private static String getLightTableName() {
        String tableName;
        
        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`light`";
        
        return tableName;
    }
    
    public int loadLight(Map<String, Light> mapLight) {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int ret = -1;
        
        try {            
            connection = MsSqlFactory.getConnection();
            String lightTableName =  "room_area_light";
            String queryStr;
            String deviceCode;
            Light light;
            
            // load light list
            queryStr = String.format("SELECT * FROM %s", lightTableName);
            
            stmt = connection.prepareStatement(queryStr);
            rs = stmt.executeQuery();
            
            if (rs != null) {
                while (rs.next()) {
                    light = new Light();
                    deviceCode = rs.getString("light_code");                    
                    light.setLight_id(rs.getLong("light_id"));
                    light.setLight_code(deviceCode);
                    light.setLight_name(rs.getString("light_name"));
                    light.setArea_id(rs.getLong("area_id"));
                    light.setOn_off(rs.getInt("on_off"));                    
                    mapLight.put(deviceCode, light);
                }
                
                ret = 0;
            }
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadLight: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
     
    public int loadLight() {
        return loadLight(_mapLightInfo);
    }
    
    public int reloadLight() {
        int ret;
        Map<String, Light> mapLight = Collections.synchronizedMap(new LinkedHashMap<String, Light>());
        ret = loadLight(mapLight);
        if (ret == 0)
            _mapLightInfo = mapLight;
        
        return ret;
    }
    
    public Light getLight(String lightCode) {
        
        Light light = _mapLightInfo.get(lightCode);
        return light;
    }
    
    
    public int updateOnOff(String light_code, int on_off) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getLightTableName();
            String queryStr;
            
            queryStr = String.format("UPDATE %s SET `on_off` = %d WHERE `light_code` = '%s'", tableName, on_off, light_code);
            stmt.execute(queryStr);
            ret = 0;

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateOnOff: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public int updateBrightness(String light_code, int on_off, int brightness) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getLightTableName();
            String queryStr;
            
            queryStr = String.format("UPDATE %s SET `on_off` = %d, `brightness` = %d WHERE `light_code` = '%s'", tableName, on_off, brightness, light_code);
            stmt.execute(queryStr);
            ret = 0;

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateBrightness: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public int updateBrightnessAllLightByAreaID(int area_id, int brightness) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getLightTableName();
            String queryStr;
            if(brightness == 0)
                queryStr = String.format("UPDATE %s SET `brightness` = %d, `on_off` = 0 WHERE `area_id` = %d", tableName, brightness, area_id);
            queryStr = String.format("UPDATE %s SET `brightness` = %d , `on_off`  = 1 WHERE `area_id` = %d", tableName, brightness, area_id);
            stmt.execute(queryStr);
            ret = 0;

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateBrightnessAllLightByAreaID: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public int updateBrightnessAllLightByAreaID(int area_id, int on_off, int brightness) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getLightTableName();
            String queryStr;
            queryStr = String.format("UPDATE %s SET `brightness` = %d , `on_off`  = %d WHERE `area_id` = %d", tableName, brightness, on_off, area_id);
            stmt.execute(queryStr);
            ret = 0;

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateBrightnessAllLightByAreaID: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public int updateOnOffAllLightByAreaID(int area_id, int on_off) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getLightTableName();
            String queryStr;
            queryStr = String.format("UPDATE %s SET `on_off`  = %d WHERE `area_id` = %d", tableName, on_off, area_id);
            stmt.execute(queryStr);
            ret = 0;

        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".updateOnOffAllLightByAreaID: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public int getBrightness(String light_code) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            String tableName = getLightTableName();
            String queryStr;
            
            queryStr = String.format("SELECT `brightness` FROM %s WHERE `light_code` = '%s' AND `active` = %d", 
                    tableName, 
                    light_code,
                    LIGHT_ACTIVE);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
             if (rs != null) {
                if (rs.next()) {
                    ret = rs.getInt("brightness");   
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
    public int updateOnOffByID(String company_id, Light light) {
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement cs = null;
        int ret = -1;
        try {
            int brightness = 0;
            // light type: 0(dim), 1(onoff)
            if (light.getIsActiveBrightness() == 0) {
                if (light.getOn_off()== 0) {
                    brightness = 0;
                } else {
                    brightness = 100;
                }
            } else {
                brightness = light.getBrightness();
            }
            connection = SQLConnFactory.getConnection(company_id);
            cs = connection.prepareStatement("SET NOCOUNT ON ; \n "
                    + " update " + CommonService.getTableName(company_id, "room_area_light") + " \n "
                    + " set [on_off] = ? , [brightness] = ? \n "
                    + " where light_code = ? ; \n  "
                    + " SELECT 1 as result; \n "
                    + " select top 1 l.* \n "
                    + " from " + CommonService.getTableName(company_id, "room_area_light") + " as l \n "
                    + " where l.light_code = ? "
                    + "");
            int count = 1;
            cs.setInt(count++, light.getOn_off());
            cs.setInt(count++, brightness);
            cs.setString(count++, light.getLight_code());
            cs.setString(count++, light.getLight_code());
            boolean kq = cs.execute();
            if (kq) {
                rs = cs.getResultSet();
                if (rs.next()) {
                    if (rs.getLong(1) > 0 && cs.getMoreResults()) {
                        rs = cs.getResultSet();
                        if (rs.next()) {
                            light.setLight_id(rs.getLong("light_id"));
                            light.setLight_code(rs.getString("light_code"));
                            light.setOn_off(rs.getInt("on_off"));
                            light.setBrightness(rs.getInt("brightness"));
                        }
                        ret = 0;
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("RoomAreaLightModelSQL.updateOnOffByID : " + ex.getMessage(), ex);
        } finally {
            SQLConnFactory.safeClose(rs);
            SQLConnFactory.safeClose(cs);
            SQLConnFactory.safeClose(connection);
        }
        return ret;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;

import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.Reader;
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
public class ReaderModel {
    
    private static ReaderModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static Map<String, Reader> _mapReaderInfo = Collections.synchronizedMap(new LinkedHashMap<String, Reader>());

    public static ReaderModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new ReaderModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    private static String getReaderTableName() {
        String tableName;

        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`reader`";

        return tableName;
    }
    
    private int loadReader(Map<String, Reader> mapReader) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String readerTableName =  getReaderTableName();
            String queryStr;
            String deviceCode;
            Reader reader;
            
            // load reader list
            queryStr = String.format("SELECT `reader_id`, `reader_code`, `reader_name`, `position`, `door_mask` FROM %s", readerTableName);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            
            if (rs != null) {
                while (rs.next()) {
                    reader = new Reader();
                    deviceCode = rs.getString("reader_code");
                    
                    reader.setReader_id(rs.getLong("reader_id"));
                    reader.setReader_code(deviceCode);
                    reader.setReader_name(rs.getString("reader_name"));
                    reader.setDoor_mask(rs.getLong("door_mask"));
                    reader.setPosition(rs.getInt("position"));
                    
                    mapReader.put(deviceCode, reader);
                }
                
                ret = 0;
            }
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadReader: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }
    
    public int loadReader() {
        return loadReader(_mapReaderInfo);
    }

    public int reloadReader() {
        int ret;
        
        Map<String, Reader> mapReader = Collections.synchronizedMap(new LinkedHashMap<String, Reader>());
        ret = loadReader(mapReader);
        _mapReaderInfo = mapReader;
        return ret;
    }
    
    public Reader getReader(String readerCode) {
        
        Reader reader = _mapReaderInfo.get(readerCode);
        return reader;
    }
}

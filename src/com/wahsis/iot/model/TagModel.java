/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.model;


import com.wahsis.iot.common.CommonFunction;
import com.wahsis.iot.common.DefinedName;
import com.wahsis.iot.data.Tag;
import com.wahsis.iot.database.MySqlFactory;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class TagModel {

    private static TagModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    private static  Map<String, Tag> _mapTag = Collections.synchronizedMap(new LinkedHashMap<String, Tag>());
    
    public static TagModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new TagModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    private static String getTagTableName() {
        String tableName;

        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`tag`";

        return tableName;
    }
    
    private static String getEmployeeTableName() {
        String tableName;

        tableName = "`" + DefinedName.MASTER_DB_NAME + "`." + "`employee`";

        return tableName;
    }
    
    public int loadTag() {
        return loadTag(_mapTag);
    }
    
     public int reloadTag() {
        int ret;
        Map<String, Tag> mapTag = Collections.synchronizedMap(new LinkedHashMap<String, Tag>());
        ret = loadTag(mapTag);
        if (ret == 0)
            _mapTag = mapTag;
        
        return ret;
    }

   public int loadTag(Map<String, Tag> mapTag) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String tagTableName =  getTagTableName();
            String employeeTableName =  getEmployeeTableName();
            String tagCode;
            Tag tag;
            
            queryStr = String.format("SELECT `tag_id`, `tag_code`,`door_mask`, `tag_type`, `tag_status` , `create_date`, `start_date`, `end_date`, `map_date` ,`door_mask`, `employee_id` FROM %s", tagTableName);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            
            if (rs != null) {
                while (rs.next()) {
                    tag = new Tag();
                    tagCode = rs.getString("tag_code");
                    tag.setTag_id(rs.getLong("tag_id"));
                    tag.setTag_code(tagCode);
                    tag.setDoor_mask(rs.getLong("door_mask"));
                    tag.setTag_type(rs.getInt("tag_type"));
                    tag.setTag_status(rs.getInt("tag_status"));
                    tag.setEmployee_id(rs.getLong("employee_id"));

                    String createDate = CommonFunction.formatDateTimeFromString(rs.getString("create_date"), "dd/MM/YYYY");
                    String startDate = CommonFunction.formatDateTimeFromString(rs.getString("start_date"), "dd/MM/YYYY");
                    String endDate = CommonFunction.formatDateTimeFromString(rs.getString("end_date"), "dd/MM/YYYY");
                    String mapDate = CommonFunction.formatDateTimeFromString(rs.getString("map_date"), "dd/MM/YYYY");
                    tag.setCreate_date(createDate);
                    tag.setStart_date(startDate);
                    tag.setEnd_date(endDate);
                    tag.setMap_date(mapDate);
                    mapTag.put(tagCode, tag);
                }
                
                ret = 0;
            }
            
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".loadTag: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }

    public int getListTagByEmployee(List<Tag> listTag, long employeeId) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            String queryStr;
            String tableTagName = getTagTableName();

            queryStr = String.format("SELECT * FROM %s WHERE employee_id = %d ", tableTagName, employeeId);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            Tag item;
            if (rs != null) {
                while (rs.next()) {
                    item = new Tag();
                    item.setTag_code(rs.getString("tag_code"));
                    item.setTag_id(rs.getInt("tag_id"));
                    item.setDoor_mask(rs.getLong("door_mask"));
                    item.setTag_type(rs.getInt("tag_type"));
                    item.setTag_status(rs.getInt("tag_status"));

                    String createDate = CommonFunction.formatDateTimeFromString(rs.getString("create_date"), "dd/MM/YYYY");
                    String startDate = CommonFunction.formatDateTimeFromString(rs.getString("start_date"), "dd/MM/YYYY");
                    String endDate = CommonFunction.formatDateTimeFromString(rs.getString("end_date"), "dd/MM/YYYY");
                    String mapDate = CommonFunction.formatDateTimeFromString(rs.getString("map_date"), "dd/MM/YYYY");
                    item.setCreate_date(createDate);
                    item.setStart_date(startDate);
                    item.setEnd_date(endDate);
                    item.setMap_date(mapDate);

                    listTag.add(item);
                }
                ret = 0;
            }

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".getListTagByEmployee: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }

    public int getTagByEmployee(List<Tag> tag, long employeeId) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        int ret = -1;

        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            String queryStr;
            String tableTagName = getTagTableName();

            queryStr = String.format("SELECT * FROM %s WHERE `employee_id` = %d", tableTagName, employeeId);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            Tag item;
            if (rs != null) {
                while (rs.next()) {
                    item = new Tag();
                    item.setTag_code(rs.getString("tag_code"));
                    item.setTag_id(rs.getLong("tag_id"));
                    item.setDoor_mask(rs.getLong("door_mask"));
                    item.setTag_type(rs.getInt("tag_type"));
                    item.setTag_status(rs.getInt("tag_status"));
                    item.setEmployee_id(rs.getLong("employee_id"));

                    String createDate = CommonFunction.formatDateTimeFromString(rs.getString("create_date"), "dd/MM/YYYY");
                    String startDate = CommonFunction.formatDateTimeFromString(rs.getString("start_date"), "dd/MM/YYYY");
                    String endDate = CommonFunction.formatDateTimeFromString(rs.getString("end_date"), "dd/MM/YYYY");
                    String mapDate = CommonFunction.formatDateTimeFromString(rs.getString("map_date"), "dd/MM/YYYY");
                    item.setCreate_date(createDate);
                    item.setStart_date(startDate);
                    item.setEnd_date(endDate);
                    item.setMap_date(mapDate);

                    tag.add(item);

                }
                ret = 0;
            }

        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".getTagByEmployee: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }



    public int unMapTag(Tag tag) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs;
        int ret = -1;

        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String tableTagName = getTagTableName();

            // Kiểm tra nhân viên đã được gán loại thẻ này chưa?
            queryStr = String.format("SELECT `tag_id` FROM %s WHERE `employee_id` = %d ", tableTagName, tag.getEmployee_id());
            stmt.execute(queryStr);
            rs = stmt.getResultSet();

            if (rs == null || !rs.next()) {
                ret = 5;
            } else {
                queryStr = String.format("UPDATE %1$s SET `employee_id` = 0, `tag_status` = 0, `map_date` = NULL WHERE `tag_code` = '%2$s'",
                        tableTagName, tag.getTag_code());
                stmt.execute(queryStr);

                ret = 0;
            }
        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".update: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int delete(JsonArray arrIDDel) {
        Connection connection = null;
        Statement stmt = null;
        int ret = -1;

        try {
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String tableTagName = getTagTableName();

            String queryStr = String.format("DELETE FROM %s WHERE tag_id IN (%s)", tableTagName, getWhereClauseDelete(arrIDDel));
            stmt.execute(queryStr);
            ret = 0;
        } catch (SQLException ex) {
            logger.error(getClass().getSimpleName() + ".delete: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }

        return ret;
    }
    
    public Boolean checkExist(String tagCode) {
        
        Tag tag = _mapTag.get(tagCode);
        if (tag != null)
            return true;
        
        return false;
    } 
    
    public Tag getTag(String tagCode) {
        
        Tag tag = _mapTag.get(tagCode);
        return tag;
    }
    
     public Boolean checkValidTagCode(String tagCode){
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean ret = false;
        
        try {            
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            String queryStr;
            String tagTableName =  getTagTableName();
            //Tag tag;
            
            queryStr = String.format("SELECT * FROM %s WHERE now() > start_date AND end_date > now() and tag_status = 1 and tag_code = '%s'", tagTableName, tagCode);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            Tag item;
             if (rs != null) {
                  while (rs.next()) {
                    ret = true;
                  }
            }
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".checkValidTagCode: " + ex.getMessage(), ex);
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        
        return ret;
    }

    public String getWhereClauseSearch(String tagCode) {
        StringBuilder result = new StringBuilder();
        String and = "WHERE";
        if (tagCode != null && !tagCode.isEmpty()) {
            result.append(String.format("%s (tag_code LIKE '%%%s%%')", and, tagCode));
        }

        return result.toString();
    }

    public String getWhereClauseDelete(JsonArray a) {
        StringBuilder result = new StringBuilder();
        String s = ",";
        for (int i = 0; i < a.size(); i++) {
            if (i < (a.size() - 1)) {
                result.append(String.format("%d %s", a.get(i).getAsInt(), s));
            } else {
                result.append(a.get(i).getAsInt());
            }
        }
        return result.toString();
    }
}

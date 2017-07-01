/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.data;

/**
 *
 * @author diepth
 */
public class OpenDoorLog {
    
    private long log_id;
    private String tag_code;
    private String employee_account = "";
    private String door_name = "";
    private int check_type = 0;
    private String open_date_time;
    private long employee_id;

    /**
     * @return the log_id
     */
    public long getLog_id() {
        return log_id;
    }

    /**
     * @param log_id the log_id to set
     */
    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    /**
     * @return the tag_code
     */
    public String getTag_code() {
        return tag_code;
    }

    /**
     * @param tag_code the tag_code to set
     */
    public void setTag_code(String tag_code) {
        this.tag_code = tag_code;
    }

    /**
     * @return the employee_account
     */
    public String getEmployee_account() {
        return employee_account;
    }

    /**
     * @param employee_account the employee_account to set
     */
    public void setEmployee_account(String employee_account) {
        this.employee_account = employee_account;
    }

    /**
     * @return the door_name
     */
    public String getDoor_name() {
        return door_name;
    }

    /**
     * @param door_name the door_name to set
     */
    public void setDoor_name(String door_name) {
        this.door_name = door_name;
    }

    /**
     * @return the check_type
     */
    public int getCheck_type() {
        return check_type;
    }

    /**
     * @param check_type the check_type to set
     */
    public void setCheck_type(int check_type) {
        this.check_type = check_type;
    }

    /**
     * @return the open_date_time
     */
    public String getOpen_date_time() {
        return open_date_time;
    }

    /**
     * @param open_date_time the open_date_time to set
     */
    public void setOpen_date_time(String open_date_time) {
        this.open_date_time = open_date_time;
    }

    /**
     * @return the employee_id
     */
    public long getEmployee_id() {
        return employee_id;
    }

    /**
     * @param employee_id the employee_id to set
     */
    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }
}

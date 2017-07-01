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
public class EmployeeTag {
    
    private long tag_id;
    private String tag_code;
    private String employee_code = "";
    private String employee_account = "";
    private String employee_name = "";
    private String avatar_url = "";
    private long door_mask = 0;

    /**
     * @return the tag_id
     */
    public long getTag_id() {
        return tag_id;
    }

    /**
     * @param tag_id the tag_id to set
     */
    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
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
     * @return the employee_code
     */
    public String getEmployee_code() {
        return employee_code;
    }

    /**
     * @param employee_code the employee_code to set
     */
    public void setEmployee_code(String employee_code) {
        this.employee_code = employee_code;
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
     * @return the employee_name
     */
    public String getEmployee_name() {
        return employee_name;
    }

    /**
     * @param employee_name the employee_name to set
     */
    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    /**
     * @return the avatar_url
     */
    public String getAvatar_url() {
        return avatar_url;
    }

    /**
     * @param avatar_url the avatar_url to set
     */
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    /**
     * @return the door_mask
     */
    public long getDoor_mask() {
        return door_mask;
    }

    /**
     * @param door_mask the door_mask to set
     */
    public void setDoor_mask(long door_mask) {
        this.door_mask = door_mask;
    }
}

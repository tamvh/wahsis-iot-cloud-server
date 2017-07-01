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

public class Tag {
    
    private long tag_id;
    private String tag_code;
    private Long door_mask;
    private int tag_type;
    private int tag_status;
    private String create_date;
    private String map_date;
    private String start_date;
    private String end_date;
    private long employee_id;
    private String employee_account;

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
     * @return the door_mask
     */
    public Long getDoor_mask() {
        return door_mask;
    }
    
    /**
     * @param door_mask the door_mask to set
     */
    public void setDoor_mask(long door_mask) {
        this.door_mask = door_mask;
    }

    
    public int getTag_type() {
        return tag_type;
    }
    public void setTag_type(int tag_type) {
        this.tag_type = tag_type;
    }
    
    public int getTag_status() {
        return tag_status;
    }
    public void setTag_status(int tag_status) {
        this.tag_status = tag_status;
    }

    public String getCreate_date() {
        return create_date;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
    
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    
    public long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }
    
    public String getEmployee_account() {
        return employee_account;
    }

    public void setEmployee_account(String employee_account) {
        this.employee_account = employee_account;
    }
    
    public String getMap_date() {
        return map_date;
    }

    public void setMap_date(String map_date) {
        this.map_date = map_date;
    }
    
}


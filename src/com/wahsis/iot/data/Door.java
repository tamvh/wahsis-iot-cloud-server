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
public class Door {
    
    private long door_id;
    private String door_code;
    private String door_name = "";
    private long door_value;
    private String description = "";

    /**
     * @return the door_id
     */
    public long getDoor_id() {
        return door_id;
    }

    /**
     * @param door_id the door_id to set
     */
    public void setDoor_id(long door_id) {
        this.door_id = door_id;
    }

    /**
     * @return the door_code
     */
    public String getDoor_code() {
        return door_code;
    }

    /**
     * @param door_code the door_code to set
     */
    public void setDoor_code(String door_code) {
        this.door_code = door_code;
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
     * @return the door_value
     */
    public Long getDoor_value() {
        return door_value;
    }

    /**
     * @param door_value the door_value to set
     */
    public void setDoor_value(long door_value) {
        this.door_value = door_value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

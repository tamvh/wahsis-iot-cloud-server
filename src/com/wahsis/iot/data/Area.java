/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.data;

/**
 *
 * @author sinhnd
 */
public class Area {
    
    private long area_id;
    private String area_name = "";
    private String description = "";
    private int status;
    private String area_code = "";
    private int area_brightness;

    public int getArea_brightness() {
        return area_brightness;
    }

    public void setArea_brightness(int area_brightness) {
        this.area_brightness = area_brightness;
    }
    
    
    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }
     
    
    /**
     * @return the area_id
     */
    public long getArea_id() {
        return area_id;
    }

    /**
     * @param area_id the area_id to set
     */
    public void setArea_id(long area_id) {
        this.area_id = area_id;
    }

    /**
     * @return the area_name
     */
    public String getArea_name() {
        return area_name;
    }

    /**
     * @param area_name the area_name to set
     */
    public void setArea_name(String area_name) {
        this.area_name = area_name;
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

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
}

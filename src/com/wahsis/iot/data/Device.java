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
public class Device {
    
    private long device_id;
    private String device_code;
    private String device_name;
    private int device_type;
    private String rel_device_code;

    /**
     * @return the device_id
     */
    public long getDevice_id() {
        return device_id;
    }

    /**
     * @param device_id the device_id to set
     */
    public void setDevice_id(long device_id) {
        this.device_id = device_id;
    }

    /**
     * @return the device_code
     */
    public String getDevice_code() {
        return device_code;
    }

    /**
     * @param device_code the device_code to set
     */
    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    /**
     * @return the device_name
     */
    public String getDevice_name() {
        return device_name;
    }

    /**
     * @param device_name the device_name to set
     */
    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    /**
     * @return the device_type
     */
    public int getDevice_type() {
        return device_type;
    }

    /**
     * @param device_type the device_type to set
     */
    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }

    /**
     * @return the rel_device_code
     */
    public String getRel_device_code() {
        return rel_device_code;
    }

    /**
     * @param rel_device_code the rel_device_code to set
     */
    public void setRel_device_code(String rel_device_code) {
        this.rel_device_code = rel_device_code;
    }
}

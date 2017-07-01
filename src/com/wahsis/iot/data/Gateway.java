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
public class Gateway {
    
    private long gateway_id;
    private String gateway_code;
    private String gateway_name = "";
    private String description = "";

    /**
     * @return the gateway_id
     */
    public long getGateway_id() {
        return gateway_id;
    }

    /**
     * @param gateway_id the gateway_id to set
     */
    public void setGateway_id(long gateway_id) {
        this.gateway_id = gateway_id;
    }

    /**
     * @return the gateway_code
     */
    public String getGateway_code() {
        return gateway_code;
    }

    /**
     * @param gateway_code the gateway_code to set
     */
    public void setGateway_code(String gateway_code) {
        this.gateway_code = gateway_code;
    }

    /**
     * @return the gateway_name
     */
    public String getGateway_name() {
        return gateway_name;
    }

    /**
     * @param gateway_name the gateway_name to set
     */
    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
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

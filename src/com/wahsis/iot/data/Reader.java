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
public class Reader {
    
    private long reader_id;
    private String reader_code;
    private String reader_name = "";
    private long door_mask;
    private String description = "";
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the reader_id
     */
    public long getReader_id() {
        return reader_id;
    }

    /**
     * @param reader_id the reader_id to set
     */
    public void setReader_id(long reader_id) {
        this.reader_id = reader_id;
    }

    /**
     * @return the reader_code
     */
    public String getReader_code() {
        return reader_code;
    }

    /**
     * @param reader_code the reader_code to set
     */
    public void setReader_code(String reader_code) {
        this.reader_code = reader_code;
    }

    /**
     * @return the reader_name
     */
    public String getReader_name() {
        return reader_name;
    }

    /**
     * @param reader_name the reader_name to set
     */
    public void setReader_name(String reader_name) {
        this.reader_name = reader_name;
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

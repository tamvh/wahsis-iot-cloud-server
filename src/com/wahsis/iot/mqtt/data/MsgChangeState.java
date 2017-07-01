/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt.data;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author haint3
 */
public class MsgChangeState {
    @SerializedName("device_id")
    private String  _deviceId = "";
    
    @SerializedName("new_state")
    private int     _newState = 0;
    
    public MsgChangeState(){}
    
    public MsgChangeState(String deviceId, int newState) {
        _deviceId = deviceId;
        _newState = newState;
    }

    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(String _deviceId) {
        this._deviceId = _deviceId;
    }

    public int getNewState() {
        return _newState;
    }

    public void setNewState(int _newState) {
        this._newState = _newState;
    }
}

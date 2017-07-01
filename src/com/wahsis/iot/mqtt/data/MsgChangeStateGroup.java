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
public class MsgChangeStateGroup {
    @SerializedName("group_id")
    private int  _group_Id = 0;
    
    @SerializedName("new_state")
    private int     _newState = 0;
    
    public MsgChangeStateGroup(){}
    
    public MsgChangeStateGroup(int groupId, int newState) {
        _group_Id = groupId;
        _newState = newState;
    }

    public int getDeviceId() {
        return _group_Id;
    }

    public void setDeviceId(int _group_Id) {
        this._group_Id = _group_Id;
    }

    public int getNewState() {
        return _newState;
    }

    public void setNewState(int _newState) {
        this._newState = _newState;
    }
}

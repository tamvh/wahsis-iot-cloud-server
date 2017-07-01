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
public class IMqttMessage <T extends Object> {
    
    @SerializedName("msg_id")
    private int      _msgId = 0;
    
    @SerializedName("msg")
    private String    _msg = "";
    
    @SerializedName("err")
    private int       _errCode = 0;
    
    @SerializedName("dt")
    private T         _msgData = null;
    
    public IMqttMessage(){}
    
    public IMqttMessage(int errCode, String msg, T msgData) {
        
        _msg = msg;
        _errCode = errCode;
        _msgData = msgData;
    }
    
    public IMqttMessage(int errCode, String msg, T msgData, int msgId) {
        _msgId = msgId;
        _msg = msg;
        _errCode = errCode;
        _msgData = msgData;
    }

    public int getMsgId() {
        return _msgId;
    }

    public void setMsgId(int _msgId) {
        this._msgId = _msgId;
    }

    public String getMsg() {
        return _msg;
    }

    public void setMsg(String _msg) {
        this._msg = _msg;
    }

    public int getErrCode() {
        return _errCode;
    }

    public void setErrCode(int _errCode) {
        this._errCode = _errCode;
    }

    public T getMsgData() {
        return _msgData;
    }

    public void setMsgData(T _msgData) {
        this._msgData = _msgData;
    }
    
}

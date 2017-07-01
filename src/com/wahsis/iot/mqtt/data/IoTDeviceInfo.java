/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.mqtt.data;

/**
 *
 * @author haint3
 */
public class IoTDeviceInfo {
    private String  _deviceId = "";
    private String  _gatewayId = "";
    private String  _relativeId = "";
    private String  _appKey = "";
    private String  _name = "";
    private int     _type = 0;
    private int     _state = 0;
    private boolean _active = false;
    
    public IoTDeviceInfo() {}
    public IoTDeviceInfo(String gatewayId, String deviceId, String relativeId, String appKey, int type, int state, boolean active) {
        _gatewayId = gatewayId;
        _deviceId = deviceId;
        _relativeId = relativeId;
        _appKey = appKey;
        _type = type;
        _state = state;
        _active = active;
    }

    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(String _deviceId) {
        this._deviceId = _deviceId;
    }

    public String getGatewayId() {
        return _gatewayId;
    }

    public void setGatewayId(String _gatewayId) {
        this._gatewayId = _gatewayId;
    }

    public String getAppKey() {
        return _appKey;
    }

    public void setAppKey(String _appKey) {
        this._appKey = _appKey;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public int getState() {
        return _state;
    }

    public void setState(int _state) {
        this._state = _state;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean _active) {
        this._active = _active;
    }

    public String getRelativeId() {
        return _relativeId;
    }

    public void setRelativeId(String _relativeId) {
        this._relativeId = _relativeId;
    }

    

    
}

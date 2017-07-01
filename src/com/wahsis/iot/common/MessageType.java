/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wahsis.iot.common;

/**
 *  
 * @author thanhnn3
 */
public class MessageType {
    
    public static final int MSG_REQUEST = 0; // message type request from client follow range 0-1000
    public static final int MSG_RESPONSE = 1000; // message type response from server follow range >1000
    
    //define message type of request
    public static final int  MSG_PING = MSG_REQUEST + 1;
    //public static final int  REQ_GET_DATA = MSG_REQUEST + 2;
    //public static final int  REQ_GET_DATA_1 = MSG_REQUEST + 3;
    
    //define message type of response
    public static final int  MSG_PONG = MSG_RESPONSE + 1;
    
    public static final int REQ_GET_DATA= MSG_REQUEST + 2; //mess type for function get data in test_ws controller
    public static final int REQ_GET_DATA_1= MSG_REQUEST + 3;//mess type for function get data in test_ws_1 controller
    public static final int MSG_TYPE_GET_DATA_4 = MSG_REQUEST + 4;//mess type for function get data in test_ws_1 controller
    public static final int MSG_TYPE_GET_DATA_5 = MSG_REQUEST + 5;//mess type for function get data in test_ws_1 controller
    public static final int MSG_RELOAD_OPEN_DOOR_LOG_PAGE = MSG_REQUEST + 6;//request reload open door log page on client
    public static final int MSG_RELOAD_DIM_GROUP = MSG_REQUEST + 7;//request reload brightness for dim_group on client
    public static final int MSG_SWITCH_LIGHT_GROUP = MSG_REQUEST + 8;//request reload light for group on client
    
   
}
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
public class CardInfo {
    private String _cardId = "";
    private String _fullname = "";
    
    public CardInfo(String cardId, String fullname) {
        _cardId = cardId;
        _fullname = fullname;
    }

    public String getCardId() {
        return _cardId;
    }

    public void setCardId(String _cardId) {
        this._cardId = _cardId;
    }

    public String getFullname() {
        return _fullname;
    }

    public void setFullname(String _fullname) {
        this._fullname = _fullname;
    }
}

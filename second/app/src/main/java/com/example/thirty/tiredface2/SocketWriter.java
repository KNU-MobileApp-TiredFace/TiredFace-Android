package com.example.thirty.tiredface2;

import com.example.thirty.tiredface2.JsonDataSender.JsonDataSender;

import org.json.JSONObject;

import java.net.Socket;
/*

public class SocketWriter implements JsonDataSender {
    Socket sendSocket;
    String defaultEvent;

    public SocketWriter(Socket sendSocket, String defaultEvent){
        this.sendSocket = sendSocket;
        this.defaultEvent = defaultEvent;
        if(this.defaultEvent == null)
            this.defaultEvent = "message";
    }
    @Override
    public void sendData(JSONObject jsonObject){
        sendSocket.emit("message",jsonObject);
    }
}
*/
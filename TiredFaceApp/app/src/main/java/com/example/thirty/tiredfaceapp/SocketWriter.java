package com.example.thirty.tiredfaceapp;

import com.example.thirty.tiredfaceapp.JsonDataSender.JsonDataSender;

import org.json.JSONObject;

import io.socket.client.Socket;

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

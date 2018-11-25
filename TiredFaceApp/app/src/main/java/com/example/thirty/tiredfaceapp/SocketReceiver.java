package com.example.thirty.tiredfaceapp;

import android.util.Log;

import com.example.thirty.tiredfaceapp.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredfaceapp.JsonObjectEventObserver.JsonObjectEventSubject;

import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketReceiver implements JsonObjectEventSubject {
    Socket receiveSocket = null;
    ArrayList<JsonObjectEventObserver> observers;

    public SocketReceiver(Socket socket, String[] receivingEvents){
        observers = new ArrayList<JsonObjectEventObserver>();
        this.receiveSocket = socket;

        if(receivingEvents != null) {
            for (String event : receivingEvents) {
                socket.on(event, onMessageReceived);
            }
        }
        else
            socket.on("message",onMessageReceived);
    }
    private Emitter.Listener onMessageReceived = new Emitter.Listener(){
        @Override
        public void call(Object... args) {
            notifyObserver((JSONObject) args[0]);
        }
    };

    public void registerObserver(JsonObjectEventObserver observer)  {
        observers.add(observer);
    }

    public void removeObserver(JsonObjectEventObserver observber){
        observers.remove(observber);
    }

    public void notifyObserver(JSONObject jsonObject){
        for(JsonObjectEventObserver observer : observers)
            observer.update(jsonObject);
    }
}

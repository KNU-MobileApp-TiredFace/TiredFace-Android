package com.example.thirty.tiredface.JsonObjectEventObserver;

import org.json.JSONObject;

//Obeserver 패턴
//Json 이벤트 수신 Subject
public interface JsonObjectEventSubject {
    public void registerObserver(JsonObjectEventObserver observer);
    public void removeObserver(JsonObjectEventObserver observber);
    public void notifyObserver(JSONObject jsonObject);
}

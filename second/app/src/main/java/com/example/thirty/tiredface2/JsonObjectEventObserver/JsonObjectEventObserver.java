package com.example.thirty.tiredface2.JsonObjectEventObserver;

import org.json.JSONObject;

//Observer 패턴
//Json 데이터 수신 이벤트 관찰자
public interface JsonObjectEventObserver {
    public void update(JSONObject jsonObject);
}

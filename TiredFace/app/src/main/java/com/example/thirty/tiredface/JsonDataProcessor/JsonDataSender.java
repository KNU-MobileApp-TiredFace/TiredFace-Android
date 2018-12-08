package com.example.thirty.tiredface.JsonDataProcessor;

import org.json.JSONObject;

//Json으로 데이터를 전송하는 기능에 대한 인터페이스
public interface JsonDataSender {
    public void sendData(JSONObject jsonObject);
    public void sendData(String jsonStr);
}

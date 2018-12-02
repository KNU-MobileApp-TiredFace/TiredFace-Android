package com.example.thirty.tiredface2.JsonSerializable;

import org.json.JSONObject;

//Json으로 변형하는 기능에 대한 인터페이스
public interface ToJsonSerializable {
    public JSONObject toJson();
}

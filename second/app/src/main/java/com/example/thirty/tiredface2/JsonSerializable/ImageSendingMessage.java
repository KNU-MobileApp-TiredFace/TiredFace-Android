package com.example.thirty.tiredface2.JsonSerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//서버에게 보내는 이미지 메시지
public class ImageSendingMessage implements ToJsonSerializable {
    private int msgId;
    private String encodedImage;
    private String encodingType;

    public ImageSendingMessage(int msgId, String encodedImage, String encodingType){
        this.msgId = msgId;
        this.encodedImage = encodedImage;
        this.encodingType = encodingType;
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("msgId",msgId);
            jsonObject.put("encodingType",encodingType);
            //jsonObject.put("imageArr",putImage());

            jsonObject.put("image",encodedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public String toJsonString(){
        String jsonStr = "{";
        jsonStr += "\"msgId\":\"" + msgId + "\",";
        jsonStr += "\"encodedType\":\"" + encodingType + "\",";
        jsonStr += "\"image\":\"" + encodedImage + "\"";
        jsonStr += "}";
        return jsonStr;
    }

    public ArrayList<JSONObject> toJsonArr(){
        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        int maxSize = 3000;
        for(int i = 0; i < encodedImage.length(); i+= maxSize ) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msgId", msgId);
                jsonObject.put("encodingType", encodingType);
                //jsonObject.put("imageArr",putImage());
                //String testerStr = "";
                //jsonObject.put("tester", testerStr);

                jsonObject.put("image", encodedImage.substring(i,min(encodedImage.length(),i+maxSize)));
                list.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private int min(int a, int b){
        return (a < b) ? a : b;
    }

    private JSONArray putImage(){
        JSONArray arr = new JSONArray();
        int size = encodedImage.length();
        for(int i = 0 ;i < size ; i++) {
            try {
                arr.put(i,encodedImage.charAt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return arr;
    }
}

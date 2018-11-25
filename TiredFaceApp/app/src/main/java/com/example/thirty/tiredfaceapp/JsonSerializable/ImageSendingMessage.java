package com.example.thirty.tiredfaceapp.JsonSerializable;

import org.json.JSONException;
import org.json.JSONObject;

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
            jsonObject.put("image",encodedImage);
            jsonObject.put("encodingType",encodingType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

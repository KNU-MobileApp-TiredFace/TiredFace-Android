package com.example.thirty.tiredface2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.thirty.tiredface2.JsonDataSender.JsonDataSender;
import com.example.thirty.tiredface2.JsonSerializable.ImageSendingMessage;
import com.example.thirty.tiredface2.JsonSerializable.ToJsonSerializable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//이미지를 받아서 전송하고 서버 응답을 받는 클래스
public class ImageProcess {
    public String processImage(String image){
        ToJsonSerializable jsonSerializable = new ImageSendingMessage(1, image, "base64");
        String jsonStr = ((ImageSendingMessage) jsonSerializable).toJsonString();

        Log.i("DevelopLog","obj : " + jsonStr.toString());
        Log.i("DevelopLog",  "length : " + jsonStr.length());

        /**************************************서버에 데이터 전송***************************************/
        //소켓 연결 및 레퍼런스 전달
        TCPSocketCreator socketCreator = new TCPSocketCreator(Settings.SERVER_IP, Settings.SERVER_PORT);
        Socket mSocket = socketCreator.createSocket();

        //데이터 전송자로 TCPDataWriter 할당
        JsonDataSender jsonDataSender = new TCPDataWriter(mSocket);
        jsonDataSender.sendData(jsonStr);
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        /***************************************서버로부터 답변 수신**********************************/
        socketCreator = new TCPSocketCreator(Settings.SERVER_IP, Settings.SERVER_PORT);
        Socket receiverSocket = socketCreator.createSocket();

        TCPJsonSocketReceiver receiver = null;
        JSONObject receivedJson = null;
        try {
            receiver = new TCPJsonSocketReceiver(receiverSocket);
            receivedJson = receiver.waitForAnswer();
            receiverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////

        //Json 데이터에서 이미지를 얻어옴
        //이 부분은 Json 형식이 정해지면 클래스화하도록 수정할 것
        String receivedImage = null;
        try {
            receivedImage = receivedJson.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //테스트를 위해 받은 이미지를 저장
        new ImageToGallery().stringImageToGallery(receivedImage);
        //new ImageToGallery().stringImageToGallery(image);

        return receivedImage;
    }


}

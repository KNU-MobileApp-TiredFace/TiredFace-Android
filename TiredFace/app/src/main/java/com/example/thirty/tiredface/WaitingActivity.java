package com.example.thirty.tiredface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.example.thirty.tiredface.ImageBitmapUriConvert.ImageToGallery;
import com.example.thirty.tiredface.JsonDataProcessor.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredface.JsonDataProcessor.TCPSocket.TCPJsonSocketReceiver;
import com.example.thirty.tiredface.JsonDataProcessor.TCPSocket.TCPSocketCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

public class WaitingActivity extends AppCompatActivity implements JsonObjectEventObserver {
    Socket receiverSocket = null;
    boolean finishedJob = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        /***************************************서버로부터 답변 수신**********************************/
        TCPSocketCreator socketCreator = new TCPSocketCreator(Settings.SERVER_IP, Settings.SERVER_PORT);
        socketCreator = new TCPSocketCreator(Settings.SERVER_IP, Settings.SERVER_RESPONSE_PORT);
        receiverSocket = socketCreator.createSocket();

        TCPJsonSocketReceiver receiver = null;
        try {
            receiver = new TCPJsonSocketReceiver(receiverSocket);
            receiver.registerObserver(this);
            receiver.waitForAnswer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onRestart() {
        Log.i("DevelopLog","WaitingActivity Restarted");
        super.onRestart();
        finish();
    }

    private void toResultPage(String imageString) {
        Intent intent = new Intent(WaitingActivity.this,ResultActivity.class);
        intent.putExtra("image",imageString);
        startActivity(intent);
        //finish();
    }

    @Override
    public void update(JSONObject jsonObject) {
        /***************************테스트용 코드*************************/
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////

        try {
            receiverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Json 데이터에서 이미지를 얻어옴
        String receivedImage = null;
        try {
            receivedImage = jsonObject.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //테스트를 위해 받은 이미지를 저장
        new ImageToGallery().stringImageToGallery(receivedImage);
        //new ImageToGallery().stringImageToGallery(image);
        finishedJob = true;
        toResultPage(receivedImage);
    }
}

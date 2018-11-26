package com.example.thirty.tiredface2;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.thirty.tiredface2.JsonDataSender.JsonDataSender;
import com.example.thirty.tiredface2.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredface2.JsonSerializable.ImageSendingMessage;
import com.example.thirty.tiredface2.JsonSerializable.ToJsonSerializable;
import com.example.thirty.tiredface2.UriToByteArrString.ImageUriToBase64ByteArrString;
import com.example.thirty.tiredface2.UriToByteArrString.ImageUriToBase64ByteArrString;
import com.example.thirty.tiredface2.UriToByteArrString.UriToByteArrString;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.Permission;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements JsonObjectEventObserver {
    private JsonDataSender jsonDataSender;
    private TCPJsonSocketReceiver receiver;
    private UriToByteArrString uriToByteArrString;
    private final static int PICK_IMAGE = 1;
    private String encodedImage = null;

    public static String serverURL = "192.168.0.74";
    public static int serverPort = 6000;

    //public static final String serverURL ="tcp://175.113.48.102:34627";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        //소켓 연결 및 레퍼런스 전달
        TCPSocketCreator socketCreator = new TCPSocketCreator(serverURL, serverPort);
        Socket mSocket = socketCreator.createSocket();

        jsonDataSender = new TCPDataWriter(mSocket);

        try {
            receiver = new TCPJsonSocketReceiver(mSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SocketReceiver socketReceiver = new SocketReceiver(mSocket,null);
        //socketReceiver.registerObserver((JsonObjectEventObserver) this);

        uriToByteArrString = new ImageUriToBase64ByteArrString(getContentResolver());
    }

    @Override
    public void update(JSONObject jsonObject) {
        Log.i("DevelopLog", "message received : " + jsonObject);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {

            Uri uri = data.getData();
            encodedImage = uriToByteArrString.convert(uri);

            Log.i("DevelopLog", "encodedImage : " + encodedImage);
        }
    }

    public void selectImageButtonClicked(View v) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    public void sendButtonClicked(View v) {
        ToJsonSerializable jsonSerializable = new ImageSendingMessage(1, encodedImage, "base64");
        ArrayList<JSONObject> objList = ((ImageSendingMessage) jsonSerializable).toJsonArr();
        JSONObject obj = jsonSerializable.toJson();

        //Log.i("DevelopLog","obj : " + obj.toString());
        //jsonDataSender.sendData(obj);


        for(int i = 0 ;  i < objList.size(); i++) {
            //Log.i("DevelopLog", "obj list: " + objList.get(i).toString());
            jsonDataSender.sendData(objList.get(i));
        }

    }
}

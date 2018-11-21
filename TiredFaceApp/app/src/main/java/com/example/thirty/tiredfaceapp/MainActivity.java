package com.example.thirty.tiredfaceapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.thirty.tiredfaceapp.JsonDataSender.JsonDataSender;
import com.example.thirty.tiredfaceapp.JsonSerializable.ImageSendingMessage;
import com.example.thirty.tiredfaceapp.JsonSerializable.ToJsonSerializable;
import com.example.thirty.tiredfaceapp.UriToByteArrString.ImageUriToBase64ByteArrString;
import com.example.thirty.tiredfaceapp.UriToByteArrString.UriToByteArrString;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private JsonDataSender jsonDataSender;
    private UriToByteArrString uriToByteArrString;
    private final static int PICK_IMAGE = 1;
    private String encodedImage = null;

    public static final String serverURL ="http://192.168.0.74:1337";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //소켓 연결 및 레퍼런스 전달
        Socket mSocket = null;
        try{
            mSocket = IO.socket(serverURL);
        }catch(URISyntaxException e){
            e.printStackTrace();
            return;
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.connect();
        jsonDataSender = new SocketWriter(mSocket, "message");

        uriToByteArrString = new ImageUriToBase64ByteArrString(getContentResolver());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {

            Uri uri = data.getData();
            encodedImage = uriToByteArrString.convert(uri);
        }
    }

    public void selectImageButtonClicked(View v){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    public void sendButtonClicked(View v){
        ToJsonSerializable jsonSerializable = new ImageSendingMessage(1,encodedImage,"base64");
        jsonDataSender.sendData(jsonSerializable.toJson());
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("thirtyLog","connected to the server!");
        }
    };
}

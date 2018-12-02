package com.example.thirty.tiredface2;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.thirty.tiredface2.JsonDataSender.JsonDataSender;
import com.example.thirty.tiredface2.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredface2.JsonSerializable.ImageSendingMessage;
import com.example.thirty.tiredface2.JsonSerializable.ToJsonSerializable;
import com.example.thirty.tiredface2.UriToByteArrString.ImageUriToBase64ByteArrString;
import com.example.thirty.tiredface2.UriToByteArrString.ImageUriToBase64ByteArrString;
import com.example.thirty.tiredface2.UriToByteArrString.UriToByteArrString;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements JsonObjectEventObserver {

    private final static int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private String encodedImage = null;


    //public static final String serverURL ="tcp://175.113.48.102:34627";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         /*******************************테스트 코드*******************************/
        try {
            TCPSocketCreator testCreator = new TCPSocketCreator(Settings.SERVER_IP, Settings.SERVER_PORT);
            Socket socket = testCreator.createSocket();
            TCPJsonSocketReceiver testReceiver = new TCPJsonSocketReceiver(socket);
            JSONObject answer = testReceiver.waitForAnswer();
            String testCoded = answer.getString("image");
            new ImageToGallery().stringImageToGallery(testCoded);
            byte [] encodeByte=Base64.decode(testCoded,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////////////////////////
    }


    @Override
    public void update(JSONObject jsonObject) {
        Log.i("DevelopLog", "message received : " + jsonObject);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //갤러리에서 이미지를 골라옴
        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            UriToByteArrString uriToByteArrString = new ImageUriToBase64ByteArrString(getContentResolver());
            encodedImage = uriToByteArrString.convert(uri);
            Log.i("DevelopLog", "encodedImage : " + encodedImage);
        }
        else if(requestCode == CAMERA_REQUEST){ //카메라로 사진을 찍어옴
            Bitmap picture = (Bitmap) data.getExtras().get("data");
            File file = new File("file:///sdcard/", "photo.jpg");
            Uri uri = Uri.fromFile(file);
            ImageUriToBase64ByteArrString imageUriToBase64ByteArrString = new ImageUriToBase64ByteArrString(getContentResolver());
            Bitmap photo = imageUriToBase64ByteArrString.convertToBitmap(uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
            encodedImage = new String(stream.toByteArray());
        }
    }

    //이미지 선택 버튼 클릭 이벤트 처리 메소드
    public void selectImageButtonClicked(View v) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    //Send 버튼 클릭 이벤트 처리 메소드
    public void sendButtonClicked(View v) throws JSONException {
        ImageProcess imageProcess = new ImageProcess();
        String resultImage = imageProcess.processImage(encodedImage);

        byte [] encodeByte=Base64.decode(resultImage,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
    }


    //작동 안됨
    //사진찍기 기능
    public void takePhoto(View v) throws IOException {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        photoFile = createImageFile();

        Uri photoURI = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider",
                photoFile);

        //Uri photoURI = Uri.fromFile(photoFile);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    //새로 찍은 사진을 위한 파일 생성
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

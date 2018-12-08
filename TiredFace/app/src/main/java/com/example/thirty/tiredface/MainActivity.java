package com.example.thirty.tiredface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.thirty.tiredface.JsonObjectEventObserver.JsonObjectEventObserver;
import com.example.thirty.tiredface.UriToByteArrString.ImageUriToBase64ByteArrString;
import com.example.thirty.tiredface.UriToByteArrString.UriToByteArrString;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


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
/*
        try {
            TCPSocketCreator testCreator = new TCPSocketCreator(Settings.SERVER_IP, Settings.SERVER_PORT);
            Socket socket = testCreator.createSocket();
            TCPJsonSocketReceiver testReceiver = new TCPJsonSocketReceiver(socket);
            JSONObject answer = testReceiver.waitForAnswer();
            String testCoded = answer.getString("image");
            new ImageToGallery().stringImageToGallery(testCoded);
            byte[] encodeByte = Base64.decode(testCoded, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
        } catch (IOException e) {
            Log.i("DevelopLog", "IOException");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.i("DevelopLog", "JSONException");
            e.printStackTrace();
        }
*/
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
        } else if (requestCode == CAMERA_REQUEST) { //카메라로 사진을 찍어옴
            Bitmap picture = (Bitmap) data.getParcelableExtra("data");
            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(picture);

            BitmapToByteStringBase64 converter = new BitmapToByteStringBase64();
            encodedImage = converter.convert(picture);
        }
    }

    //이미지 선택 버튼 클릭 이벤트 처리 메소드
    public void selectImageButtonClicked(View v) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    //Send 버튼 클릭 이벤트 처리 메소드
    public void sendButtonClicked(View v) throws JSONException {
        ImageProcess imageProcess = new ImageProcess();
        String resultImage = imageProcess.processImage(encodedImage);

        byte[] encodeByte = Base64.decode(resultImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
    }


    //사진찍기 기능
    public void takePhoto(View v) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }
}

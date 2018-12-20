package com.example.thirty.tiredface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.example.thirty.tiredface.ImageBitmapUriConvert.ImageToGallery;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        String image = getIntent().getStringExtra("image");
        new ImageToGallery().stringImageToGallery(image);
        byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(bitmap);
    }
}

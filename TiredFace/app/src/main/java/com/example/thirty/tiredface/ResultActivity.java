package com.example.thirty.tiredface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thirty.tiredface.ImageBitmapUriConvert.ImageToGallery;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(WaitingActivity.w_activity != null)
        {
            WaitingActivity activity = (WaitingActivity)WaitingActivity.w_activity;
            activity.finish();
        }


        String image = getIntent().getStringExtra("image");
        double tiredPercentage = getIntent().getDoubleExtra("tiredPercentage",0.0);
        new ImageToGallery().stringImageToGallery(image);
        byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(bitmap);
        //((ImageView) findViewById(R.id.imageView2)).getLayoutParams().height

        ProgressBar pBar = (ProgressBar)findViewById(R.id.tiredGauge);
        pBar.setMax(100);
        ProgressBarAnimation anim = new ProgressBarAnimation(pBar, 0, (int) tiredPercentage);
        anim.setDuration(1000);
        pBar.startAnimation(anim);

        pBar.setProgress((int) tiredPercentage);

        ((TextView)findViewById(R.id.textView2)).setText("" + (int) tiredPercentage);

        ImageView textImageView = findViewById(R.id.imageView5);
        if(tiredPercentage <= 25)
            textImageView.setImageDrawable(getResources().getDrawable(R.drawable.tired_01));
        else if(tiredPercentage <= 75)
            textImageView.setImageDrawable(getResources().getDrawable(R.drawable.tired_02));
        else
            textImageView.setImageDrawable(getResources().getDrawable(R.drawable.tired_03));


    }

    public void onStartButtonClicked(View v){
        Intent intent = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(intent);
        //final Intent intent = new Intent(this, MainActivity.class);

        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
    }
}

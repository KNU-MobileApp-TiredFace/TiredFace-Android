package com.example.thirty.tiredface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LobyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loby);
    }

    public void toMainActivity(View v){
        Intent intent = new Intent(LobyActivity.this,MainActivity.class);
        startActivity(intent);
    }
}

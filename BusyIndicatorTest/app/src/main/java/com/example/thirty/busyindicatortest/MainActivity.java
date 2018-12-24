package com.example.thirty.busyindicatortest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.MultiplePulseRing;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        MultiplePulseRing doubleBounce = new MultiplePulseRing();
        doubleBounce.setColorFilter(6737322,);
        progressBar.setIndeterminateDrawable(doubleBounce);
    }
}

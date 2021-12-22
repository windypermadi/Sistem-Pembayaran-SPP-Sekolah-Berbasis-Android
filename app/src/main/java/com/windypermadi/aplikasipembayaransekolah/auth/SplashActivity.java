package com.windypermadi.aplikasipembayaransekolah.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.windypermadi.aplikasipembayaransekolah.R;

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    ImageView img;
    TextView text, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img = findViewById(R.id.img);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.zoomsplash);
        img.startAnimation(myanim);
        text.startAnimation(myanim);
        text2.startAnimation(myanim);
    }
}
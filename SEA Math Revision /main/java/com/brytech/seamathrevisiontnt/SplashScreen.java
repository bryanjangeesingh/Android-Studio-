package com.brytech.seamathrevisiontnt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView displayLogo = findViewById(R.id.imageView);
        displayLogo.setImageResource(R.drawable.splash_screen);
        AnimationDrawable logo = (AnimationDrawable)displayLogo.getDrawable();
        logo.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent goToMain = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(goToMain);
                finish();
            }
        }, 2992);

    }
}

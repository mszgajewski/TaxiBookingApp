package com.mszgajewski.taxibookingapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    private TextView animatingText;
    private ImageView marker;
    Animation animateNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

        init();
    }

    private void init() {
        animatingText = (TextView) findViewById(R.id.textView);
        marker = (ImageView) findViewById(R.id.imageView);
        animateNow = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_animation);
        animatingText.setAnimation(animateNow);

        Glide.with(this).load(R.drawable.marker).into(marker);
    }
}
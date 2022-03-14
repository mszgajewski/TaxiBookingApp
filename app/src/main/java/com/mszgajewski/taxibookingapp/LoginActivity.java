package com.mszgajewski.taxibookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button phoneButton, googleButton;
    Animation phoneAnimation, googleAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneButton = (Button) findViewById(R.id.button2);
        googleButton = (Button) findViewById(R.id.button);

        phoneAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_animation);
        googleAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_animation);

        phoneButton.setAnimation(phoneAnimation);
        googleButton.setAnimation(googleAnimation);

    }

    public void phoneLoginClick(View view) {
        Intent intent  = new Intent(LoginActivity.this, PhoneLoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }
}
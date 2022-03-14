package com.mszgajewski.taxibookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hbb20.CountryCodePicker;

public class PhoneLoginActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    private String selected_country_code = "+48";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        ccp = findViewById(R.id.ccp);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
                ccp.setCountryPreference("PL");
            }
        });
    }
}
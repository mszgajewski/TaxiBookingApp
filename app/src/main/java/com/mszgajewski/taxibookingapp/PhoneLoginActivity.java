package com.mszgajewski.taxibookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class PhoneLoginActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    private EditText phoneEditText;
    private String selected_country_code = "+48";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        phoneEditText = (EditText) findViewById(R.id.editTextTextPersonName2);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
                ccp.setCountryPreference("PL");
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().length() == 9) {
                    Toast.makeText(PhoneLoginActivity.this,"halo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
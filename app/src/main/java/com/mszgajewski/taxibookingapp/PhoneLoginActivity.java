package com.mszgajewski.taxibookingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText phoneEditText;
    private PinView firstPinView;
    private ConstraintLayout phoneLayout;
    private ProgressBar progressBar;
    private String selected_country_code = "+48";
    private static final int CREDENTIAL_PICKER_REQUEST =120;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        ccp = findViewById(R.id.ccp);
        phoneEditText = findViewById(R.id.editTextTextPersonName2);
        firstPinView = findViewById(R.id.firstPinView);
        phoneLayout = findViewById(R.id.phoneLayout);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();



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
                     sendOtp();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().length() == 4) {

                    progressBar.setVisibility(View.VISIBLE);

                    PhoneAuthCredential credential =PhoneAuthProvider
                            .getCredential(mVerificationId, firstPinView
                            .getText().toString().trim());

                    signInWithAuthCredential(credential);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        HintRequest request = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Credentials.getClient(PhoneLoginActivity.this).getHintPickerIntent(request);
        try{
            startIntentSenderForResult(intent.getIntentSender(),CREDENTIAL_PICKER_REQUEST,null,0,0,0, new Bundle());
        }
        catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    firstPinView.setText(code);

                    signInWithAuthCredential(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(PhoneLoginActivity.this,"Kod niewysłany",Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.VISIBLE);
                firstPinView.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = mVerificationId;
                mResentToken = forceResendingToken;

                Toast.makeText(PhoneLoginActivity.this,"Kod wysłany", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
                phoneLayout.setVisibility(View.GONE);
                firstPinView.setVisibility(View.VISIBLE);
            }
        };

    }

    private void sendOtp() {

        progressBar.setVisibility(View.VISIBLE);

        String phoneNumber = selected_country_code+phoneEditText.getText().toString();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setPhoneNumber(phoneNumber)
                .setActivity(PhoneLoginActivity.this)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {

            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            phoneEditText.setText(credentials.getId().substring(3));


        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE){

            Toast.makeText(this, "Nie znaleziono numeru telefonu", Toast.LENGTH_LONG).show();
        }
    }


    private void signInWithAuthCredential(PhoneAuthCredential phoneAuthCredential) {
    mAuth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        Toast.makeText(PhoneLoginActivity.this,"Zalogowano",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PhoneLoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this,"Błąd logowania",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PhoneLoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                        // Animatoo.animateSlideRight(PhoneLoginActivity.this);
                    }
                }
            });
    }
}
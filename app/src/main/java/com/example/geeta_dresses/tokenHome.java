package com.example.geeta_dresses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class tokenHome extends AppCompatActivity {
    //variables
    TextView userName, userDescription;
    SharedPreferences userSP;
    Button backBTN, createTokenBTN, existingBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_home);
        Window window = tokenHome.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(tokenHome.this, R.color.black));
        //hocks
        userName = findViewById(R.id.userName);
        userDescription = findViewById(R.id.userDescription);
        backBTN = findViewById(R.id.backButtonTokenHome);
        createTokenBTN = findViewById(R.id.createTokenBTN);
        existingBTN = findViewById(R.id.existingTokenBTN);

        //User data part with SP
        userSP = getSharedPreferences("userMetadata", MODE_PRIVATE);
        userName.setText(userSP.getString("userName", ""));
        userDescription.setText(userSP.getString("userEmail", ""));

        //backBTN code
        backBTN.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
        //createTokenBTN code
        createTokenBTN.setOnClickListener(view -> {
            Intent createTokenIntent = new Intent(getApplicationContext(), tokenNumberCreation.class);
            startActivity(createTokenIntent);
        });
        //existingTokenBTN code
        existingBTN.setOnClickListener(view -> {
            Intent existingTokenIntent = new Intent(getApplicationContext(), ExistingToken.class);
            startActivity(existingTokenIntent);
        });
    }
}
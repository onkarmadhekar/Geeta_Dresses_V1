package com.example.geeta_dresses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ExistingToken extends AppCompatActivity {
    TextView userName, userDescription;
    EditText tokenNumber;
    SharedPreferences userSP;
    Button backBTN, attendTokenBTN, leaveTokenBTN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_token);
        Window window = ExistingToken.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ExistingToken.this, R.color.black));
        //hocks
        userName = findViewById(R.id.userNameTokenNumberET);
        userDescription = findViewById(R.id.userDescriptionTokenNumberET);
        tokenNumber = findViewById(R.id.tokenNumberBigET);
        backBTN = findViewById(R.id.backButtonTokenNumberET);
        attendTokenBTN =findViewById(R.id.attendTokenET);
        leaveTokenBTN = findViewById(R.id.leaveTokenET);

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
        attendTokenBTN.setOnClickListener(view -> {
            if(tokenNumber.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),"Empty Field now Allowed! Please enter the Token Number.",Toast.LENGTH_LONG).show();
            } else {
                String getTokenText = tokenNumber.getText().toString();
                SharedPreferences.Editor tokenEdit = userSP.edit();
                tokenEdit.putString("tokenNumber", getTokenText);
                tokenEdit.apply();
                Intent intent = new Intent(ExistingToken.this, tokenDashboard.class);
                startActivity(intent);
            }
        });
        //existingTokenBTN code
        leaveTokenBTN.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
    }
}
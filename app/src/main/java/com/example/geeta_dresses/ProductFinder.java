package com.example.geeta_dresses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ProductFinder extends AppCompatActivity {
    EditText productId, productName, productPrice;
    TextView userName, currentTokenNumber;
    SharedPreferences userSP;
    Button nextBTN, backBTN;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_finder);
        Window window = ProductFinder.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ProductFinder.this, R.color.black));
        //hocks
        userName = findViewById(R.id.userNamePF);
        currentTokenNumber = findViewById(R.id.userCurrentPF);
        productId = findViewById(R.id.prouctIdPF);
        productName = findViewById(R.id.prouctNamePF);
        productPrice = findViewById(R.id.prouctPricePF);
        nextBTN = findViewById(R.id.nextPFBTN);
        backBTN = findViewById(R.id.backBTNPF);
        //User data part with SP
        userSP = getSharedPreferences("userMetadata", MODE_PRIVATE);
        userName.setText(userSP.getString("userName", ""));
        currentTokenNumber.setText("Token No - " + userSP.getString("tokenNumber", ""));

        // Initializing Request Queue
        requestQueue = Volley.newRequestQueue(this);


        //backBTN code
        backBTN.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
        //nextBTN code
        nextBTN.setOnClickListener(view -> {
            Intent intent = new Intent(ProductFinder.this, tokenDashboard.class);

            if (!productName.getText().toString().isEmpty() && !productPrice.getText().toString().isEmpty() && !productId.getText().toString().isEmpty()) {
                intent.putExtra("activity", "ProductFinder");
                intent.putExtra("result", "OK");
                intent.putExtra("product_name", productName.getText().toString());
                intent.putExtra("productId", productId.getText().toString());
                intent.putExtra("productPrice", productPrice.getText().toString());
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please input all the above boxes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
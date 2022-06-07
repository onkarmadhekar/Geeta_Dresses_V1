package com.example.geeta_dresses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.geeta_dresses.constants.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class tokenNumberCreation extends AppCompatActivity {

    TextView userName, userDescription, tokenNumber;
    SharedPreferences userSP;
    Button backBTN, attendTokenBTN, leaveTokenBTN;
    Constant constant,constant_new;
    RequestQueue requestQueue,requestQueueUpdate;
    JsonObjectRequest tokenRequest,tokenUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_number);
        Window window = tokenNumberCreation.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(tokenNumberCreation.this, R.color.black));
        //hocks
        userName = findViewById(R.id.userNameTokenNumber);
        userDescription = findViewById(R.id.userDescriptionTokenNumber);
        tokenNumber = findViewById(R.id.tokenNumberBig);
        backBTN = findViewById(R.id.backButtonTokenNumber);
        attendTokenBTN =findViewById(R.id.attendToken);
        leaveTokenBTN = findViewById(R.id.leaveToken);

        //User data part with SP
        userSP = getSharedPreferences("userMetadata", MODE_PRIVATE);
        userName.setText(userSP.getString("userName", ""));
        userDescription.setText(userSP.getString("userEmail", ""));
        getToken();

        //backBTN code
        backBTN.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
        //createTokenBTN code
        attendTokenBTN.setOnClickListener(view -> {
                String getTokenText = tokenNumber.getText().toString();
                String userNameText = userName.getText().toString();
                updateToken(getTokenText,userNameText);
                Intent intent = new Intent(tokenNumberCreation.this,tokenDashboard.class);
                startActivity(intent);
        });

        //existingTokenBTN code
        leaveTokenBTN.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
    }

    // Fetching Token From API
    private void getToken(){
        constant = new Constant();
        // Initializing Volley
        requestQueue = Volley.newRequestQueue(this);

        // URL
        String url = constant.getURL() + constant.getPORT() + constant.getGET_TOKEN();
        // Initializing JSON Object
        JSONObject object = new JSONObject();
        // Initializing Object Request
        tokenRequest = new JsonObjectRequest(Request.Method.GET, url,object, response -> {
            //Log.d("String Request Response",response.toString());
            try {
                tokenNumber.setText(response.getString("counter"));
                Toast.makeText(getApplicationContext(),"Token Created!",Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Token Creation Failed!",Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            //Log.d("String Request Error",error.toString());
            tokenNumber.setText("---");
            Toast.makeText(getApplicationContext(),"Token Creation Failed!",Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(tokenRequest);

    }

    private void updateToken(String getTokenText,String userNameText){
        constant_new = new Constant();
        // Initializing Request Queue
        requestQueueUpdate = Volley.newRequestQueue(this);
        // URL
        String update_url = constant_new.getURL() + constant_new.getPORT() + constant_new.getUPDATE_TOKEN();
        // JSON OBJECT FOR POST
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        JSONObject updatedData = new JSONObject();
        try {
            updatedData.put("username",userNameText);
            updatedData.put("tokenNumber",getTokenText);
            updatedData.put("reason","null");
            updatedData.put("day", date);
            updatedData.put("isEnquired",false);
            updatedData.put("isPurchased",false);
            SharedPreferences.Editor tokenEdit = userSP.edit();
            tokenEdit.putString("tokenNumber",getTokenText);
            tokenEdit.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tokenUpdate = new JsonObjectRequest(Request.Method.POST, update_url, updatedData, response -> Log.d("Update Token Response",response.toString()), error -> Log.d("Update Token Response",error.toString()));
        requestQueueUpdate.add(tokenUpdate);
    }
}
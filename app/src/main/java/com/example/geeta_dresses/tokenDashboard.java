package com.example.geeta_dresses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.geeta_dresses.constants.Constant;
import com.example.geeta_dresses.models.InquiryModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class tokenDashboard extends AppCompatActivity {//implements AdapterView.OnItemSelectedListener {
    private RecyclerView courseRV;
    Constant constant;
    RequestQueue requestQueue;
    Switch inquiredSW, purchasedSW;
    JsonObjectRequest jsonObjectRequest;
    Button backBTN, nameBTN, nextBTN, dateBTN;
    TextView userName, currentTokenNumber , tvTotalAmount;
    SharedPreferences userSP;
    JSONObject response_object, object;
    InquiryModel inquiryModel;
    DatePickerDialog picker;
    int totalAmount;
    String dataSTR, reasonSTR;
    String[] courses = {"Less Varieties", "High Prices",
            "Required Other Brand", "Bad Service",
            "Required Other Variety"};

    // Arraylist for storing data
    private ArrayList<productsModel> productsModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_dashboard);
        Window window = tokenDashboard.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(tokenDashboard.this, R.color.black));
        //hocks
        userName = findViewById(R.id.userNameTokenNumber);
        currentTokenNumber = findViewById(R.id.userCurrentTokenNo);
        Spinner spino = findViewById(R.id.coursesspinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spino.setAdapter(adapter);
        //User data part with SP
        userSP = getSharedPreferences("userMetadata", MODE_PRIVATE);
        userName.setText(userSP.getString("userName", ""));
        currentTokenNumber.setText("Token No - " + userSP.getString("tokenNumber", ""));


        //hocks
        courseRV = findViewById(R.id.idRVCourse);
        backBTN = findViewById(R.id.backButtonTokenNumber);
        nameBTN = findViewById(R.id.nameBTN);
        nextBTN = findViewById(R.id.nextToDaBTN);
        dateBTN = findViewById(R.id.InquiryDate);
        inquiredSW = findViewById(R.id.InquirySW);
        purchasedSW = findViewById(R.id.PurchasedSW);
        tvTotalAmount = findViewById(R.id.idAmount);


        // here we have created new array list and added data to it.
        productsModelArrayList = new ArrayList<>();


        //Code for the intent from productFinder
        String resultIN, product_nameIN, productIDIN, product_priceIN;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            resultIN = null;
            product_nameIN = null;
            productIDIN = null;
            product_priceIN = null;
        } else {
            resultIN = extras.getString("result");
            product_nameIN = extras.getString("product_name");
            productIDIN = extras.getString("productId");
            product_priceIN = extras.getString("productPrice");
            Log.d("Data here Look", String.valueOf(extras));
            productsModelArrayList.add(new productsModel(product_nameIN, "1", product_priceIN, productIDIN));
            productsModelArrayList.add(new productsModel(product_nameIN, "1", product_priceIN, productIDIN));

        }
        // Using Constants
        constant = new Constant();
        // URL
        String url = constant.getURL() + constant.getPORT() + constant.getGET_TOKEN_DETAILS() + userSP.getString("tokenNumber", "");
        //userSP.getString("tokenNumber","");
        // Setting up request queue
        requestQueue = Volley.newRequestQueue(this);
        object = new JSONObject();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object, response -> {
            try {
                response_object = response;
                JSONArray data_array = response.getJSONArray("data");
                JSONObject data = (JSONObject) data_array.get(0);

                //reasonETV.setText(data.getString("reason"));
                dateBTN.setText(data.getString("day"));

                if (data.getBoolean("isEnquired")) {
                    inquiredSW.setTextOn("ON");
                }
                if (data.getBoolean("isPurchased")) {
                    purchasedSW.setTextOn("ON");
                }

                JSONArray product_array = data.getJSONArray("product");

                for (int i = 0; i < product_array.length(); i++) {
                    JSONObject product = product_array.getJSONObject(i);
                    String product_name = product.getString("productName");
                    String qty = product.getString("quantity");
                    String ActualPrice = product.getString("price");
                    String productId = product.getString("productId");
                    totalAmount = Integer.parseInt(qty) * Integer.parseInt(ActualPrice);
                    productsModelArrayList.add(new productsModel(product_name, qty, ActualPrice, productId));
                    courseRV.setAdapter(new productAdapter(tokenDashboard.this, productsModelArrayList));

                }
            } catch (JSONException e) {
                //Toast.makeText(tokenDashboard.this,"Failed to get token details!",Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(tokenDashboard.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);

        // we are initializing our adapter class and passing our arraylist to it.

        Log.d("Product array list here", String.valueOf(productsModelArrayList));
        productAdapter productAdapter = new productAdapter(this, productsModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(productAdapter);

        tvTotalAmount.setText(String.valueOf(totalAmount));
        //backBTN code
        backBTN.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
        nameBTN.setOnClickListener(view -> {
            //defination for the dynamic dialog box
            AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
            View v= LayoutInflater.from(view.getContext()).inflate(R.layout.productfinder_card_dialog,null);
            EditText product_id = v.findViewById(R.id.productIdPFD);
            EditText product_name = v.findViewById(R.id.prouctNamePFD);
            EditText product_price = v.findViewById(R.id.prouctPricePFD);
            builder.setView(v);

            //code for the positive button
            builder.setPositiveButton("Next", (dialog, which) -> {
                 Toast.makeText(v.getContext(),product_id.getText().toString() + "  " + product_name.getText().toString()+"  "+product_price.getText().toString(),Toast.LENGTH_LONG).show();
                if ( product_id.getText().toString().isEmpty() && product_name.getText().toString().isEmpty() && product_price.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(),"Complete the formality", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(v.getContext(),product_id.getText().toString() + "  " + product_name.getText().toString()+"  "+product_price.getText().toString(),Toast.LENGTH_LONG).show();
                    totalAmount = Integer.parseInt(product_price.getText().toString()) * Integer.parseInt(product_price.getText().toString());
                    productsModelArrayList.add(new productsModel(product_id.getText().toString(), "1", product_price.getText().toString(), product_name.getText().toString()));
                }
            });
            //code for the negative button
            builder.setNegativeButton("back", (dialog, which) -> dialog.dismiss());

            //output line for the calling
            final AlertDialog alertDialog=builder.create();
            alertDialog.show();

            //designing for buttons
            //positive
            Button button_positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            //button_positive.setBackgroundColor(Color.GRAY);
            button_positive.setPadding(20, 5, 20, 5);
            button_positive.setTextColor(Color.BLACK);
            //negative
            Button button_negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            //button_negative.setBackgroundColor(Color.GRAY);
            button_negative.setPadding(20, 5, 20, 5);
            button_negative.setTextColor(Color.BLACK);


    });
        dateBTN.setOnClickListener(v ->

    {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(tokenDashboard.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateBTN.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        String str = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        //setOrder_date_str(str);

                        dataSTR = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

                    }
                }, year, month, day);
        picker.show();
    });

    //nextBTN code
        nextBTN.setOnClickListener(view ->

    {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);

        inquiryModel = new InquiryModel();
        inquiryModel.setUserId(userSP.getString("userId", ""));
        inquiryModel.setUserName(userSP.getString("userName", ""));
        inquiryModel.setCustomerId("Customer ID");
        inquiryModel.setTokenNumber(userSP.getString("tokenNumber", ""));
        inquiryModel.setReason(reasonSTR);
        if (dataSTR == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            inquiryModel.setDay(dateFormat.format(date));
        } else {
            inquiryModel.setDay(dataSTR);
        }
        inquiryModel.setEnquired(inquiredSW.isChecked());
        inquiryModel.setPurchased(purchasedSW.isChecked());
        inquiryModel.setProductsModelArrayList(productsModelArrayList);

        try {
            constant = new Constant();
            object = new JSONObject(inquiryModel.toString());
            String url1 = constant.getURL() + constant.getPORT() + constant.getGET_TOKEN_DETAILS() + userSP.getString("tokenNumber", "");

            jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url1, object, response -> Toast.makeText(tokenDashboard.this, "Inquiry Added", Toast.LENGTH_SHORT).show(), error -> Toast.makeText(tokenDashboard.this, "Failed To Add Inquiry", Toast.LENGTH_SHORT).show());
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Toast.makeText(tokenDashboard.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        startActivity(intent);
    });
}

// Performing action when ItemSelected
// from spinner, Overriding onItemSelected method
//
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////        Toast.makeText(getApplicationContext(),
////                courses[i],
////                Toast.LENGTH_LONG)
////                .show();
//        reasonSTR = courses[i];
//    }
//
//
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
}



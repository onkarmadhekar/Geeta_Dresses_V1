package com.example.geeta_dresses;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.geeta_dresses.constants.Constant;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variable
    private RecyclerView courseRV;
    Constant constant, constant_1;
    JsonObjectRequest jsonObjectRequest, jsonObjectRequest_1, jsonObjectRequestCSV;
    JSONObject object, object_1,objectCSV;
    RequestQueue requestQueue, requestQueue_1,requestQueueCSV;
    JSONObject response_object, response_object_1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ExtendedFloatingActionButton addFab, addTokenbtn, filterbtn;
    Button downloadCSV;
    //FloatingActionButton addTokenbtn;
    SharedPreferences spLogin, spToken, spUserData, userSP;
    TextView inquiryCount;//, addTokenTV;
    private long pressedTime;
    Boolean isAllFabsVisible;
    DatePickerDialog picker;
    String startDateSTR, endDateSTR;

    // Arraylist for storing data
    private ArrayList<inquiryModel> inquiryModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        Window window = Dashboard.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(Dashboard.this, R.color.black));

        //hocks
        courseRV = findViewById(R.id.idInquiryRV);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarHere);
        addTokenbtn = findViewById(R.id.addTokenbtn);
        addFab = findViewById(R.id.add_fab);
        filterbtn = findViewById(R.id.filterbtn);
        inquiryCount = findViewById(R.id.textView4);
        downloadCSV = findViewById(R.id.dowloadCSV);
        //User data part with SP
        userSP = getSharedPreferences("userMetadata", MODE_PRIVATE);

        //code for the floating buttons
        addTokenbtn.setVisibility(View.GONE);
        filterbtn.setVisibility(View.GONE);
        //addTokenTV.setVisibility(View.GONE);
        isAllFabsVisible = false;
        addFab.shrink();
        //Tool bar as action bar
        setSupportActionBar(toolbar);

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navi_drawer_open, R.string.navi_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.drawable.menu);


        navigationView.setNavigationItemSelectedListener(this);

        // here we have created new array list and added data to it.
        inquiryModelArrayList = new ArrayList<>();

        // Using Constants
        constant = new Constant();
        // URL
        String url = constant.getURL() + constant.getPORT() + constant.getUPDATE_TOKEN();
        //userSP.getString("tokenNumber","");
        // Setting up request queue
        requestQueue = Volley.newRequestQueue(this);
        object = new JSONObject();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object, response -> {
            try {
                response_object = response;
                JSONArray data_array = response.getJSONArray("data");

                inquiryCount.setText("Total inquiries - " + data_array.length());
                for (int i = 0; i < data_array.length(); i++) {
                    //JSONObject product = product_array.getJSONObject(i);
                    JSONObject data = (JSONObject) data_array.get(i);
                    String inquiryNo = data.getString("tokenNumber");
                    String inquiryUser = data.getString("username");
                    String rawDay = data.getString("day");
                    //String inquiryDay = rawDay.substring(0,10);
                    String inquiryDay = rawDay;

                    String[] mobileArray = {};//= {"Android","IPhone","WindowsMobile","Blackberry",
//                                "WebOS","Ubuntu","Windows7","Max OS X"};
                    ArrayList<String> productArray = new ArrayList<String>();
                    JSONArray product_array = data.getJSONArray("product");
                    Log.d("Product_array_dialog", String.valueOf(product_array));
                    for (int j = 0; j < product_array.length(); j++) {
                        JSONObject product = product_array.getJSONObject(j);
                        String product_id = product.getString("productId");
                        String product_name = product.getString("productName");
                        String qty = product.getString("quantity");
                        String price = product.getString("price");
                        String amount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
                        productArray.add(product_id + "    " + product_name + "     " + qty + "     " + price + "     " + amount);
                        Log.d("MobileArray", product_id + "  " + product_name + "   " + qty + "   " + price + "   " + amount);
                    }
                    mobileArray = productArray.toArray(mobileArray);
                    Boolean isInquired = data.getBoolean("isEnquired");
                    Boolean isPurchased = data.getBoolean("isPurchased");
                    String userRole = userSP.getString("userRole", "");
                    String billNo;
                    try {
                        billNo = data.getString("billNo");
                    } catch (JSONException e) {
                        billNo = "";
                    }

                    boolean isManger;
                    if (userRole.equals("manager")) {
                        isManger = true;
                    } else {
                        isManger = false;
                    }
                    inquiryModelArrayList.add(new inquiryModel(inquiryNo, inquiryUser, inquiryDay, mobileArray, isInquired, isPurchased, isManger, billNo));
                    courseRV.setAdapter(new inquiryAdapter(Dashboard.this, inquiryModelArrayList));

                }
            } catch (JSONException e) {
                //Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Dashboard.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
        // we are initializing our adapter class and passing our arraylist to it.
        inquiryAdapter inquiryAdapter = new inquiryAdapter(this, inquiryModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(inquiryAdapter);

        //Floating button implementation

        addFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                addTokenbtn.show();
                filterbtn.show();
                //addTokenTV.setVisibility(View.VISIBLE);

                addFab.extend();
                isAllFabsVisible = true;
            } else {
                addTokenbtn.hide();
                filterbtn.hide();
                //addTokenTV.setVisibility(View.GONE);

                addFab.shrink();
                isAllFabsVisible = false;
            }
        });
        addTokenbtn.setOnClickListener(view -> {
            Intent addOrderIntent = new Intent(getApplicationContext(), tokenHome.class);
            startActivity(addOrderIntent);
            //Toast.makeText(getApplicationContext(), "Clicked on add order", Toast.LENGTH_SHORT).show();
        });
        downloadCSV.setOnClickListener(view -> {
            filterCode(view,"Download CSV");
        });

        filterbtn.setOnClickListener(view -> {
            filterCode(view,"Set Filter");
        });
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        View view ;


        switch (item.getItemId()) {
            case R.id.nav_token:
                startActivity(new Intent(this, tokenHome.class));
                return true;

            case R.id.logout:
                spLogin = getSharedPreferences("login", MODE_PRIVATE);
                spLogin.edit().clear().apply();

                spToken = getSharedPreferences("tokenSharedPreferences", MODE_PRIVATE);
                spToken.edit().clear().apply();

                spUserData = getSharedPreferences("userMetadata", MODE_PRIVATE);
                spUserData.edit().clear().apply();

                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filterCode(View view,String typeHere){
        Toast.makeText(Dashboard.this, "Clicked filter!!", Toast.LENGTH_SHORT).show();
        //defination for the dynamic dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View view2 = LayoutInflater.from(view.getContext()).inflate(R.layout.filter_card_dialog, null);
        Button startDate = view2.findViewById(R.id.startDate);
        Button endDate = view2.findViewById(R.id.endDate);
        Switch ApprovedSW = view2.findViewById(R.id.ApprovedSW);
        builder.setView(view2);

        startDate.setOnClickListener(view1 -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(Dashboard.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String monthOfYearSTR = String.valueOf((monthOfYear)+1),dayOfMonthSTR = String.valueOf(dayOfMonth);
                            if((monthOfYear + 1)<9){
                                monthOfYearSTR = 0+String.valueOf((monthOfYear)+1);
                            }
                            if((dayOfMonth + 1)<9){
                                dayOfMonthSTR = 0+String.valueOf(dayOfMonth);
                            }
                            startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            String str = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            //setOrder_date_str(str);
                            startDateSTR = year + "/" + monthOfYearSTR + "/" + dayOfMonthSTR;

                        }
                    }, year, month, day);
            picker.show();

        });

        endDate.setOnClickListener(view1 -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(Dashboard.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String monthOfYearSTR = String.valueOf((monthOfYear)+1),dayOfMonthSTR = String.valueOf(dayOfMonth);
                            if((monthOfYear + 1)<9){
                                monthOfYearSTR = 0+String.valueOf((monthOfYear)+1);
                            }
                            if((dayOfMonth + 1)<9){
                                dayOfMonthSTR = 0+String.valueOf(dayOfMonth);
                            }
                            endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            String str = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            //setOrder_date_str(str);
                            endDateSTR = year + "/" + monthOfYearSTR + "/" + dayOfMonthSTR;


                        }
                    }, year, month, day);
            picker.show();
        });

        //code for the positive button
        builder.setPositiveButton(typeHere, (dialog, which) -> {
            // Onkar Write Code Here
            constant_1 = new Constant();
            requestQueue_1 = Volley.newRequestQueue(this);
//            String url1 = constant_1.getURL() + constant_1.getPORT() + constant_1.getUPDATE_TOKEN() + "filter?from=" + startDateSTR + "&to=" + endDateSTR + "&isApproved=" + ApprovedSW.isChecked();
            String url2 = constant_1.getURL() + constant_1.getPORT() + constant_1.getUPDATE_TOKEN() + "getCSV?from=" + startDateSTR + "&to=" + endDateSTR + "&isApproved=" + ApprovedSW.isChecked();
            String urlO ;
            if(typeHere == "Download CSV"){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url2));
                startActivity(intent);
            }else{
                String url1 = constant_1.getURL() + constant_1.getPORT() + constant_1.getUPDATE_TOKEN() + "filter?from=" + startDateSTR + "&to=" + endDateSTR + "&isApproved=" + ApprovedSW.isChecked();
                jsonObjectRequest_1 = new JsonObjectRequest(Request.Method.GET, url1, object_1, response -> {

                    try {
                        inquiryModelArrayList.clear();
                        response_object = response;
                        JSONArray data_array = response.getJSONArray("data");

                        inquiryCount.setText("Total inquiries - " + data_array.length());
                        for (int i = 0; i < data_array.length(); i++) {
                            //JSONObject product = product_array.getJSONObject(i);
                            JSONObject data = (JSONObject) data_array.get(i);
                            String inquiryNo = data.getString("tokenNumber");
                            String inquiryUser = data.getString("username");
                            String rawDay = data.getString("day");
                            //String inquiryDay = rawDay.substring(0,10);
                            String inquiryDay = rawDay;

                            String[] mobileArray = {};//= {"Android","IPhone","WindowsMobile","Blackberry",-
//                                "WebOS","Ubuntu","Windows7","Max OS X"};
                            ArrayList<String> productArray = new ArrayList<String>();
                            JSONArray product_array = data.getJSONArray("product");
                            Log.d("Product_array_dialog", String.valueOf(product_array));
                            for (int j = 0; j < product_array.length(); j++) {
                                JSONObject product = product_array.getJSONObject(j);
                                String product_id = product.getString("productId");
                                String product_name = product.getString("productName");
                                String qty = product.getString("quantity");
                                String price = product.getString("price");
                                String amount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
                                productArray.add(product_id + "  " + product_name + "   " + qty + "   " + price + "   " + amount);
                                Log.d("MobileArray", product_id + "  " + product_name + "   " + qty + "   " + price + "   " + amount);
                            }
                            mobileArray = productArray.toArray(mobileArray);
                            Log.d("MobileArray", String.valueOf(mobileArray));
                            Boolean isInquired = data.getBoolean("isEnquired");
                            Boolean isPurchased = data.getBoolean("isPurchased");
                            String userRole = userSP.getString("userRole", "");
                            String billNo;
                            try {
                                billNo = data.getString("billNo");
                            } catch (JSONException e) {
                                billNo = "";
                            }

                            boolean isManger;
                            if (userRole.equals("manager")) {
                                isManger = true;
                            } else {
                                isManger = false;
                            }
                            inquiryModelArrayList.add(new inquiryModel(inquiryNo, inquiryUser, inquiryDay, mobileArray, isInquired, isPurchased, isManger, billNo));
                            courseRV.setAdapter(new inquiryAdapter(Dashboard.this, inquiryModelArrayList));

                        }
                    } catch (JSONException e) {
                        //Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(this, "Something Went Wrong! hjhj" + error, Toast.LENGTH_SHORT).show();
                });
                requestQueue_1.add(jsonObjectRequest_1);

                //Toast.makeText(view2.getContext(), "Start date " + startDateSTR + "end date" + endDateSTR, Toast.LENGTH_LONG).show();
                startDateSTR = null;
                endDateSTR = null;
            }

//            jsonObjectRequest_1 = new JsonObjectRequest(Request.Method.GET, urlO, object_1, response -> {
//
//                try {
//                    inquiryModelArrayList.clear();
//                    response_object = response;
//                    JSONArray data_array = response.getJSONArray("data");
//
//                    inquiryCount.setText("Total inquiries - " + data_array.length());
//                    for (int i = 0; i < data_array.length(); i++) {
//                        //JSONObject product = product_array.getJSONObject(i);
//                        JSONObject data = (JSONObject) data_array.get(i);
//                        String inquiryNo = data.getString("tokenNumber");
//                        String inquiryUser = data.getString("username");
//                        String rawDay = data.getString("day");
//                        //String inquiryDay = rawDay.substring(0,10);
//                        String inquiryDay = rawDay;
//
//                        String[] mobileArray = {};//= {"Android","IPhone","WindowsMobile","Blackberry",-
////                                "WebOS","Ubuntu","Windows7","Max OS X"};
//                        ArrayList<String> productArray = new ArrayList<String>();
//                        JSONArray product_array = data.getJSONArray("product");
//                        Log.d("Product_array_dialog", String.valueOf(product_array));
//                        for (int j = 0; j < product_array.length(); j++) {
//                            JSONObject product = product_array.getJSONObject(j);
//                            String product_id = product.getString("productId");
//                            String product_name = product.getString("productName");
//                            String qty = product.getString("quantity");
//                            String price = product.getString("price");
//                            String amount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
//                            productArray.add(product_id + "  " + product_name + "   " + qty + "   " + price + "   " + amount);
//                            Log.d("MobileArray", product_id + "  " + product_name + "   " + qty + "   " + price + "   " + amount);
//                        }
//                        mobileArray = productArray.toArray(mobileArray);
//                        Log.d("MobileArray", String.valueOf(mobileArray));
//                        Boolean isInquired = data.getBoolean("isEnquired");
//                        Boolean isPurchased = data.getBoolean("isPurchased");
//                        String userRole = userSP.getString("userRole", "");
//                        String billNo;
//                        try {
//                            billNo = data.getString("billNo");
//                        } catch (JSONException e) {
//                            billNo = "";
//                        }
//
//                        boolean isManger;
//                        if (userRole.equals("manager")) {
//                            isManger = true;
//                        } else {
//                            isManger = false;
//                        }
//                        inquiryModelArrayList.add(new inquiryModel(inquiryNo, inquiryUser, inquiryDay, mobileArray, isInquired, isPurchased, isManger, billNo));
//                        courseRV.setAdapter(new inquiryAdapter(Dashboard.this, inquiryModelArrayList));
//
//                    }
//                } catch (JSONException e) {
//                    //Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show();
//                }
//            }, error -> {
//                Toast.makeText(this, "Something Went Wrong!" + error, Toast.LENGTH_SHORT).show();
//            });
//            requestQueue_1.add(jsonObjectRequest_1);
//
//            Toast.makeText(view2.getContext(), "Start date " + startDateSTR + "end date" + endDateSTR, Toast.LENGTH_LONG).show();
//            startDateSTR = null;
//            endDateSTR = null;
        });
        //code for the negative button
        builder.setNegativeButton("back", (dialog, which) -> dialog.dismiss());

        //output line for the calling
        final AlertDialog alertDialog = builder.create();
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
    }

}
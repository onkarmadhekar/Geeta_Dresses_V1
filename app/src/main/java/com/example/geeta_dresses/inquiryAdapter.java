package com.example.geeta_dresses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.geeta_dresses.constants.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class inquiryAdapter extends RecyclerView.Adapter<inquiryAdapter.Viewfinder> {

    private Context context;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    JSONObject data;
    Constant constant;
    SharedPreferences userSP;

    ArrayList<inquiryModel> inquiryModelArrayList;


    // Constructor
    public inquiryAdapter(Context context, ArrayList<inquiryModel> inquiryModelArrayList) {
        this.context = context;
        this.inquiryModelArrayList = inquiryModelArrayList;
    }

    @NonNull
    @Override
    public Viewfinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_inquiry_cardview, parent, false);
        return new Viewfinder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewfinder holder, int position) {
        // to set data to textview and imageview of each card layout
        inquiryModel model = inquiryModelArrayList.get(position);
        holder.idInquiryNo.setText(model.getInquiry_no());
        holder.idInquiryUser.setText(model.getInquiry_user());
        holder.idInquiryDay.setText(model.getInquiry_day());

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return inquiryModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewfinder extends RecyclerView.ViewHolder {
        private final TextView idInquiryNo;
        private final TextView idInquiryUser;
        private final TextView idInquiryDay;
        private Context context;

        public Viewfinder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            idInquiryNo = itemView.findViewById(R.id.idInquiryNo);
            idInquiryUser = itemView.findViewById(R.id.idInquiryUser);
            idInquiryDay = itemView.findViewById(R.id.idInquiryDay);

            itemView.setOnClickListener(v -> {

                //definition for the dynamic dialog box
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=LayoutInflater.from(context).inflate(R.layout.activity_custom_dialog,null);

                builder.setView(view);
                //hocks and code
                TextView DialogUserName = view.findViewById(R.id.DialoagUserName);
                DialogUserName.setText(inquiryModelArrayList.get(getAdapterPosition()).getInquiry_user());
                TextView DialogToken = view.findViewById(R.id.DialogTokenNumber);
                DialogToken.setText(inquiryModelArrayList.get(getAdapterPosition()).getInquiry_no());
                TextView DialogDay = view.findViewById(R.id.DialogDay);
                DialogDay.setText(inquiryModelArrayList.get(getAdapterPosition()).getInquiry_day());
                CheckBox isInquired = view.findViewById(R.id.checkBox);
                isInquired.setChecked(inquiryModelArrayList.get(getAdapterPosition()).getInquired());
                CheckBox isPurchased = view.findViewById(R.id.checkBox2);
                isPurchased.setChecked(inquiryModelArrayList.get(getAdapterPosition()).getPurchased());

                //code for the listview in there
                ArrayAdapter adapter = new ArrayAdapter<String>(context,
                        R.layout.activity_list_view, inquiryModelArrayList.get(getAdapterPosition()).getProduct_list());

                ListView listView = view.findViewById(R.id.product_list);
                listView.setAdapter(adapter);
                //code for the positive button
                builder.setPositiveButton("Approve", (dialog, which) -> {
                    Boolean isPrivillaged = inquiryModelArrayList.get(getAdapterPosition()).getManger();
                    if(isPrivillaged){
                        requestQueue = Volley.newRequestQueue(context);
                        constant = new Constant();

                        // URL
                        userSP = context.getSharedPreferences("userMetadata", context.MODE_PRIVATE);
                        String url = constant.getURL() + constant.getPORT() + constant.getGET_TOKEN_DETAILS() + inquiryModelArrayList.get(getAdapterPosition()).getInquiry_no();
                        Log.d("URL here",url);
                        data = new JSONObject();
                        try {
                            data.put("isApproved",true);
                            data.put("approvedBy",userSP.getString("userName",""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, data, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Onkar See Here",response.toString());
                                Toast.makeText(context,"Manager", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Onkar See Here Error",error.toString());
                            }
                        });


                    }else {
                        Toast.makeText(context, "Not Manager", Toast.LENGTH_SHORT).show();
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
                //button_positive.setEnabled(inquiryModelArrayList.get(getAdapterPosition()).getManger());
                //negative
                Button button_negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                //button_negative.setBackgroundColor(Color.GRAY);
                button_negative.setPadding(20, 5, 20, 5);
                button_negative.setTextColor(Color.BLACK);

            });

        }
    }


}

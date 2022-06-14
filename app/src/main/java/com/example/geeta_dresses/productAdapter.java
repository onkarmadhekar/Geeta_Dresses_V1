package com.example.geeta_dresses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class productAdapter extends RecyclerView.Adapter<productAdapter.Viewfinder> {

    View rootView;
    private Context context;
    ArrayList<productsModel> productModelArrayList;
    int totalMoney = 0;
    TextView totalAmount;
    // Constructor
    public productAdapter(Context context, ArrayList<productsModel> productModelArrayList) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public Viewfinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_products_cardview, parent, false);
        context = parent.getContext();
        rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        totalAmount = (TextView) rootView.findViewById(R.id.idAmount);
        return new Viewfinder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull Viewfinder holder, int position) {
        // to set data to textview and imageview of each card layout
        productsModel model = productModelArrayList.get(position);
        holder.idProductName.setText(model.getproduct_name());
        holder.idProductQTY.setText(model.getproduct_qty());
        Log.d("Product Prixe",model.getProduct_prize());
        Log.d("Product ID",model.getProduct_id());
//        totalMoney = 0;
//        totalMoney = totalMoney +(Integer.parseInt(model.getproduct_qty())*Integer.parseInt(model.getProduct_prize()));

        for(int i = 0;i<getItemCount();i++){
            productsModel model1 = productModelArrayList.get(i);
            totalMoney = totalMoney +(Integer.parseInt(model1.getproduct_qty())*Integer.parseInt(model1.getProduct_prize()));
            totalAmount.setText(String.valueOf(totalMoney));
        }
        totalAmount.setText(String.valueOf(totalMoney));
        totalMoney = 0;
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return productModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewfinder extends RecyclerView.ViewHolder {
        private final TextView idProductName;
        private final TextView idProductQTY;
        private Context context;

        public Viewfinder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            idProductName = itemView.findViewById(R.id.idProductsName);
            idProductQTY = itemView.findViewById(R.id.idProductsQTY);



            itemView.findViewById(R.id.reduceBTN).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productModelArrayList.get(getAdapterPosition()).setproduct_qty(String.valueOf(Integer.parseInt(productModelArrayList.get(getAdapterPosition()).getproduct_qty())-1));
                    //productModelArrayList.get(getAdapterPosition()).setProduct_prize(String.valueOf(Integer.parseInt(productModelArrayList.get(getAdapterPosition()).getproduct_qty())*Integer.parseInt(productModelArrayList.get(getAdapterPosition()).getProduct_prize())));

                    for(int i = 0;i<getItemCount();i++){
                        productsModel model1 = productModelArrayList.get(i);
                        totalMoney = totalMoney +(Integer.parseInt(model1.getproduct_qty())*Integer.parseInt(model1.getProduct_prize()));
                        totalAmount.setText(String.valueOf(totalMoney));
                    }
                    totalAmount.setText(String.valueOf(totalMoney));
                    totalMoney = 0;
                    notifyItemChanged( getAdapterPosition() );
                    notifyDataSetChanged();
                    //Toast.makeText(context, "Reduces", Toast.LENGTH_LONG).show();
                }
            });
            itemView.findViewById(R.id.increaseBTN).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productModelArrayList.get(getAdapterPosition()).setproduct_qty(String.valueOf(Integer.parseInt(productModelArrayList.get(getAdapterPosition()).getproduct_qty()) +1));
                    //productModelArrayList.get(getAdapterPosition()).setProduct_prize(String.valueOf(Integer.parseInt(productModelArrayList.get(getAdapterPosition()).getproduct_qty())*Integer.parseInt(productModelArrayList.get(getAdapterPosition()).getProduct_prize())));

                    for(int i = 0;i<getItemCount();i++){
                        productsModel model1 = productModelArrayList.get(i);
                        totalMoney = totalMoney +(Integer.parseInt(model1.getproduct_qty())*Integer.parseInt(model1.getProduct_prize()));
                        totalAmount.setText(String.valueOf(totalMoney));
                    }
                    totalAmount.setText(String.valueOf(totalMoney));
                    totalMoney = 0;
                    notifyItemChanged( getAdapterPosition() );
                    notifyDataSetChanged();
                    //Toast.makeText(context, "Increase", Toast.LENGTH_LONG).show();
                }
            });
            itemView.findViewById(R.id.productsDeleteBTN).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productModelArrayList.remove(getAdapterPosition());

                    for(int i = 0;i<getItemCount();i++){
                        productsModel model1 = productModelArrayList.get(i);
                        totalMoney = totalMoney +(Integer.parseInt(model1.getproduct_qty())*Integer.parseInt(model1.getProduct_prize()));
                        totalAmount.setText(String.valueOf(totalMoney));
                    }
                    totalAmount.setText(String.valueOf(totalMoney));
                    totalMoney = 0;
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();

                    Toast.makeText(context, "delete", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

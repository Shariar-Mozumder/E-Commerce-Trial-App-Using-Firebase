package com.example.shariarspc.ecommercetrial.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shariarspc.ecommercetrial.Model.Orders;
import com.example.shariarspc.ecommercetrial.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    DatabaseReference orderref;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderref= FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerView=findViewById(R.id.order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options=
                new FirebaseRecyclerOptions.Builder<Orders>().setQuery(orderref,Orders.class).build();

        FirebaseRecyclerAdapter<Orders,orderViewHolder> adapter=new FirebaseRecyclerAdapter<Orders, orderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull orderViewHolder orderViewHolder, final int i, @NonNull final Orders orders) {

                orderViewHolder.username.setText("Name: "+orders.getName());
                orderViewHolder.userPhone.setText("Phone: "+orders.getPhone());
                orderViewHolder.userTotalPrice.setText("Total amount: "+orders.getTotalamount());
                orderViewHolder.userAddress.setText("Customer's Address: "+orders.getAddress()+","+orders.getDistrict());
                orderViewHolder.userdateTime.setText("Ordering Date"+orders.getTime()+orders.getDate());

                orderViewHolder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID=getRef(i).getKey();

                        Intent intent=new Intent(AdminNewOrderActivity.this, AdminUserProducts.class);
                        intent.putExtra("pid",uID);
                        startActivity(intent);
                    }
                });

                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(AdminNewOrderActivity.this);
                        alertDialog.setTitle("HaVE yOU Shipped These Orderd Products?");
                        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    String uID=getRef(i).getKey();
                                    removeProduct(uID);
                                }
                                else if (i==1){
                                    finish();
                                }
                            }
                        });
                        alertDialog.show();
                    }
                });
            }

            @NonNull
            @Override
            public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_design,parent,false);

                return new orderViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeProduct(String uID) {
        orderref.child(uID).removeValue();
    }

    public class orderViewHolder extends RecyclerView.ViewHolder{

        public TextView username,userPhone,userAddress,userTotalPrice,userdateTime;
        public Button showOrdersBtn;

        public orderViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.order_user_name);
            userPhone=itemView.findViewById(R.id.user_phn_number);
            userTotalPrice=itemView.findViewById(R.id.total_price);
            userAddress=itemView.findViewById(R.id.adress);
            userdateTime=itemView.findViewById(R.id.date_time);
            showOrdersBtn=itemView.findViewById(R.id.showOrderbtn);
        }
    }
}

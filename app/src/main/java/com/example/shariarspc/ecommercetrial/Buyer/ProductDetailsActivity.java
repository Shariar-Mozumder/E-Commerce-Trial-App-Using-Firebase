package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shariarspc.ecommercetrial.Model.Products;
import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button add_to_cart_btn;
    private ElegantNumberButton numberButton;
    private ImageView productImage;
    private TextView nameTV,description_TV,price_TV;
    private String ProductID,state="normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        add_to_cart_btn=findViewById(R.id.add_product_to_cart_btn);
        numberButton=findViewById(R.id.number_btn);
        productImage=findViewById(R.id.product_image_view);
        nameTV=findViewById(R.id.product_name_TV);
        description_TV=findViewById(R.id.product_description_TV);
        price_TV=findViewById(R.id.product_description_TV);

        ProductID=getIntent().getStringExtra("pid");

        getProductDetails(ProductID);

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.equals("Order Shipped")||state.equals("Order Placed")){
                    Toast.makeText(ProductDetailsActivity.this, "You Can Add More Products Once Your Orders are shipped...", Toast.LENGTH_SHORT).show();
                }
                else{
                    addingTocart();
                }
                

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
    }

    private void addingTocart() {
        String saveCurrentTime,saveCurrentDate;

        Calendar callfordate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate=currentDate.format(callfordate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(callfordate.getTime());

        final DatabaseReference cartListref=FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",ProductID);
        cartMap.put("pname",nameTV.getText().toString());
        cartMap.put("pdescription",description_TV.getText().toString());
        cartMap.put("pprice",price_TV.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListref.child("User View").child(Prevelent.currentOnlineUsers.getPhone()).child("Products")
                .child(ProductID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            cartListref.child("Admin View").child(Prevelent.currentOnlineUsers.getPhone()).child("Products")
                                    .child(ProductID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "This Product is added to the Cart .", Toast.LENGTH_SHORT).show();

                                            Intent intent=new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetails(String productID) {
        DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child("Products");

        productref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);

                    nameTV.setText(products.getPname());
                    description_TV.setText(products.getPdescription());
                    price_TV.setText(products.getPprice());
                    Picasso.get().load(products.getPimage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkOrderState(){
        final DatabaseReference orderref=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelent.currentOnlineUsers.getPhone());

        orderref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shipmentState= dataSnapshot.child("state").getValue().toString();
                    String username= dataSnapshot.child("name").getValue().toString();

                    if(shipmentState.equals("shipped")){

                        state="Order Shipped";

                    }
                    else if(shipmentState.equals("not shipped")){
                       state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

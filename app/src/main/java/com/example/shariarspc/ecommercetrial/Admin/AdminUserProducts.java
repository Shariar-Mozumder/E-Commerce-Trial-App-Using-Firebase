package com.example.shariarspc.ecommercetrial.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shariarspc.ecommercetrial.Model.Cart;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProducts extends AppCompatActivity {

    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlistRef;
    private String userPhone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        productList=findViewById(R.id.products_list);
        productList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);

        userPhone=getIntent().getStringExtra("pid").toString();

        cartlistRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userPhone).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlistRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                cartViewHolder.txtProductName.setText(cart.getPname());
                cartViewHolder.txtProductQuantity.setText(cart.getQuantity());
                cartViewHolder.txtProductPrice.setText(cart.getPprice());



            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder cartViewHolder=new CartViewHolder(view);
                return cartViewHolder;
            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();
    }


}

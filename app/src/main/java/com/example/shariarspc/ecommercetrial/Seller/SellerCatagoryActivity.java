package com.example.shariarspc.ecommercetrial.Seller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.shariarspc.ecommercetrial.R;

public class SellerCatagoryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView tshirts,sports,female_dresses,sweater;
    ImageView glasses,purses,hats,shoes;
    ImageView headphones,laptops,watches,mobiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_catagory);

        tshirts=findViewById(R.id.t_shirts);
        sports=findViewById(R.id.sports_t_shirts);
        female_dresses=findViewById(R.id.female_dresses);
        sweater=findViewById(R.id.sweater);
        glasses=findViewById(R.id.glasses);
        purses=findViewById(R.id.purses);
        hats=findViewById(R.id.hats);
        shoes=findViewById(R.id.shoeses);
        headphones=findViewById(R.id.headphones);
        laptops=findViewById(R.id.laptops);
        watches=findViewById(R.id.watches);
        mobiles=findViewById(R.id.mobiles);


        tshirts.setOnClickListener(this);
        sports.setOnClickListener(this);
        female_dresses.setOnClickListener(this);
        sweater.setOnClickListener(this);
        glasses.setOnClickListener(this);
        purses.setOnClickListener(this);
        hats.setOnClickListener(this);
        shoes.setOnClickListener(this);
        headphones.setOnClickListener(this);
        laptops.setOnClickListener(this);
        watches.setOnClickListener(this);
        mobiles.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.t_shirts:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","tshirts");
                startActivity(intent);
                break;}

            case R.id.sports_t_shirts:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","sports tshirts");
                startActivity(intent);
                break;}

            case R.id.female_dresses:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","female dresses");
                startActivity(intent);
                break;}

            case R.id.sweater:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","sweater");
                startActivity(intent);
                break;}

            case R.id.glasses:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","glasses");
                startActivity(intent);
                break;}

            case R.id.purses:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","purses");
                startActivity(intent);
                break;}

            case R.id.hats:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","hats");
                startActivity(intent);
                break;}

            case R.id.shoeses:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","shoeses");
                startActivity(intent);
                break;}

            case R.id.headphones:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","headphones");
                startActivity(intent);
                break;}

            case R.id.laptops:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","laptops");
                startActivity(intent);
                break;}

            case R.id.watches:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","watches");
                startActivity(intent);
                break;}

            case R.id.mobiles:{
                Intent intent=new Intent(SellerCatagoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("catagory","mobiles");
                startActivity(intent);
                break;}
        }
    }
}

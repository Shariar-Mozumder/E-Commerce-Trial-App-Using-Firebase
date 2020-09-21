package com.example.shariarspc.ecommercetrial.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shariarspc.ecommercetrial.Buyer.HomeActivity;
import com.example.shariarspc.ecommercetrial.Buyer.MainActivity;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.Seller.SellerCatagoryActivity;

public class AdminHomeActivity extends AppCompatActivity {

    Button logoutBtn,checknewBtn,maintainBtn,approveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn=findViewById(R.id.logout_admin_btn);
        checknewBtn=findViewById(R.id.check_order_btn);
      maintainBtn=findViewById(R.id.maintain_btn);
        approveBTN=findViewById(R.id.check_products_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checknewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        approveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, ChecktoApproveProducts.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });
    }
}

package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Model.Users;
import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.Seller.SellerHomeActivity;
import com.example.shariarspc.ecommercetrial.Seller.SellerRegistration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button loginbtn,joinnowbtn;
    private ProgressDialog progressDialog;
    private TextView sellerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginbtn=findViewById(R.id.main_login_btn);
        joinnowbtn=findViewById(R.id.main_join_now_btn);
        sellerTextView=findViewById(R.id.become_a_seller);
        progressDialog=new ProgressDialog(this);
        Paper.init(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinnowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        sellerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SellerRegistration.class);
                startActivity(intent);
            }
        });

        String userphonekey=Paper.book().read(Prevelent.userCurrentPhoneKey);
        String userpasswordkey=Paper.book().read(Prevelent.userCurrentPasswordkey);

        if(userphonekey != null && userpasswordkey != null){
           if(!userphonekey.isEmpty()&&!userpasswordkey.isEmpty()){

               progressDialog.setTitle("Already Logged in");
               progressDialog.setMessage("Please wait, while we are chexking the credentials..");
               progressDialog.setCancelable(false);
               progressDialog.show();

              AllowAccess(userphonekey,userpasswordkey);
           }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent=new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists()){

                    Users userdata= dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone)){
                        if(userdata.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                            Prevelent.currentOnlineUsers=userdata;
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Incorrect phone", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Create a new Account", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

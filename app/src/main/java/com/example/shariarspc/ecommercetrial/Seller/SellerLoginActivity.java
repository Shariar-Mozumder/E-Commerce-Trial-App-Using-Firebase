package com.example.shariarspc.ecommercetrial.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private EditText emailET,passET;
    private Button loginBTN;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        emailET=findViewById(R.id.login_seller_email);
        passET=findViewById(R.id.login_seller_password);
        loginBTN=findViewById(R.id.seller_loginId);

        progressDialog=new ProgressDialog(this);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellerLogin();
                progressDialog.setTitle("Seller Login");
                progressDialog.setMessage("Please wait, Login Seller Account...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    private void sellerLogin() {
        final String email=emailET.getText().toString();
        final String password=passET.getText().toString();

        if(!email.isEmpty()&&!password.isEmpty()){

            final FirebaseAuth mAuth=FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent intent=new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "Incorrect Password Or Email", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(this, "Please Fill up both Email & Password", Toast.LENGTH_SHORT).show();
        }
    }
}

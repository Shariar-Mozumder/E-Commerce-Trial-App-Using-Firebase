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

import com.example.shariarspc.ecommercetrial.Buyer.MainActivity;
import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistration extends AppCompatActivity {

    private Button loginBtn,registerBtn;
    private EditText nameET,phoneET,passwordET,emailET,addressET;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        progressDialog=new ProgressDialog(this);

        loginBtn=findViewById(R.id.already_have_acc);
        registerBtn=findViewById(R.id.registerId);
        nameET=findViewById(R.id.seller_name);
        phoneET=findViewById(R.id.seller_phone);
        passwordET=findViewById(R.id.seller_password);
        emailET=findViewById(R.id.seller_email);
        addressET=findViewById(R.id.seller_address);

        mAuth=FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerRegistration.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Seller Registration");
                progressDialog.setMessage("Please wait, Creating Seller Account...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                registerSeller();
            }
        });
    }

    private void registerSeller() {

        final String name=nameET.getText().toString();
        final String phone=phoneET.getText().toString();
        final String email=emailET.getText().toString();
        final String password=passwordET.getText().toString();
        final String address=addressET.getText().toString();

        if (!name.isEmpty()&&!phone.isEmpty()&&!email.isEmpty()&&!password.isEmpty()&&!address.isEmpty()){

           mAuth.createUserWithEmailAndPassword(email,password)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               String sid=mAuth.getCurrentUser().getUid();
                               final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();

                               HashMap<String,Object> sellersmap=new HashMap<>();
                               sellersmap.put("name",name);
                               sellersmap.put("phone",phone);
                               sellersmap.put("email",email);
                               sellersmap.put("password",password);
                               sellersmap.put("address",address);
                               sellersmap.put("sid",sid);

                               ref.child("Sellers").child(sid).updateChildren(sellersmap)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   progressDialog.dismiss();
                                                   Toast.makeText(SellerRegistration.this, "You are Registered as a seller", Toast.LENGTH_SHORT).show();
                                                   Intent intent=new Intent(SellerRegistration.this, SellerHomeActivity.class);
                                                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                   startActivity(intent);
                                                   finish();
                                               }
                                               else{
                                                   progressDialog.dismiss();
                                                   Toast.makeText(SellerRegistration.this, "Error Occurd, try again!", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });
                           }
                       }
                   });



        }
        else{
            progressDialog.dismiss();
            Toast.makeText(this, "Please Fill up all the fields", Toast.LENGTH_SHORT).show();
        }






    }
}

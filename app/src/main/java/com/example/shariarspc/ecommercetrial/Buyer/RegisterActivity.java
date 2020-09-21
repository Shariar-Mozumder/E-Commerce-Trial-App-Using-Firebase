package com.example.shariarspc.ecommercetrial.Buyer;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createaccountbtn;
    private EditText inputName,inputPhone,inputPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createaccountbtn=findViewById(R.id.register_btn);
        inputName=findViewById(R.id.register_username_input);
        inputPassword=findViewById(R.id.register_password_input);
        inputPhone=findViewById(R.id.register_phone_num_input);
        progressDialog=new ProgressDialog(this);
        createaccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String name=inputName.getText().toString();
        String phone=inputPhone.getText().toString();
        String password=inputPassword.getText().toString();

        if(name.isEmpty()||phone.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Plese Fill up all the fields..", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setTitle("Create account");
            progressDialog.setMessage("Please wait, while we are chexking the credentials..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            validatephoneNumber(name,phone,password);

        }
    }

    private void validatephoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userDataMap=new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("password",password);
                    userDataMap.put("name",name);

                    rootref.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "you have created an Account Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(RegisterActivity.this, "This Phone Number is Already used, Try with a new one.", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

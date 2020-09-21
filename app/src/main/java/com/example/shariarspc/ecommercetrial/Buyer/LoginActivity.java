package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Admin.AdminHomeActivity;
import com.example.shariarspc.ecommercetrial.Seller.SellerCatagoryActivity;
import com.example.shariarspc.ecommercetrial.Model.Users;
import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText inputPhone,inputPassword;
    private Button Loginbtn;
    private ProgressDialog progressDialog;
    private String ParentDBname="Users";
    private CheckBox checkBoxRememberMe;
    private TextView adminlink,notAdmin,forgotPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhone=findViewById(R.id.login_phone_num_input);
        inputPassword=findViewById(R.id.login_password_input);
        Loginbtn=findViewById(R.id.login_btn);
        adminlink=findViewById(R.id.admin_panel_link);
        notAdmin=findViewById(R.id.not_admin_panel_link);
        progressDialog=new ProgressDialog(this);
        checkBoxRememberMe=findViewById(R.id.remember_me_chkb);
        forgotPass=findViewById(R.id.forgot_pass_link);

        Paper.init(this );

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loginbtn.setText("Login Admin");
                adminlink.setVisibility(view.INVISIBLE);
                notAdmin.setVisibility(view.VISIBLE);
                ParentDBname="Admins";


            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Loginbtn.setText("Login");
                adminlink.setVisibility(view.VISIBLE);
                notAdmin.setVisibility(view.INVISIBLE);
                ParentDBname="Users";

            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String phone=inputPhone.getText().toString();
        String password=inputPassword.getText().toString();

        if(phone.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Plese Fill up all the fields..", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.setTitle("Login account");
            progressDialog.setMessage("Please wait, while we are chexking the credentials..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            allowaccesAccount(phone,password);
        }

    }

    private void allowaccesAccount(final String phone, final String password) {
        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevelent.userCurrentPhoneKey,phone);
            Paper.book().write(Prevelent.userCurrentPasswordkey,password);
        }


        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();

        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(ParentDBname).child(phone).exists()){

                    Users userdata= dataSnapshot.child(ParentDBname).child(phone).getValue(Users.class);
                    if(userdata.getPhone().equals(phone)){
                        if(userdata.getPassword().equals(password)){
                            if(ParentDBname.equals("Admins")){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent=new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if(ParentDBname.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                Prevelent.currentOnlineUsers=userdata;
                                startActivity(intent);
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Incorrect phone", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Create a new Account", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

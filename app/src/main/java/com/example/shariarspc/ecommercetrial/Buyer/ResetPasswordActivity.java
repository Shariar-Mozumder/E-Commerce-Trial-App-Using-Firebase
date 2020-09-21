package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check="";
    TextView resetTV,msg2;
    EditText phnET,q1,q2;
    Button verifyBTn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("check");

        resetTV=findViewById(R.id.reset_TV);
        msg2=findViewById(R.id.msg2);
        phnET=findViewById(R.id.phn_ET);
        q1=findViewById(R.id.Q1);
        q2=findViewById(R.id.Q2);
        verifyBTn=findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phnET.setVisibility(View.GONE);
        if(check.equals("settings")){
            resetTV.setText("Set Answers");
            msg2.setText("Please set Answers for the following Questions..");
            verifyBTn.setText("Set");
            displayAnswers();

            verifyBTn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    questionset();

        }
    });
        }
        else if(check.equals("login")){
            resetTV.setText("Reset Password");
            msg2.setText("Please Answer the following Questions....");
            verifyBTn.setText("Verify");
            phnET.setVisibility(View.VISIBLE);

            verifyBTn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyuser();
                }
            });
        }

    }

    private void verifyuser() {


        final String phone=phnET.getText().toString();
        final String question1=q1.getText().toString().toLowerCase();
        final String question2=q2.getText().toString().toLowerCase();
        if(!phone.isEmpty()||!question1.isEmpty()||!question2.isEmpty()) {

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").
                    child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {

                        String mPhone = dataSnapshot.child("phone").getValue().toString();

                        if (dataSnapshot.hasChild("Security Questions")) {
                            String ans1 = dataSnapshot.child("Security Questions").child("Question1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("Question2").getValue().toString();

                            if (!ans1.equals(question1)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your 1st answer is wrong", Toast.LENGTH_SHORT).show();
                            } else if (!ans2.equals(question2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your 2nd answer is wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newpassword = new EditText(ResetPasswordActivity.this);
                                newpassword.setHint("Write new Password here");
                                builder.setView(newpassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!newpassword.getText().toString().equals("")) {
                                            ref.child("password")
                                                    .setValue(newpassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                                                            Intent intent=new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                builder.show();
                            }
                        }

                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "This user Phone Number is not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            Toast.makeText(this, "Please Fill up all the fields", Toast.LENGTH_SHORT).show();
        }


    }

    private void questionset(){
        String question1=q1.getText().toString().toLowerCase();
        String question2=q2.getText().toString().toLowerCase();

        if(question1.isEmpty()||question2.isEmpty()){
            Toast.makeText(ResetPasswordActivity.this, "Please Answer the Both Questions..", Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").
                    child(Prevelent.currentOnlineUsers.getPhone());

            HashMap<String,Object> securityMap=new HashMap<>();
            securityMap.put("Question1",question1);
            securityMap.put("Question2",question2);

            ref.child("Security Questions").updateChildren(securityMap).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "Security Questions updated Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
            );
        }
    }

    private void displayAnswers(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").
                child(Prevelent.currentOnlineUsers.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ans1=dataSnapshot.child("Question1").getValue().toString();
                    String ans2=dataSnapshot.child("Question2").getValue().toString();

                    q1.setText(ans1);
                    q2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

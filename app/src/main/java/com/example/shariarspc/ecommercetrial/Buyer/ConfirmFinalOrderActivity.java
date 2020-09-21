package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText ship_name,ship_phone,ship_district,ship_address;
    private Button confirm_btn;

    private String totalamount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalamount=getIntent().getStringExtra("total amount");

        ship_name=findViewById(R.id.shipment_name);
        ship_phone=findViewById(R.id.shipment_phone);
        ship_district=findViewById(R.id.shipment_district);
        ship_address=findViewById(R.id.shipment_home);
        confirm_btn=findViewById(R.id.confirm_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ship_name.equals("")||ship_address.equals("")||ship_phone.equals("")||ship_district.equals("")){
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please Fill up all the fields carefully!", Toast.LENGTH_SHORT).show();
                }
                else{
                    confirmingOrder();
                }
            }
        });
    }

    private void confirmingOrder() {

        String saveCurrentTime,saveCurrentDate;

        Calendar callfordate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate=currentDate.format(callfordate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(callfordate.getTime());

        final DatabaseReference orderref= FirebaseDatabase.getInstance().getReference().child("Orders");

        final HashMap<String,Object> orderMap=new HashMap<>();
        orderMap.put("name",ship_name.getText().toString());
        orderMap.put("phone",ship_phone.getText().toString());
        orderMap.put("district",ship_district.getText().toString());
        orderMap.put("address",ship_address.getText().toString());
        orderMap.put("totalAmount", totalamount);
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");

        orderref.child(Prevelent.currentOnlineUsers.getPhone()).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevelent.currentOnlineUsers.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Toast.makeText(ConfirmFinalOrderActivity.this, "Your Final Orders has been palced successfully", Toast.LENGTH_SHORT).show();

                                       Intent intent =new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(intent);
                                       finish();
                                   }
                                }
                            });
                }
            }
        });

    }
}

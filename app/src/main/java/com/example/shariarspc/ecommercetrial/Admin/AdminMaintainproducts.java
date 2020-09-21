package com.example.shariarspc.ecommercetrial.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.Seller.SellerCatagoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainproducts extends AppCompatActivity {

    private EditText maintainPname,maintainPprice,maintainPdesc;
    private Button apply_changes_btn,delete_btn;
    private ImageView maintainImage;
    private String ProductID="";
    private DatabaseReference productref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintainproducts);

        ProductID=getIntent().getStringExtra("pid");
        productref= FirebaseDatabase.getInstance().getReference().child("Products").child(ProductID);

        maintainPname=findViewById(R.id.maintain_p_name);
        maintainPprice=findViewById(R.id.maintain_P_price);
        maintainPdesc=findViewById(R.id.maintain_Pdescription);
        maintainImage=findViewById(R.id.maintain_pimage);
        apply_changes_btn=findViewById(R.id.apply_changes_btn);
        delete_btn=findViewById(R.id.delete_btn);

        displaySpecificProducts();

        apply_changes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pname=maintainPname.getText().toString();
                String pprice=maintainPprice.getText().toString();
                String pdescription=maintainPdesc.getText().toString();

                if(pname.isEmpty()||pprice.isEmpty()||pdescription.isEmpty()){
                    Toast.makeText(AdminMaintainproducts.this, "WriteDown the all the Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    HashMap<String,Object> productMap=new HashMap<>();
                    productMap.put("pid",ProductID);
                    productMap.put("pname",pname);
                    productMap.put("pprice",pprice);
                    productMap.put("pdescription",pdescription);

                    productref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(AdminMaintainproducts.this, "Changed Applied Successfully", Toast.LENGTH_SHORT).show();

                               Intent intent=new Intent(AdminMaintainproducts.this, SellerCatagoryActivity.class);
                               startActivity(intent);
                               finish();
                           }
                        }
                    });
                }


            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteproduct();
            }
        });


    }

    private void deleteproduct() {
        productref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminMaintainproducts.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AdminMaintainproducts.this, SellerCatagoryActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void displaySpecificProducts() {

        productref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name=dataSnapshot.child("pname").getValue().toString();
                    String price=dataSnapshot.child("pprice").getValue().toString();
                    String description=dataSnapshot.child("pdescription").getValue().toString();
                    String image=dataSnapshot.child("pimage").getValue().toString();

                    maintainPname.setText(name);
                    maintainPprice.setText(price);
                    maintainPdesc.setText(description);
                    Picasso.get().load(image).into(maintainImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

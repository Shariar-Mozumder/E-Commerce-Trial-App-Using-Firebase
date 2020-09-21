package com.example.shariarspc.ecommercetrial.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {

    private String catagory,pNmae,pDescription,pPrice,saveCurrentTime,saveCurrentDate;
    private Button AddnewProductButton;
    private ImageView inputProductImage;
    private EditText inputProductName,inputProductPrice,inputProductDecription;
    private static final int GALLERY_PICK=1;
    private Uri imageUri;
    private String productrandomKey,downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productref,sellerref;
    private ProgressDialog progressDialog;

    private String sNmae,sEmail,sAddress,sPhoNE,sid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

        catagory=getIntent().getExtras().get("catagory").toString();
        productImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productref= FirebaseDatabase.getInstance().getReference().child("Products");
        sellerref= FirebaseDatabase.getInstance().getReference().child("Sellers");


        AddnewProductButton=findViewById(R.id.add_new_product);
        inputProductImage=findViewById(R.id.select_product_image);
        inputProductName=findViewById(R.id.product_name);
        inputProductDecription=findViewById(R.id.product_description);
        inputProductPrice=findViewById(R.id.product_price);

        progressDialog=new ProgressDialog(this);

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        AddnewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validInformation();
            }
        });


        sellerref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            sNmae=dataSnapshot.child("name").getValue().toString();
                            sPhoNE=dataSnapshot.child("phone").getValue().toString();
                            sEmail=dataSnapshot.child("email").getValue().toString();
                            sAddress=dataSnapshot.child("address").getValue().toString();
                            sid=dataSnapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void validInformation() {
        pNmae=inputProductName.getText().toString();
        pDescription=inputProductDecription.getText().toString();
        pPrice=inputProductPrice.getText().toString();

        if(imageUri==null){
            Toast.makeText(this, "Please Select a product Image", Toast.LENGTH_SHORT).show();
        }
        else if(pNmae.isEmpty()||pDescription.isEmpty()||pPrice.isEmpty()){
            Toast.makeText(this, "Pleae Fill up All the Fields", Toast.LENGTH_SHORT).show();
        }
        else{
            storeproductInformation();
        }
    }

    private void storeproductInformation() {
        progressDialog.setTitle("Adding new product");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productrandomKey=saveCurrentDate + saveCurrentTime;

        final StorageReference filepath=productImagesRef.child(imageUri.getLastPathSegment()+productrandomKey+".jpg");
        final UploadTask uploadTask=filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "error:"+msg, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "product image uploaded Succeessfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product Image url got successfully", Toast.LENGTH_SHORT).show();

                            saveProductinfotodatabase();
                        }
                    }
                });
            }

            private void saveProductinfotodatabase() {
                HashMap<String,Object> productmap=new HashMap<>();
                productmap.put("pcatagory",catagory);
                productmap.put("pname",pNmae);
                productmap.put("pdescription",pDescription);
                productmap.put("pprice",pPrice);
                productmap.put("pid",productrandomKey);
                productmap.put("pdate",saveCurrentDate);
                productmap.put("ptime",saveCurrentTime);
                productmap.put("pimage",downloadImageUrl);


                productmap.put("sellerName",sNmae);
                productmap.put("sellerPhone",sPhoNE);
                productmap.put("sellerEmail",sEmail);
                productmap.put("sellerAddress",sAddress);
                productmap.put("sid",sid);
                productmap.put("productState","Not Approved");

                productref.child(productrandomKey).updateChildren(productmap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SellerAddNewProductActivity.this, "Product uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    progressDialog.dismiss();
                                    String msg=task.getException().toString();
                                    Toast.makeText(SellerAddNewProductActivity.this, "Error: "+msg, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });



    }

    private void openGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK&& resultCode==RESULT_OK&& data!=null){
            imageUri=data.getData();
            inputProductImage.setImageURI(imageUri);

        }
    }
}

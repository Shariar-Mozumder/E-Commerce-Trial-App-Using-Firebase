package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView close,update,change_pic;
    private EditText set_Phone,set_name,set_address;

    private Button securityBtn;

    Uri imageUri;
    String myUrl="";
    StorageReference storageProfilePicRef;
    String checker="";
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        circleImageView=findViewById(R.id.profile_pic_settings);
        close=findViewById(R.id.close_id);
        update=findViewById(R.id.update_id);
        change_pic=findViewById(R.id.change_photo_btn);
        set_Phone=findViewById(R.id.settings_phone_number);
        set_name=findViewById(R.id.settings_full_name);
        set_address=findViewById(R.id.settings_address);
        securityBtn=findViewById(R.id.security_btn);

        storageProfilePicRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        userInfoDisplay( circleImageView,set_Phone,set_name,set_address);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked")){
                    saveuserinfoall();
                }
                else{
                    updateonlyInfo();
                }
            }
        });

        change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checker="clicked";
                CropImage.activity(imageUri)
                        .start(SettingsActivity.this);
            }
        });

        securityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });
    }

    private void updateonlyInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",set_name.getText().toString());
        userMap.put("address",set_address.getText().toString());
        userMap.put("phoneOrder",set_Phone.getText().toString());

        ref.child(Prevelent.currentOnlineUsers.getPhone()).updateChildren(userMap);



        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void saveuserinfoall() {
        if(set_Phone.getText().toString().isEmpty()|| set_name.getText().toString().isEmpty()|| set_address.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Fill up all the Fields", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please Wait, Updating Account Info...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference fileref=storageProfilePicRef
                    .child(Prevelent.currentOnlineUsers.getPhone()+".jpg");

            uploadTask=fileref.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation(){

                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }


                    return fileref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadurl= (Uri) task.getResult();
                        myUrl=downloadurl.toString();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",set_name.getText().toString());
                        userMap.put("address",set_address.getText().toString());
                        userMap.put("phoneOrder",set_Phone.getText().toString());
                        userMap.put("image",myUrl);

                        ref.child(Prevelent.currentOnlineUsers.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                circleImageView.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error: "+error, Toast.LENGTH_SHORT).show();

                startActivity(new Intent( SettingsActivity.this,SettingsActivity.class));
                finish();
            }
        }
    }

    private void userInfoDisplay(final CircleImageView circleImageView, final EditText set_phone, final EditText set_name, final EditText set_address) {

        DatabaseReference userref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.currentOnlineUsers.getPhone());

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){
                        String image=dataSnapshot.child("image").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String phone=dataSnapshot.child("phone").getValue().toString();
                        String address=dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(circleImageView);
                        set_phone.setText(phone);
                        set_name.setText(name);
                        set_address.setText(address );

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

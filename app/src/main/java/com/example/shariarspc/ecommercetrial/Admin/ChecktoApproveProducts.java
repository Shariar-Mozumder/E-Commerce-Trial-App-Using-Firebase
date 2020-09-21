package com.example.shariarspc.ecommercetrial.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Interface.ItemClickListener;
import com.example.shariarspc.ecommercetrial.Model.Products;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ChecktoApproveProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private  DatabaseReference unApprovedProduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkto_approve_products);

        recyclerView=findViewById(R.id.product_to_approve_list);
        recyclerView.hasFixedSize();
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        unApprovedProduct= FirebaseDatabase.getInstance().getReference().child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unApprovedProduct.orderByChild("productState").equalTo("Not Approved"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {


                productViewHolder.txt_pname.setText(products.getPname());
                productViewHolder.txt_pDescription.setText(products.getPdescription());
                productViewHolder.txt_price.setText("Price= "+products.getPprice()+" BDT");

                Picasso.get().load(products.getPimage()).into(productViewHolder.productimage);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String productID=products.getPid();

                        CharSequence options[]=new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(ChecktoApproveProducts.this);
                        builder.setTitle("Do you want to Approve this Product? Are You Sure?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){

                                    approvingProduct(productID);
                                }
                                else if(i==1){

                                }
                            }
                        });

                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                ProductViewHolder productViewHolder=new ProductViewHolder(view);

                return productViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void approvingProduct(String productID) {
        unApprovedProduct.child(productID).child("productState").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ChecktoApproveProducts.this, "Product Approved Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

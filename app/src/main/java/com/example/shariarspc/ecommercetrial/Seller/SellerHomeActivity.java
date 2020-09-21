package com.example.shariarspc.ecommercetrial.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shariarspc.ecommercetrial.Buyer.MainActivity;
import com.example.shariarspc.ecommercetrial.Model.Products;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.ViewHolder.ProductViewHolder;
import com.example.shariarspc.ecommercetrial.ViewHolder.SellerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unApprovedProduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
       BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        recyclerView=findViewById(R.id.unvarified_product_list);
        recyclerView.hasFixedSize();
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        unApprovedProduct= FirebaseDatabase.getInstance().getReference().child("Products");

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


     //  AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
           //     R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                //.build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
      //  NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unApprovedProduct.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();

        FirebaseRecyclerAdapter<Products, SellerViewHolder> adapter=new FirebaseRecyclerAdapter<Products, SellerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerViewHolder sellerViewHolder, int i, @NonNull final Products products) {

                sellerViewHolder.txtPname.setText(products.getPname());
                sellerViewHolder.txtPdesc.setText(products.getPdescription());
                sellerViewHolder.txtPprice.setText("Price= "+products.getPprice()+"BDT");
                sellerViewHolder.txtPstate.setText(products.getProductState());
                Picasso.get().load(products.getPimage()).into(sellerViewHolder.proImage);

                sellerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options[]=new CharSequence[]{
                          "Yes",
                          "No"
                        };

                        AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Do You Want to delete this Product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String ProductID=products.getPid();
                                if(i==0){
                                    deleteproduct(ProductID);
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
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_products_design,parent,false);
                SellerViewHolder sellerViewHolder=new SellerViewHolder(view);

                return sellerViewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteproduct(String productID) {
        unApprovedProduct.child(productID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SellerHomeActivity.this, "Product has been deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {




            if ((menuItem.getItemId()==R.id.navigation_home)){
                return true;
            }
            else if(menuItem.getItemId()==R.id.navigation_add){

                Intent intent=new Intent(SellerHomeActivity.this, SellerCatagoryActivity.class);
                startActivity(intent);
                return true;
            }
            else if(menuItem.getItemId()==R.id.navigation_logout){

                final FirebaseAuth mAuth;
                mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent=new Intent(SellerHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;

            }

            return false;
        }
    };

}

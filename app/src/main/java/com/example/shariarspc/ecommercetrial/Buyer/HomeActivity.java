package com.example.shariarspc.ecommercetrial.Buyer;

import android.content.Intent;
import android.os.Bundle;

import com.example.shariarspc.ecommercetrial.Admin.AdminMaintainproducts;
import com.example.shariarspc.ecommercetrial.Model.Products;
import com.example.shariarspc.ecommercetrial.Prevelent.Prevelent;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    ActionBarDrawerToggle toggle;
    private DatabaseReference productref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private  String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            type=getIntent().getExtras().get("Admin").toString();
        }



        Paper.init(this);

        productref= FirebaseDatabase.getInstance().getReference().child("Products");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle=new ActionBarDrawerToggle(this,drawer,R.string.nav_open,R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
      //  NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
      //  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
      //  NavigationUI.setupWithNavController(navigationView, navController);


        View headerView=navigationView.getHeaderView(0);
        TextView usernameTv=headerView.findViewById(R.id.user_profile_name);
        CircleImageView userimage=headerView.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin")){
            usernameTv.setText(Prevelent.currentOnlineUsers.getName());
            Picasso.get().load(Prevelent.currentOnlineUsers.getImage()).placeholder(R.drawable.profile).into(userimage);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productref.orderByChild("productState").equalTo("Approved"),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        productViewHolder.txt_pname.setText(products.getPname());
                        productViewHolder.txt_pDescription.setText(products.getPdescription());
                        productViewHolder.txt_price.setText("Price= "+products.getPprice()+" BDT");

                        Picasso.get().load(products.getPimage()).into(productViewHolder.productimage);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(type.equals("Admin")){

                                    Intent intent=new Intent(HomeActivity.this, AdminMaintainproducts.class);
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent=new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);
                                }

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);

                         ProductViewHolder holder=new ProductViewHolder(view);
                         return holder;
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
       int id=menuItem.getItemId();

       if(id==R.id.nav_cart){
           if(!type.equals("Admin")) {
               Intent intent=new Intent(HomeActivity.this,CartActivity.class);
               startActivity(intent);
           }

       }
       else if(id==R.id.nav_search){
           if(!type.equals("Admin")){
               Intent intent=new Intent(HomeActivity.this, SearchproductsActivity.class);
               startActivity(intent);
           }



       }
       else if(id==R.id.nav_catagories){
           if(!type.equals("Admin")){

           }

       }
       else if(id==R.id.nav_settings){

           if(!type.equals("Admin")){
               Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
               startActivity(intent);
           }

       }
       else if(id==R.id.nav_logout){
           if(!type.equals("Admin")){
               Paper.book().destroy();
               Intent intent=new Intent(HomeActivity.this, MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
           }

       }
       else if(id==R.id.nav_share){

       }
       else if(id==R.id.nav_contact){

       }

        return false;
    }
}

package com.example.shariarspc.ecommercetrial.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shariarspc.ecommercetrial.Model.Products;
import com.example.shariarspc.ecommercetrial.R;
import com.example.shariarspc.ecommercetrial.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchproductsActivity extends AppCompatActivity {

    private EditText searchET;
    private Button searchbtn;
    private RecyclerView searchList;

    private String searchinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchproducts);

        searchET=findViewById(R.id.searchEditText);
        searchbtn=findViewById(R.id.searchBtn);
        searchList=findViewById(R.id.search_LIST);
        searchList.setLayoutManager(new LinearLayoutManager(SearchproductsActivity.this));

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchinput=searchET.getText().toString();
                onStart();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference productref= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(productref.orderByChild("pname").startAt(searchinput),Products.class).build();

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
                                Intent intent=new Intent(SearchproductsActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid",products.getPid());
                                startActivity(intent);
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

        searchList.setAdapter(adapter);
        adapter.startListening();

    }
}

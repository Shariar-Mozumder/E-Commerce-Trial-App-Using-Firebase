package com.example.shariarspc.ecommercetrial.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shariarspc.ecommercetrial.Interface.ItemClickListener;
import com.example.shariarspc.ecommercetrial.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    public TextView txtProductName,txtProductQuantity,txtProductPrice;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

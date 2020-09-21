package com.example.shariarspc.ecommercetrial.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shariarspc.ecommercetrial.Interface.ItemClickListener;
import com.example.shariarspc.ecommercetrial.R;

public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtPname,txtPdesc,txtPprice,txtPstate;
    public ImageView proImage;
    public ItemClickListener listener;

    public SellerViewHolder(@NonNull View itemView) {
        super(itemView);

        txtPname=itemView.findViewById(R.id.seller_p_name);
        txtPdesc=itemView.findViewById(R.id.seller_Pdescription);
        txtPprice=itemView.findViewById(R.id.seller_P_price);
        txtPstate=itemView.findViewById(R.id.product_state);
        proImage=itemView.findViewById(R.id.seller_pimage);
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {

        listener.onClick(view,getAdapterPosition(),false);
    }
}

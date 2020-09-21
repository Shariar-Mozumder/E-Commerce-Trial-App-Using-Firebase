package com.example.shariarspc.ecommercetrial.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shariarspc.ecommercetrial.Interface.ItemClickListener;
import com.example.shariarspc.ecommercetrial.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_pname,txt_pDescription,txt_price;
    public ImageView productimage;
    ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_pname=itemView.findViewById(R.id.p_name);
        txt_pDescription=itemView.findViewById(R.id.Pdescription);
        txt_price=itemView.findViewById(R.id.P_price);
        productimage=itemView.findViewById(R.id.pimage);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}

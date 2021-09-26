package com.example.sa7tekprojectf.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa7tekprojectf.ItemClickLiestener;
import com.example.sa7tekprojectf.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice;
    public ImageView imageView;
    public ItemClickLiestener itemClickLiestener;
    private ItemClickLiestener listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.product_image);
        txtProductName=(TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice=(TextView) itemView.findViewById(R.id.product_price);
    }
    public void setItemClickListener(ItemClickLiestener listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
    listner.onClick(view,getAdapterPosition(),false);
    }
}

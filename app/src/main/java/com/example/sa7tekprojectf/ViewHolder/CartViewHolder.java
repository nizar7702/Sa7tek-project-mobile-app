package com.example.sa7tekprojectf.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sa7tekprojectf.ItemClickLiestener;
import com.example.sa7tekprojectf.R;

public class CartViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    private ItemClickLiestener itemClickLiestener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View v) {
     itemClickLiestener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickLiestener(ItemClickLiestener itemClickLiestener) {
        this.itemClickLiestener = itemClickLiestener;
    }
}

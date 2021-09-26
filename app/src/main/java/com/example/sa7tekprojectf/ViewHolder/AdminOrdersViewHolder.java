package com.example.sa7tekprojectf.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sa7tekprojectf.ItemClickLiestener;
import com.example.sa7tekprojectf.R;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAdress;
    public Button ShowOrdersBtn;
    private ItemClickLiestener itemClickLiestener;

    public AdminOrdersViewHolder(View itemView){
        super(itemView);


        userName=itemView.findViewById(R.id.order_user_name);
        userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
        userTotalPrice=itemView.findViewById(R.id.order_total_price);
        userDateTime=itemView.findViewById(R.id.order_date_time);
        userShippingAdress=itemView.findViewById(R.id.order_address_city);
        ShowOrdersBtn=itemView.findViewById(R.id.show_all_products_btn1);
    }

    @Override
    public void onClick(View v) {
        itemClickLiestener.onClick(v,getAdapterPosition(),false);}
        public void setItemClickLiestener(ItemClickLiestener itemClickLiestener ){
            this.itemClickLiestener = itemClickLiestener;
        }
}
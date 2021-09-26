package com.example.sa7tekprojectf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,adressEditText,cityEditText;
    private Button confirmOrderBtn;
    private String totalAmount="";
    private String User_Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price on millm= "+totalAmount, Toast.LENGTH_SHORT).show();
        confirmOrderBtn=(Button)findViewById(R.id.confirmfinal_order_btn);
        nameEditText=(EditText) findViewById(R.id.shippment_name);
        phoneEditText=(EditText) findViewById(R.id.shippment_phone_number);
        adressEditText=(EditText) findViewById(R.id.shippment_adress);
        cityEditText=(EditText) findViewById(R.id.shippment_city);
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        User_Id=sp1.getString("User_Id", null);
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }
    private void Check(){
       if(TextUtils.isEmpty((nameEditText.getText().toString()))){
           Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
           Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(adressEditText.getText().toString())){
           Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(cityEditText.getText().toString())){
           Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
       }
       else{
           ConfirmOrder();
       }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currrentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(User_Id);
        HashMap<String,Object> ordersMap=new HashMap<>();
        ordersMap.put("total",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("adress",adressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(User_Id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfully", Toast.LENGTH_SHORT).show();
                               Intent intent=new Intent(ConfirmFinalOrderActivity.this,Home_Activity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                               finish();
                           }
                       }
                   });
               }
            }
        });

    }
}
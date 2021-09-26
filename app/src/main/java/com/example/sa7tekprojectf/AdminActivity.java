package com.example.sa7tekprojectf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {
    private Button Add_medicament,Logout_btn,Orders_btn,maintainProductsBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Logout_btn=findViewById(R.id.admin_logout_btn);
        Add_medicament=findViewById(R.id.Add_medicament);
        Orders_btn=findViewById(R.id.Check_orders_btn);
        maintainProductsBtn=findViewById(R.id.maintain_btn);
        Add_medicament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,Add_medicament_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        Logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AdminNewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,Home_Activity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
                finish();
            }
        });

    }
}
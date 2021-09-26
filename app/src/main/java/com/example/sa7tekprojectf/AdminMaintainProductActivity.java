package com.example.sa7tekprojectf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {
    private Button applychangesBtn;
    private EditText name,price,description;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference productsRef;
    private SharedPreferences sp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);
        productID=getIntent().getStringExtra("pid");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        applychangesBtn=findViewById(R.id.apply_changes_btn);
        name=findViewById(R.id.product_name_maintain);
        price=findViewById(R.id.product_price_maintain);
        imageView=findViewById(R.id.product_image_maintain);
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.clear();
        Ed.commit();



        displaySpecificProductInfo();
        applychangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               applyChanges();
            }
        });
    }

    private void applyChanges() {
        String pName=name.getText().toString();
        String pPrice=price.getText().toString();
        if(pName.equals("")){
            Toast.makeText(this, "Write down Product Name", Toast.LENGTH_SHORT).show();
        }
        else if(pPrice.equals("")){
            Toast.makeText(this, "Write down Product Price", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }
                    
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
      productsRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists()){
                 String pName=snapshot.child("pname").getValue().toString();
                 String pPrice=snapshot.child("price").getValue().toString();
                 String pImage=snapshot.child("image").getValue().toString();

                 name.setText(pName);
                 price.setText(pPrice);
                 Picasso.get().load(pImage).into(imageView);





             }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }
}
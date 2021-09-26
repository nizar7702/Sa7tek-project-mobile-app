package com.example.sa7tekprojectf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private TextView button1;
    private Button loginb;
    private EditText password, email;
    private String User_Name,User_Email,User_Password,image;
    private String parentDbName = "Users";
    private  Users usersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        initViews();
        button1 = findViewById(R.id.not_registred);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        loginb = findViewById(R.id.loginbtn);
        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1= email.getText().toString();
                String password1 = password.getText().toString();
                oninitregister(email1, password1);
            }
        });
    }

    private void oninitregister(String email1, String password1) {
        if (validatedata(email1, password1)) {
            showsnackbar(email1, password1);
        }
    }

    private void showsnackbar(String email1, String password1) {
        FirebaseAuth mauth;
        Log.d(email1, "email");
        Log.d(password1, "password");
        if (email1.equals("nizar.bousabat2000@gmail.com") && password1.equals("02052000")) {
            Toast.makeText(MainActivity.this, "login admin successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(i);
        } else {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            mauth = FirebaseAuth.getInstance();
            mauth.signInWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference uidRef = rootRef.child(parentDbName).child(uid);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                image=dataSnapshot.child("image").getValue(String.class);
                                User_Name= dataSnapshot.child("name").getValue(String.class);
                                User_Email= dataSnapshot.child("email").getValue(String.class);
                                User_Password= dataSnapshot.child("password").getValue(String.class);
                                SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                                SharedPreferences.Editor Ed=sp.edit();
                                Log.d(String.valueOf(Ed), "preference before: ");
                                Ed.clear();
                                Ed.commit();
                                Log.d(String.valueOf(Ed), "preference after: ");
                                Ed.putString("Image",image);
                                Ed.putString("User_Id",uid);
                                Ed.putString("User_Name",User_Name);
                                Ed.putString("User_Email",User_Email);
                                Ed.putString("Password",User_Password);
                                Ed.commit();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        };

                        uidRef.addListenerForSingleValueEvent(eventListener);
                        Toast.makeText(MainActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Home_Activity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "login failed!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
        public void openActivity2(){
        Intent intent=new Intent(this,register_activity.class);
        startActivity(intent);
    }
    private boolean validatedata(String name1,String password1){

        if(name1.isEmpty()){
            email.setError("email is required");
            email.requestFocus();
            return false;
        }
        if(password1.isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            return false;
        }
        return true;
    }
    private void initViews(){
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
    }
}
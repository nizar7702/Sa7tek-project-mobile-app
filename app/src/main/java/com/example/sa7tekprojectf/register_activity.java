package com.example.sa7tekprojectf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register_activity extends AppCompatActivity {

    private TextView namevalid,emailvalid,passwordvalid,repasswordvalid;
    private EditText name,email,password,reenterpassword;
    private ConstraintLayout parent;
    private Button buttonreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        initViews();
        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String email1=email.getText().toString().trim();
                String password1=password.getText().toString().trim();
                String name1=name.getText().toString().trim();
                String repassword1=reenterpassword.getText().toString().trim();
                oninitregister(name1,email1,password1,repassword1);
            }
        });
    }
    private void oninitregister(String name1,String email1,String password1,String repassword1){
        if(validatedata(name1,email1,password1,repassword1)){
            showsnackbar(name1,email1,password1);}}

    private void showsnackbar(String name1,String email1,String password1){
        FirebaseAuth mauth;
        Log.d("myTag",email1+password1+name1+"");
        mauth= FirebaseAuth.getInstance();
        mauth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                         @Override
                                                                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                             if (task.isSuccessful()){
                                                                                                 Users users = new Users(name1,email1,password1,"no photo");
                                                                                                 FirebaseDatabase.getInstance().getReference("Users")
                                                                                                         .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                                         .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                     @Override
                                                                                                     public void onComplete(@NonNull Task<Void> task) {
                                                                                                         if(task.isSuccessful()){
                                                                                                             Toast.makeText(register_activity.this, "User registred successfully", Toast.LENGTH_SHORT).show();
                                                                                                             Intent intent=new Intent(register_activity.this,MainActivity.class);
                                                                                                             startActivity(intent);
                                                                                                         }else{
                                                                                                             Toast.makeText(register_activity.this, "register failed!!", Toast.LENGTH_SHORT).show();
                                                                                                         }
                                                                                                     }
                                                                                                 });
                                                                                             }
                                                                                         }
                                                                                     }
        );
    }
    private boolean validatedata(String name,String email,String password,String repassword){
        if(name.isEmpty()){
            namevalid.setError("name is required");
            namevalid.requestFocus();
            return false;
        }
        if(email.isEmpty()){
            emailvalid.setError("email is required");
            emailvalid.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            passwordvalid.setError("password is required");
            passwordvalid.requestFocus();
            return false;
        }
        if(repassword.isEmpty()||(password.equals(repassword)==false)){
            reenterpassword.setError("repassword is required or false");
            reenterpassword.requestFocus();
            return false;
        }
        return true;
    }
    private void initViews(){
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        reenterpassword=findViewById(R.id.reenterpassword);
        buttonreg=findViewById(R.id.buttonreg);
        namevalid=findViewById(R.id.namevalid);
        emailvalid=findViewById(R.id.emailvalid);
        passwordvalid=findViewById(R.id.passwordvalid);
        repasswordvalid=findViewById(R.id.repasswordvalid);
        parent=findViewById(R.id.parent);
    }
}
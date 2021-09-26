package com.example.sa7tekprojectf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText NameEditText,EmailEditText,PasswordEditText;
    private TextView profileChangeTextBtn,closeTextBtn,saveTextButton;
    private String User_Id;
    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfillePictureRef;
    private String checker="";
    private SharedPreferences sp1;
    private String User_Name;
    private String User_Email;
    private String User_Password;
    private SharedPreferences.Editor Ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageProfillePictureRef= FirebaseStorage.getInstance().getReference().child("Profile pictures");
        profileImageView=findViewById(R.id.settings_profile_image);
        NameEditText=findViewById(R.id.settings_name);
        profileChangeTextBtn=findViewById(R.id.profile_image_change_btn);
        closeTextBtn=findViewById(R.id.close_settings_btn);
        saveTextButton=findViewById(R.id.update_account_settings_btn);
        sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        Ed=sp1.edit();
        User_Id=sp1.getString("User_Id", null);

        User_Name=sp1.getString("User_Name", null);
        Log.d(User_Name, "username mawjoud: ");
        User_Email=sp1.getString("User_Email", null);
        User_Password=sp1.getString("User_Password", null);

        userInfoDisplay(profileImageView,NameEditText,EmailEditText,PasswordEditText);
        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();

                }else{
                   updateOnlyUserInfo();
                }
            }
        });
        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUri).setAspectRatio(1,1).start(Settings.this);
            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap=new HashMap<>();
        userMap.put("name",NameEditText.getText().toString());
        ref.child(User_Id).updateChildren(userMap);

        startActivity(new Intent(Settings.this,Home_Activity.class));
        Toast.makeText(Settings.this, " Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            imageUri=result.getUri();

            profileImageView.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Error,Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this,Settings.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if(TextUtils.isEmpty(NameEditText.getText().toString())){
            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }




    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Infos");
        progressDialog.setMessage("Please wait,while we are updating your acount informations");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if(imageUri!=null){
           final StorageReference fileRef=storageProfillePictureRef.child(User_Id+".jpg");
           uploadTask=fileRef.putFile(imageUri);
           uploadTask.continueWithTask(new Continuation() {
               @Override
               public Object then(@NonNull Task task) throws Exception {

                   if(!task.isSuccessful()){
                       throw task.getException();
                   }
                   return fileRef.getDownloadUrl();
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       Uri downloadUrl=task.getResult();
                       myUrl=downloadUrl.toString();

                       DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                       HashMap<String, Object> userMap=new HashMap<>();
                       userMap.put("name",NameEditText.getText().toString());
                       userMap.put("image",myUrl);
                       ref.child(User_Id).updateChildren(userMap);
                       Ed.putString("Image",myUrl);
                       Ed.putString("User_Name",NameEditText.getText().toString());
                       Ed.commit();
                       progressDialog.dismiss();
                       startActivity(new Intent(Settings.this,Home_Activity.class));
                       Toast.makeText(Settings.this, " Info update successfully.", Toast.LENGTH_SHORT).show();
                       finish();}else{
                       Toast.makeText(Settings.this, "Error.", Toast.LENGTH_SHORT).show();


                   }
               }
           });
        }else{
            Toast.makeText(this, "image is not sellected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText nameEditText, EditText emailEditText, EditText passwordEditText) {
        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getInstance().getReference().child("Users").child(User_Id);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  if(snapshot.child("image").exists()){
                     String image=snapshot.child("image").getValue().toString();
                     String name=snapshot.child("name").getValue().toString();

                      Picasso.get().load(image).into(profileImageView);
                      NameEditText.setText(name);
                  }
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
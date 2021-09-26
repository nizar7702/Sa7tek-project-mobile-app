package com.example.sa7tekprojectf;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.sa7tekprojectf.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
public class Home_Activity extends AppCompatActivity {
    private TextView username;
    private ImageView userimage;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String User_Name;
    private String User_Image;
    private String type="";
    private SharedPreferences sp1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            type=getIntent().getExtras().get("Admin").toString();
        }
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        username=findViewById(R.id.user_profile_name);
        userimage=findViewById(R.id.profile_image);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
        String User_Name=sp1.getString("User_Name", null);
        Log.d(User_Name, "User_Name: ");
        String User_Email= sp1.getString("User_Email", null);
        String User_Image= sp1.getString("Image", null);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!type.equals("Admin")){

                    Intent intent=new Intent(Home_Activity.this,CartActivity.class);
                    startActivity(intent);

                }



            }
        });
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        if(!type.equals("Admin")){
        username.setText("*** Welcome "+String.valueOf(User_Name)+"***");}
        Picasso.get().load(User_Image).placeholder(R.drawable.profile).into(userimage);
        recyclerView=findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef,Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price="+model.getPrice()+"millm");
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(type.equals("Admin")){
                                    Intent intent =new Intent(Home_Activity.this,AdminMaintainProductActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }else{
                                Intent intent =new Intent(Home_Activity.this,ProductDetailsActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);}
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                        ProductViewHolder holder =new ProductViewHolder(view);
                        return holder;
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(Home_Activity.this,Settings.class);
            startActivity(intent);

        } else if (id == R.id.action_logout) {
            SharedPreferences.Editor Ed=sp1.edit();
            Log.d(String.valueOf(Ed), "preference before: ");
            Ed.clear();
            Ed.commit();
            Intent intent=new Intent(Home_Activity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
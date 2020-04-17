package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.foodapp.Interface.ItemClickListener;
import com.example.foodapp.Model.CartItem;
import com.example.foodapp.Model.FoodItem;
import com.example.foodapp.ViewHolder.FoodItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;



public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btn_logout;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference userref;
    private DatabaseReference foodref;
    private DatabaseReference cartref;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
//    private GoogleSignInClient mGoogleSignInClient;
    private FloatingActionButton mCartButton;
    user current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        firebaseAuth=FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getInstance().getCurrentUser();
                if(user==null){
                    startActivity(new Intent(Main2Activity.this, MainActivity.class));
                    finish();
                }
            }
        };

        userref= FirebaseDatabase.getInstance().getReference("user").child(firebaseAuth.getInstance().getCurrentUser().getUid().toString());
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_user = dataSnapshot.getValue(user.class);

                if(current_user.Role.equals("Rider")){
                    gotorider();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mCartButton = findViewById(R.id.cart_btn);

        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, CartMainActivity.class));
            }
        });

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.openNavDrawer,
                R.string.closeNavDrawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
<<<<<<< Updated upstream
        navigationView.setNavigationItemSelectedListener( this);
=======
        navigationView.setNavigationItemSelectedListener(this);
>>>>>>> Stashed changes
        navigationView.setCheckedItem(R.id.nav_home);

        foodref=FirebaseDatabase.getInstance().getReference().child("food_menu");

        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Main2Activity.this);
        recyclerView.setLayoutManager(layoutManager);

        cartref = FirebaseDatabase.getInstance().getReference("Cart");

    }

    private void gotorider(){
        startActivity(new Intent(Main2Activity.this, RiderMainActivity.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.profile_nav:
                Intent intent = new Intent(Main2Activity.this,  ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
//                mGoogleSignInClient.signOut();
                startActivity(new Intent(Main2Activity.this, MainActivity.class));
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);

        FirebaseRecyclerOptions<FoodItem> options = new FirebaseRecyclerOptions.Builder<FoodItem>().setQuery(foodref, FoodItem.class).build();
        final FirebaseRecyclerAdapter<FoodItem, FoodItemViewHolder> adapter =
                new FirebaseRecyclerAdapter<FoodItem, FoodItemViewHolder>(options) {

                    private ItemClickListener listener;
                    @Override
                    protected void onBindViewHolder(@NonNull final FoodItemViewHolder holder, final int position, @NonNull FoodItem model) {

                        holder.mFoodItemName.setText(model.getName());
                        holder.mFoodItemPrice.setText("Price: "+model.getBase_price());
                        holder.mAddToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.mAddToCart.setEnabled(false);

//                                Log.e(position);
                                addToCart(getRef(position).getKey());

                                //Toast.makeText(MainActivity.this,getRef(position).getKey(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout,parent,false);
                        FoodItemViewHolder holder = new FoodItemViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void addToCart(final String ref){
        DatabaseReference foodItemRef = foodref.child(ref);
        foodItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FoodItem addedItem = dataSnapshot.getValue(FoodItem.class);
                final HashMap<String,Object> cartMap = new HashMap<>();
                cartMap.put("Name",addedItem.getName().toString());
                cartMap.put("Price",addedItem.getBase_price().toString());
                cartMap.put("Quantity","1");

                cartref.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid().toString()).child("Products")
                        .child(ref)
                        .updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Main2Activity.this,"Added to Cart",Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}

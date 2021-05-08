package com.lakshmimanivannan.jobhuntappserviceproviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ServiceView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    static final float END_SCALE = 0.7f;
    RecyclerView all_services_view;

    ServicesAdapter adapter;

    LinearLayout contentView;
    ImageView menu_icon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_view);

        all_services_view = (RecyclerView) findViewById(R.id.service_request);
        all_services_view();

        contentView = (LinearLayout) findViewById(R.id.content_view);
        menu_icon = (ImageView) findViewById(R.id.menu_icon);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationDrawer();
    }


    //Navigation
    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        animateNavigationDrawer();
    }

    @SuppressWarnings("deprecation")
    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.banner_background));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void all_services_view() {

        all_services_view.setHasFixedSize(true);
        all_services_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("requestServices");

        final ArrayList<ServiceClass> serviceClasses = new ArrayList<>();

        SessionManager sessionManager = new SessionManager(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();

                for (DataSnapshot dataSnapshots : snapshot.getChildren()) {

                    long unix_seconds = Long.parseLong(dataSnapshots.child("timestamp").getValue().toString());
                    //convert seconds to milliseconds
                    Date date = new Date(unix_seconds * 1000L);
                    // format of the date
                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                    String java_date = jdf.format(date);

                    if (userDetails.get(SessionManager.Key_LOCATION).equalsIgnoreCase(dataSnapshots.child("location").getValue().toString()) && userDetails.get(SessionManager.Key_CATEGORY).equalsIgnoreCase(dataSnapshots.child("category").getValue().toString())) {
                        serviceClasses.add(new ServiceClass(dataSnapshots.child("image").getValue().toString(), dataSnapshots.child("uname").getValue().toString(), dataSnapshots.child("category").getValue().toString(), java_date, dataSnapshots.child("location").getValue().toString()));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Services Requested", Toast.LENGTH_SHORT).show();
                    }
                }
                adapter = new ServicesAdapter(serviceClasses, getApplicationContext());
                all_services_view.setAdapter(adapter);

                adapter.setOnItemClickListener(new ServicesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        serviceClasses.get(position);
                        Intent intent = new Intent(ServiceView.this, AcceptRequest.class);
                        intent.putExtra("username", serviceClasses.get(position).getU_name().toString());
                        intent.putExtra("image", serviceClasses.get(position).getImage().toString());
                        intent.putExtra("timestamp", serviceClasses.get(position).getTimestamp().toString());
                        intent.putExtra("category", serviceClasses.get(position).getCategory().toString());
                        intent.putExtra("location", serviceClasses.get(position).getLocation().toString());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SessionManager sessionManager = new SessionManager(this);
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(ServiceView.this, ServiceView.class));
                break;
            case R.id.nav_search:
                Toast.makeText(ServiceView.this, "Search Icon Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_accepted_services:
                if (sessionManager.checkLogin())
                    startActivity(new Intent(ServiceView.this, AcceptedRequests.class));
                else {
                    Toast.makeText(ServiceView.this, "Please Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ServiceView.class));
                }
                break;
            case R.id.nav_share:
                Toast.makeText(ServiceView.this, "Share Icon Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_login:
                if(!sessionManager.checkLogin())
                    startActivity(new Intent(ServiceView.this, Login.class));
                else
                    Toast .makeText(ServiceView.this,"Please Logout to ReLogin",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                if(sessionManager.checkLogin())
                    startActivity(new Intent(ServiceView.this, Profile.class));
                else
                {
                    Toast .makeText(ServiceView.this,"Please Login",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }
                break;
            case R.id.nav_logout:
                if(sessionManager.checkLogin())
                {
                    Toast .makeText(ServiceView.this,"Logged out successfully",Toast.LENGTH_SHORT).show();
                    sessionManager.logout();
                    startActivity(new Intent(ServiceView.this, Login.class));
                }
                break;
        }
        return true;
    }
}
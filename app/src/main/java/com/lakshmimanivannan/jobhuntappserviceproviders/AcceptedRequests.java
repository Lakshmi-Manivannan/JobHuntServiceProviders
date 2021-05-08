package com.lakshmimanivannan.jobhuntappserviceproviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AcceptedRequests extends AppCompatActivity {

    RecyclerView accepted_request;
    LinearLayout contentView;

    AcceptedRequestAdapter adapter;

    String usernumber,useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_requests);

        accepted_request = (RecyclerView) findViewById(R.id.accepted_request);
        accepted_request();

        contentView = (LinearLayout) findViewById(R.id.content_view);
    }

    private void accepted_request() {

        accepted_request.setHasFixedSize(true);
        accepted_request.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("acceptedRequest");

        final ArrayList<AcceptedRequestHelperClass> acceptedRequestHelperClasses = new ArrayList<>();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshots : snapshot.getChildren()){

                    long unix_seconds = Long.parseLong(dataSnapshots.child("timestamp").getValue().toString());
                    //convert seconds to milliseconds
                    Date date = new Date(unix_seconds*1000L);
                    // format of the date
                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                    String java_date = jdf.format(date);
                    SessionManager sessionManager = new SessionManager(AcceptedRequests.this);
                    HashMap<String,String> userDetails =sessionManager.getUserDetailFromSession();

                    if(dataSnapshots.child("servicename").getValue().toString().equals(userDetails.get(sessionManager.Key_USERNAME))){

                        acceptedRequestHelperClasses.add(new AcceptedRequestHelperClass(dataSnapshots.child("username").getValue().toString(),dataSnapshots.child("servicename").getValue().toString(),dataSnapshots.child("location").getValue().toString(),dataSnapshots.child("category").getValue().toString(),dataSnapshots.child("user_img").getValue().toString(),dataSnapshots.child("service_img").getValue().toString(),java_date,dataSnapshots.child("servicenumber").getValue().toString(),dataSnapshots.child("usernumber").getValue().toString(),dataSnapshots.child("servicemail").getValue().toString(),dataSnapshots.child("useremail").getValue().toString(),dataSnapshots.child("description").getValue().toString()));
                    }
                }
                adapter = new AcceptedRequestAdapter(acceptedRequestHelperClasses,getApplicationContext());
                accepted_request.setAdapter(adapter);

                adapter.setOnItemClickListener(new AcceptedRequestAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(int position) {
                        acceptedRequestHelperClasses.get(position);
                        //Toast.makeText(getApplicationContext(),acceptedRequestHelperClasses.get(position).getUsername().toString(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AcceptedRequests.this, AcceptRequest.class);
                        intent.putExtra("username",acceptedRequestHelperClasses.get(position).getUsername().toString());
                        intent.putExtra("image",acceptedRequestHelperClasses.get(position).getUser_img().toString());
                        intent.putExtra("timestamp",acceptedRequestHelperClasses.get(position).getTimestamp().toString());
                        intent.putExtra("category",acceptedRequestHelperClasses.get(position).getCategory().toString());
                        intent.putExtra("location",acceptedRequestHelperClasses.get(position).getLocation().toString());
                        intent.putExtra("number",acceptedRequestHelperClasses.get(position).getUsernumber().toString());
                        intent.putExtra("email",acceptedRequestHelperClasses.get(position).getUseremail().toString());
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
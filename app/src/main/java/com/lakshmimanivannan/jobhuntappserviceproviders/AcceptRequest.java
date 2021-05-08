package com.lakshmimanivannan.jobhuntappserviceproviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AcceptRequest extends AppCompatActivity {

    ImageView img;
    TextView name,timestamp,location,category;
    Button accept;
    ImageView back_icon;
    String usernumber,useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        name = (TextView) findViewById(R.id.name_profile);
        img = (ImageView) findViewById(R.id.img_profile);
        timestamp = (TextView) findViewById(R.id.timestamp_profile);
        location = (TextView) findViewById(R.id.location_profile);
        category = (TextView) findViewById(R.id.category_profile);
        accept = (Button) findViewById(R.id.accept_service);
        back_icon = (ImageView) findViewById(R.id.back_icon);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AcceptRequest.this,ServiceView.class));
            }
        });

        Uri uri = Uri.parse(getIntent().getStringExtra("image"));

        name.setText(getIntent().getStringExtra("username"));
        timestamp.setText(getIntent().getStringExtra("timestamp"));
        category.setText("Category : "+getIntent().getStringExtra("category"));
        if(getIntent().hasExtra("number"))
        {
            accept.setEnabled(false);
            location.setText("Location :"+getIntent().getStringExtra("location")+"\nPhone Number : "+getIntent().getStringExtra("number")+"\nEmail : "+getIntent().getStringExtra("email"));
        }
        else
        {
            accept.setEnabled(true);
            location.setText("Location :"+getIntent().getStringExtra("location"));
        }

        Picasso.with(getApplicationContext()).load(uri).into(img);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userDetails =sessionManager.getUserDetailFromSession();

        String user_img = uri.toString();
        String servicename = userDetails.get(SessionManager.Key_USERNAME);
        String service_img = userDetails.get(SessionManager.Key_IMAGE);
        String servicenumber = userDetails.get(SessionManager.Key_PNO);
        String serviceemail = userDetails.get(SessionManager.Key_EMAIL);
        String description = userDetails.get(SessionManager.Key_DESCRIPTION);
        String username = getIntent().getStringExtra("username");
        String _location = getIntent().getStringExtra("location");
        String _category = getIntent().getStringExtra("category");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query check_user = reference.orderByChild("u_name").equalTo(username);

        check_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                        //Getting user data
                         usernumber = snapshot.child(username).child("pno").getValue(String.class);
                         useremail = snapshot.child(username).child("email").getValue(String.class);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //deleting data from firebase
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("requestServices");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshots : snapshot.getChildren()) {

                            long unix_seconds = Long.parseLong(dataSnapshots.child("timestamp").getValue().toString());

                            String sDate1= timestamp.getText().toString();
                            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                            jdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
                            try {
                                //Date date1= jdf.parse(sDate1);
                                long millis = jdf.parse(sDate1).getTime() /1000L;
                                if(unix_seconds==millis){
                                    dataSnapshots.getRef().removeValue();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Intent intent = new Intent(AcceptRequest.this,ServiceView.class);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("acceptedRequest");

                String time = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                AcceptedRequestHelperClass acceptedRequestHelperClass = new AcceptedRequestHelperClass(username,servicename,_location,_category,user_img,service_img,time,servicenumber,usernumber,serviceemail,useremail,description);

                myRef.child(time).setValue(acceptedRequestHelperClass);
                startActivity(intent);
            }
        });
    }
}
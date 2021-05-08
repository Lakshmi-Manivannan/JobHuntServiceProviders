package com.lakshmimanivannan.jobhuntappserviceproviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button sign_up,signin;
    TextView name;
    TextInputLayout u_name,p_word;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_up = (Button) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.sign_in1);
        back = (ImageView) findViewById(R.id.back_arrow);
        name = (TextView) findViewById(R.id.msg);
        u_name = (TextInputLayout) findViewById(R.id.username) ;
        p_word = (TextInputLayout) findViewById(R.id.password) ;


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(Login.this,ServiceView.class);
                startActivity(intent);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Login.this,SignUp.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(back,"signup_image_view");
                pairs[1] = new Pair<View,String>(name,"signup_text_view");
                pairs[2] = new Pair<View,String>(signin,"signup_next_btn");
                pairs[3] = new Pair<View,String>(sign_up,"signup_login_btn");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);

                startActivity(intent,options.toBundle());
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateUname() | !ValidatePswd()){
                    return;
                }
                else{
                    isUser();
                }
            }
        });
    }

    private Boolean ValidateUname() {
        String val = u_name.getEditText().getText().toString();
        if(val.isEmpty()){
            u_name.setError("Field cannot be empty");
            return false;
        }
        else{
            u_name.setError(null);
            u_name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean ValidatePswd() {
        String val = p_word.getEditText().getText().toString();
        if(val.isEmpty()){
            p_word.setError("Field cannot be empty");
            return false;
        }
        else {
            p_word.setError(null);
            p_word.setErrorEnabled(false);
            return true;
        }
    }
    public void loginUser(){
        if(!ValidateUname() | !ValidatePswd()){
            return;
        }
        else{
            isUser();
        }
    }
    private void isUser(){
        final String Entered_uname = u_name.getEditText().getText().toString().trim();
        final String Entered_pswd = p_word.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("service_providers");

        Query check_user = reference.orderByChild("uname").equalTo(Entered_uname);

        check_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    u_name.setError(null);
                    u_name.setErrorEnabled(false);
                    String pswd_db = snapshot.child(Entered_uname).child("password").getValue(String.class);
                    if(pswd_db.equals(Entered_pswd)){

                        String _uname = snapshot.child(Entered_uname).child("uname").getValue(String.class);
                        String _fullname = snapshot.child(Entered_uname).child("full_name").getValue(String.class);
                        String _image = snapshot.child(Entered_uname).child("imageUrl").getValue(String.class);
                        String _email = snapshot.child(Entered_uname).child("email").getValue(String.class);
                        String _pswd = snapshot.child(Entered_uname).child("password").getValue(String.class);
                        String _pno = snapshot.child(Entered_uname).child("pno").getValue(String.class);
                        String _location = snapshot.child(Entered_uname).child("location").getValue(String.class);
                        String _category = snapshot.child(Entered_uname).child("category").getValue(String.class);
                        String _description = snapshot.child(Entered_uname).child("description").getValue(String.class);

                        //Shared preferences
                        SessionManager sessionManager = new SessionManager(Login.this);
                        sessionManager.createLoginSession(_fullname,_uname,_pswd,_pno,_image,_email,_location,_category,_description);

                        startActivity(new Intent(Login.this,ServiceView.class));

                    }
                    else{
                        p_word.setError("Wrong Password");
                        p_word.requestFocus();
                    }
                }
                else{
                    u_name.setError("User doesn't exist");
                    u_name.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
package com.lakshmimanivannan.jobhuntappserviceproviders;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class SignUp2 extends AppCompatActivity {

    TextInputLayout pno;
    Button sign_in,next;
    TextView textView;
    ImageView back;

    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        pno = (TextInputLayout) findViewById(R.id.pno) ;
        back = (ImageView) findViewById(R.id.back_arrow) ;
        sign_in = (Button) findViewById(R.id.sign_in2);
        next = (Button) findViewById(R.id.next2);
        textView = (TextView) findViewById(R.id.textViewaccount);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code);



        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp2.this,Login.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp2.this,SignUp.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!validatePhoneNumber())
                {
                    return;
                }

                /*Intent intent = new Intent(SignUp2.this, ServiceProvider.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(back,"signup_image_view");
                pairs[1] = new Pair<View,String>(textView,"signup_text_view");
                pairs[2] = new Pair<View,String>(next,"signup_next_btn");
                pairs[3] = new Pair<View,String>(sign_in,"signup_login_btn");*/

                String _getUserEnteredPhoneNumber = pno.getEditText().getText().toString().trim();
                    //Remove first zero if entered!
                if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
                    _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
                }
                    //Complete phone number
                final String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUserEnteredPhoneNumber;

                String full_name = getIntent().getStringExtra("fullname").toString();
                String uname = getIntent().getStringExtra("username").toString();
                String email = getIntent().getStringExtra("email").toString();
                String password = getIntent().getStringExtra("password").toString();
                String category = getIntent().getStringExtra("category").toString();
                String location = getIntent().getStringExtra("location").toString();
                String description = getIntent().getStringExtra("description").toString();
                String imageUrl = getIntent().getStringExtra("image").toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("service_providers");


                ServiceProviderHelperClass helperClass = new ServiceProviderHelperClass(full_name,uname,email,password,category,location,description,imageUrl,_phoneNo);

                myRef.child(uname).setValue(helperClass);

                SessionManager sessionManager = new SessionManager(SignUp2.this);
                sessionManager.createLoginSession(full_name,uname,password,_phoneNo,imageUrl,email,location,category,description);

                Intent intent = new Intent(SignUp2.this,ServiceView.class);
                startActivity(intent);

                /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2.this,pairs);
                startActivity(intent,options.toBundle());*/
            }
        });

    }

    private boolean validatePhoneNumber() {
        String val = pno.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            pno.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            pno.setError("No White spaces are allowed!");
            return false;
        } else {
            pno.setError(null);
            pno.setErrorEnabled(false);
            return true;
        }
    }

}
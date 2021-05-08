package com.lakshmimanivannan.jobhuntappserviceproviders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SignUp extends AppCompatActivity {

    Button sign_in,next1;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextInputLayout uname,pswd,email,full_name, location,description;

    //ProfilePicture
    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String profileImageUrl;
    ImageView back;
    TextView text;

    RadioGroup radioGroup;
    RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        sign_in = (Button) findViewById(R.id.sign_in);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        next1 = (Button) findViewById(R.id.next1);
        uname = (TextInputLayout) findViewById(R.id.uname) ;
        full_name = (TextInputLayout) findViewById(R.id.name) ;
        email = (TextInputLayout) findViewById(R.id.email) ;
        pswd = (TextInputLayout) findViewById(R.id.pswd) ;
        location = (TextInputLayout) findViewById(R.id.Location) ;
        description = (TextInputLayout) findViewById(R.id.description) ;
        back = (ImageView) findViewById(R.id.back_arrow) ;
        text = (TextView) findViewById(R.id.textViewaccount) ;
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ValidateName() | !ValidateUname() | !ValidateEmail() | !ValidatePswd() |!ValidateDescription() |!ValidateLocation())
                {
                    return ;
                }
                if(profileImageUrl==null)
                {
                    profileImageUrl = "https://firebasestorage.googleapis.com/v0/b/job-hunt-app-75580.appspot.com/o/images%2Fprofile.png?alt=media&token=b596171f-f6f2-4c21-9cfc-a6968ab152fa";
                }

                /*String S_name = full_name.getEditText().getText().toString();
                String S_uname = uname.getEditText().getText().toString();
                String S_email = email.getEditText().getText().toString();
                String S_phno = "+91 " + pno.getEditText().getText().toString();
                String S_pswd = pswd.getEditText().getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                UserHelperClass helperClass = new UserHelperClass(S_name,S_uname,S_pswd,S_email,S_phno,profileImageUrl);

                myRef.child(S_uname).setValue(helperClass);

                Intent intent = new Intent(SignUp.this,UserDashboard.class);
                startActivity(intent);*/
                /*Intent intent = new Intent(SignUp.this,VerificationPhone.class);
                intent.putExtra("phoneNo",S_phno);
                startActivity(intent);*/

                int selectedId=radioGroup.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(selectedId);
                Toast.makeText(SignUp.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this,SignUp2.class);

                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(back,"signup_image_view");
                pairs[1] = new Pair<View,String>(text,"signup_text_view");
                pairs[2] = new Pair<View,String>(next1,"signup_next_btn");
                pairs[3] = new Pair<View,String>(sign_in,"signup_login_btn");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);

                intent.putExtra("username",uname.getEditText().getText().toString());
                intent.putExtra("fullname",full_name.getEditText().getText().toString());
                intent.putExtra("email",email.getEditText().getText().toString());
                intent.putExtra("password",pswd.getEditText().getText().toString());
                intent.putExtra("image",profileImageUrl);

                intent.putExtra("description",description.getEditText().getText().toString());
                intent.putExtra("category",radioButton.getText().toString());
                intent.putExtra("location",location.getEditText().getText().toString());

                startActivity(intent,options.toBundle());
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri =data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    @SuppressWarnings("deprecation")
    private void uploadPicture() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image......");
        progressDialog.show();

        final String randomKey  = UUID.randomUUID().toString();
        final StorageReference riversRef = storageReference.child("images/"+randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded", Snackbar.LENGTH_LONG).show();


                        riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                profileImageUrl=task.getResult().toString();
                                Log.i("URL",profileImageUrl);
                            }
                        });                                            }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Failed to upload Image",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        progressDialog.setMessage("Percentage: "+(int)progressPercent + "%");
                    }
                });

    }

    private Boolean ValidateName() {
        String val = full_name.getEditText().getText().toString();

        if(val.isEmpty()){
            full_name.setError("Field cannot be empty");
            return false;
        }
        else {
            full_name.setError(null);
            full_name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidateLocation() {
        String val = location.getEditText().getText().toString();

        if(val.isEmpty()){
            location.setError("Field cannot be empty");
            return false;
        }
        else {
            location.setError(null);
            location.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidateDescription() {
        String val = description.getEditText().getText().toString();

        if(val.isEmpty()){
            description.setError("Field cannot be empty");
            return false;
        }
        else {
            description.setError(null);
            description.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean ValidateUname() {
        String val = uname.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";//"(?=\\s+$)";
        if(val.isEmpty()){
            uname.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=15){
            uname.setError("Username is too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)){
            uname.setError("White spaces are not allowed");
            return false;
        }
        else{
            uname.setError(null);
            uname.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean ValidateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidatePswd() {
        String val = pswd.getEditText().getText().toString();
        String passwordVal = "^"+
                "(?=.*[a-zA-Z])"+
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$)"+
                ".{4,}"+
                "$";

        if(val.isEmpty()){
            pswd.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(passwordVal)){
            pswd.setError("Password is too weak");
            return false;
        }
        else {
            pswd.setError(null);
            pswd.setErrorEnabled(false);
            return true;
        }
    }

}
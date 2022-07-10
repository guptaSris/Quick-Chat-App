package com.example.quickchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchatapp.R;
import com.example.quickchatapp.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegistrationActivity extends AppCompatActivity {
TextView txt_signin,btn_SignUp;
ImageView profileimage;
FirebaseAuth auth;
EditText reg_name,reg_email,reg_password,reg_cPass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        txt_signin = findViewById(R.id.txt_signin);
        profileimage = findViewById(R.id.profile_image);
        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_pass);
        reg_cPass = findViewById(R.id.reg_cPass);
        btn_SignUp = findViewById(R.id.btn_SignUp);

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = reg_email.getText().toString();
                String name = reg_name.getText().toString();
                String password = reg_password.getText().toString();
                String status = "Hey there!";
                String cPassword = reg_cPass.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword)){
                    Toast.makeText(RegistrationActivity.this, "Enter Valid data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }                 else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    reg_email.setError("Enter valid email");
                            Toast.makeText(RegistrationActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                        }

                        else if(!password.matches(cPassword) || password.length()<6){
                    progressDialog.dismiss();
                    reg_cPass.setError("Password does not match");
                            Toast.makeText(RegistrationActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        }
                        else if( password.length()<6){                    progressDialog.dismiss();
                    reg_password.setError("Password is less than 6 characters");
                            Toast.makeText(RegistrationActivity.this, "Password is less than 6 characters", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
                                        String status = "Hey there!";
                                        Users users = new Users(auth.getUid(),name,email,status);
                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                                                }
                                                else{
                                                    Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    else{                    progressDialog.dismiss();
                                        Toast.makeText(RegistrationActivity.this, "Oops..Something went wrong! ", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }

                    }
                });



        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });


    }
}
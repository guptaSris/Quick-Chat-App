package com.example.quickchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quickchatapp.R;
import com.example.quickchatapp.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingActivity extends AppCompatActivity {
ImageView save;
FirebaseAuth auth;
    String email;
    FirebaseDatabase database;
FirebaseStorage storage;
EditText setting_name,setting_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        save = findViewById(R.id.save);
        setting_name = findViewById(R.id.setting_name);
        setting_status = findViewById(R.id.setting_status);

        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             snapshot.child("email").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                setting_name.setText(name);
                setting_status.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String name =   setting_name.getText().toString();
              String status =   setting_status.getText().toString();
              Users users = new Users(auth.getUid(),name,email,status);
              reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(SettingActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                      }
                      else{
                          Toast.makeText(SettingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                      }
                  }
              });

            }
        });



    }
}
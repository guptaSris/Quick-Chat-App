package com.example.quickchatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchatapp.Messages;
import com.example.quickchatapp.MessagesAdapter;
import com.example.quickchatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
String RecieverUid,RecieverName,SenderUid;
TextView recievername;
FirebaseDatabase database;
CardView sendBtn;
EditText edtMessage;
String senderRoom,recieverRoom;
ArrayList<Messages> messagesArrayList;
MessagesAdapter adapter;
RecyclerView messageAdapter;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
database = FirebaseDatabase.getInstance();
firebaseAuth = FirebaseAuth.getInstance();
        RecieverName = getIntent().getStringExtra("name");
        RecieverUid = getIntent().getStringExtra("uid");
        messagesArrayList = new ArrayList<>();
messageAdapter = findViewById(R.id.messageAdapter);
LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
linearLayoutManager.setStackFromEnd(true);
messageAdapter.setLayoutManager(linearLayoutManager);
adapter = new MessagesAdapter(ChatActivity.this,messagesArrayList);
messageAdapter.setAdapter(adapter);
recievername= findViewById(R.id.recievername);
sendBtn = findViewById(R.id.sendBtn);
edtMessage = findViewById(R.id.edtMessage);
recievername.setText(""+RecieverName);
SenderUid = firebaseAuth.getUid();
senderRoom = SenderUid+RecieverUid;
recieverRoom = RecieverUid+SenderUid;
        DatabaseReference reference =  database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference =  database.getReference().child("chats").child(senderRoom).child("messages");
        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot datasnapshot:snapshot.getChildren()){
                    Messages messages = datasnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

sendBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String message = edtMessage.getText().toString();
        if(message.isEmpty()){
            Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }else{edtMessage.setText("");
        Date date = new Date();
        Messages messages = new Messages(message,SenderUid,date.getTime());
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .push()
                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                database.getReference().child("chats")
                        .child(recieverRoom)
                        .child("messages")
                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });


        }



    }
});

    }
}
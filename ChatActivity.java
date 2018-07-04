package com.example.kankan.timepass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener  {

    private String senderName,senderPhoto,senderEmail,senderID;
    private RecyclerView  reView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myData;
    private DatabaseReference NotificationsReference;
    private List<Message> messageList;
    private EditText edtType;
    private Button btnsend;
    private ImageView imgPropic;
    private TextView txtName,txtOnline;
    private FirebaseFirestore  mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);

        messageList=new ArrayList<>();

        Bundle bundle=getIntent().getExtras();

        mFireStore=FirebaseFirestore.getInstance();

        senderName=bundle.getString("SENDER_NAME");
        senderPhoto=bundle.getString("SENDER_PHOTO");
        senderEmail=bundle.getString("SENDER_EMAIL");
        senderID=bundle.getString("SENDER_ID");



        //imgBackground=findViewById(R.id.imgBackGround);

        reView=findViewById(R.id.chatReView);
        reView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        reView.setHasFixedSize(true);

        myData=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //################## For notification###########
        NotificationsReference=FirebaseDatabase.getInstance().getReference().child("Notification");
        NotificationsReference.keepSynced(true);

        edtType=findViewById(R.id.edtTypeMessage);
        btnsend=findViewById(R.id.btnSendMessageTODatabase);
        btnsend.setOnClickListener(this);

        imgPropic=findViewById(R.id.imgChatPhoto);
        txtName=findViewById(R.id.txtChatName);
        txtOnline=findViewById(R.id.txtOnlineStatusOnChatActivity);


        Glide.with(ChatActivity.this).load(senderPhoto).into(imgPropic);
        txtName.setText(senderName);

        //Toast.makeText(ChatActivity.this,"onCreate ChatActivity",Toast.LENGTH_SHORT).show();

        //myData.child("Profile").child(user.getUid()).child("isOnline").setValue("Yes");
        txtName.setOnClickListener(ChatActivity.this);
        setTextOnlie();
    }




    @Override
    protected void onResume() {
        Glide.with(ChatActivity.this).load(senderPhoto).into(imgPropic);
        txtName.setText(senderName);
        addAllmessage();
        //Toast.makeText(ChatActivity.this,"onResume ChatActivity",Toast.LENGTH_SHORT).show();
        super.onResume();
    }
    private void setTextOnlie()
    {
        myData.child("Payal").child(senderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String kutta=dataSnapshot.child("isOnline").getValue().toString();
                if(kutta.equals("Yes"))
                {
                    txtOnline.setText("Online");
                    txtOnline.setTextColor(Color.GREEN);
                }
                else
                {
                    String getLastSeen=ConvertTime.getTimeAgo(Long.parseLong(kutta),ChatActivity.this);

                        if(getLastSeen.equals("fuck"))
                        {
                            txtOnline.setText("Online");
                            txtOnline.setTextColor(Color.GREEN);
                        }
                        else {

                            txtOnline.setText("Last seen " + getLastSeen);
                            txtOnline.setTextColor(Color.RED);
                        }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //set onLine status to the user

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myData.child("Payal").child(user.getUid()).child("isOnline").setValue("Yes");
            }
        },2000);


    }

    @Override
    protected void onStop() {
        super.onStop();
        myData.child("Payal").child(user.getUid()).child("isOnline").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSendMessageTODatabase:
                sentTodatabase();
                addAllmessage();
                break;
            case R.id.txtChatName:
                Intent intent=new Intent(ChatActivity.this,Pro.class);

                intent.putExtra("ID_PRO",senderID);
                startActivity(intent);
                break;
        }
    }

    private void addAllmessage() {
        messageList.clear();
        myData.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Message message=snapshot.getValue(Message.class);
                    if((message.getCurrentUser().equals(user.getEmail()) && message.getSenderUser().equals(senderEmail))
                        || message.getCurrentUser().equals(senderEmail) && message.getSenderUser().equals(user.getEmail()))

                    {
                        messageList.add(message);
                    }
                    ChatAdapter adapter=new ChatAdapter(messageList,user.getEmail(),senderEmail);
                    reView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void sentTodatabase() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        final String currentTime=format.format(calendar.getTime());
        String fuck=edtType.getText().toString();
        if(TextUtils.isEmpty(fuck))
        {
            Toast.makeText(this,"Please type something",Toast.LENGTH_SHORT).show();
            return;
        }

        // ############For Notification############

        HashMap<String,Object> notificationData=new HashMap<>();

        notificationData.put("FROM",user.getUid());
        notificationData.put("Message",edtType.getText().toString());

        /*mFireStore.collection("Notification").add(notificationData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ChatActivity.this,"Notification Sent.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });*/

        NotificationsReference.child(senderID).child(NotificationsReference.push().getKey()).setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()) {
                        Message message = new Message(user.getEmail(), edtType.getText().toString(), senderEmail, currentTime);
                        myData.child("Message").child(myData.push().getKey()).setValue(message);
                        edtType.setText("");
                }
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_account)
        {
            Intent intent=new Intent(ChatActivity.this,Pro.class);

            intent.putExtra("ID_PRO",senderID);
            startActivity(intent);
        }
        /*switch (id)
        {
            case R.id.action_account:

                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

}

package com.example.kankan.timepass;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class AllUsersActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myData;
    private StorageReference myStore;
    private List<Profile> profileList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        recyclerView=findViewById(R.id.reViewAllKas);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this));

        swipeRefreshLayout=findViewById(R.id.swipeAllUserkas);


        profileList=new ArrayList<>();
        profileList.clear();
        myData= FirebaseDatabase.getInstance().getReference();
        myStore= FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        getAllTheUser();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTheUser();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myData.child("Payal").child(user.getUid()).child("isOnline").setValue("Yes");
                Toast.makeText(AllUsersActivity.this,"onStart ChatActivity",Toast.LENGTH_SHORT).show();
            }
        },2000);



    }

    @Override
    protected void onStop() {


        super.onStop();
        myData.child("Payal").child(user.getUid()).child("isOnline").setValue(ServerValue.TIMESTAMP);
        Toast.makeText(AllUsersActivity.this,"onStop ChatActivity",Toast.LENGTH_SHORT).show();
    }

    private void getAllTheUser()
    {
        swipeRefreshLayout.setRefreshing(true);
        profileList.clear();
        myData.child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {

                    Profile profile = snapshot.getValue(Profile.class);

                    if (profile.getEmail().equals(user.getEmail())) {

                    } else {
                        //Toast.makeText(getActivity(), profile.getName(), Toast.LENGTH_SHORT).show();
                        profileList.add(profile);
                    }
                }
                AllUserAdapter adapter=new AllUserAdapter(profileList,AllUsersActivity.this);

                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllTheUser();
    }
}

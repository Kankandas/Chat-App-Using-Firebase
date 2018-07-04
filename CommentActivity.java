package com.example.kankan.timepass;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.Slidr;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgCancel;
    private Button btnSent;
    private EditText edtGetComment;
    private RecyclerView reView;
    private SwipeRefreshLayout swipe;
    private DatabaseReference myData;
    private FirebaseAuth myAuth;
    private FirebaseUser curUser;
    private Bundle bundle;
    private String postId,profileName,profileImage;
    private List<Comment> commentList;
    private List<Profile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Slidr.attach(this);

        imgCancel=findViewById(R.id.imgCloseCommentLayout);
        btnSent=findViewById(R.id.btnSentComment);
        edtGetComment=findViewById(R.id.edtCommentLay);
        reView=findViewById(R.id.reViewCommnets);
        swipe=findViewById(R.id.swipeComments);

        commentList=new ArrayList<>();

        bundle=getIntent().getExtras();
        postId=bundle.getString("TIME_LINE_ID");

        reView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        myData= FirebaseDatabase.getInstance().getReference();
        myAuth=FirebaseAuth.getInstance();
        curUser=myAuth.getCurrentUser();



        myData.child("Profile").child(curUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile=dataSnapshot.getValue(Profile.class);
                profileName=profile.getName();
                profileImage=profile.getUrl();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refreshTheList();
        imgCancel.setOnClickListener(this);
        btnSent.setOnClickListener(this);

    }
    private void sentCommentToDatabase()
    {
        Comment comment=new Comment(postId,curUser.getUid(),edtGetComment.getText().toString(),profileName,profileImage);
        myData.child("Comments").child(postId).child(myData.push().getKey()).setValue(comment);
        edtGetComment.setText("");

    }
    private void refreshTheList() {
        swipe.setRefreshing(true);
        commentList.clear();
        myData.child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                CommentAdapter commentAdapter=new CommentAdapter(CommentActivity.this,commentList);
                reView.setAdapter(commentAdapter);
                swipe.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgCloseCommentLayout:
                startActivity(new Intent(this, TimeLineFragment.class));
                finish();
                break;
            case R.id.btnSentComment:
                sentCommentToDatabase();
                refreshTheList();
                break;
        }
    }
}

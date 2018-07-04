package com.example.kankan.timepass;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment implements View.OnClickListener {

    private ImageView imgSelect,imgDialog,imgSent;
    private SwipeRefreshLayout swipe;
    private RecyclerView recyclerView;
    private TextView txtType;
    private EditText edt,edtDialog;
    private Dialog dialog;
    private static final int STATUS_IMAGE_CODE=2343;
    private Uri uriImage;
    private DatabaseReference myRef;
    private StorageReference myStore;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    private ProgressDialog progressDialog;
    private Profile ownProfile;
    private List<TimeLine> listTimeline;
    private List<String> idList;
    private TimeLineAdapter adapter;


    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_time_line, container, false);

        imgSelect=view.findViewById(R.id.imgOpenGalarForStatus);
        txtType=view.findViewById(R.id.txtWirteToTimeLine);
        swipe=view.findViewById(R.id.swipeTimeline);

        recyclerView=view.findViewById(R.id.reViewTimeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listTimeline=new ArrayList<>();
        idList=new ArrayList<>();

        myRef= FirebaseDatabase.getInstance().getReference();
        myStore=FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        curUser=auth.getCurrentUser();

        progressDialog=new ProgressDialog(getActivity());

        refreshTheList();

        myRef.child("Profile").child(curUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ownProfile=dataSnapshot.getValue(Profile.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        txtType.setOnClickListener(this);
        imgSelect.setOnClickListener(this);

        dialog=new Dialog(getActivity());

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTheList();
            }
        });

        return view;
    }
    private void updateDatabaseAfterLike()
    {
        List<TimeLine> updateList=new ArrayList<TimeLine>();



        for (int i=0;i<updateList.size();i++)
        {
            String TimeLineId=updateList.get(i).getTimeLineid();
            String noOfLikes=updateList.get(i).getNoOFlikes();
            myRef.child("Time Line").child(TimeLineId).child("noOFlikes").setValue(noOfLikes);
        }
    }
    private void refreshTheList()
    {
        listTimeline.clear();
        swipe.setRefreshing(true);

        myRef.child("Time Line").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listTimeline.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    TimeLine sexTime=snapshot.getValue(TimeLine.class);
                    //Toast.makeText(getActivity(),sexTime.getProfileName(),Toast.LENGTH_SHORT).show();
                    listTimeline.add(sexTime);
                }
                adapter=new TimeLineAdapter(listTimeline,getActivity(),myRef,ownProfile);
                recyclerView.setAdapter(adapter);
                updateDatabaseAfterLike();
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
            case R.id.txtWirteToTimeLine:
                dialog.setContentView(R.layout.layout_user_status_upload);
                edt=dialog.findViewById(R.id.edtStatusText);
                ImageView img=dialog.findViewById(R.id.statusDialogSent);
                edt.setEnabled(true);
                img.setEnabled(true);
                dialog.show();

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String id=myRef.push().getKey();

                        TimeLine mineTime=new TimeLine(null,null,curUser.getUid(),edt.getText().toString(),ownProfile.getName(),ownProfile.getUrl(),"onlyText"
                                                        ,id,"0");
                        myRef.child("Time Line").child(id).setValue(mineTime);
                        dialog.cancel();
                    }
                });


                break;
            case R.id.imgOpenGalarForStatus:

                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,STATUS_IMAGE_CODE);


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==STATUS_IMAGE_CODE && resultCode==getActivity().RESULT_OK)
        {
            uriImage=data.getData();
            if(uriImage!=null)
            {
                dialog.setContentView(R.layout.image_timeline_layout);
                imgDialog=dialog.findViewById(R.id.imgUploadImageTimeline);
                edtDialog=dialog.findViewById(R.id.edtUpdateSomeStatus);
                imgSent=dialog.findViewById(R.id.btnUploadToTimeline);
                imgDialog.setEnabled(true);
                edtDialog.setEnabled(true);
                imgSent.setEnabled(true);
                dialog.show();
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriImage);
                    imgDialog.setImageBitmap(bm);
                    String string=edtDialog.getText().toString();
                    imgSent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StorageReference ref1=myStore.child("Timeline"+System.currentTimeMillis());
                            ref1.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.setMessage("Uploading...");
                                    progressDialog.show();
                                    String download=taskSnapshot.getDownloadUrl().toString();
                                    String id=myRef.push().getKey();
                                    TimeLine timeLine=new TimeLine(edtDialog.getText().toString(),download,curUser.getUid(),edtDialog.getText().toString(),ownProfile.getName()
                                        ,ownProfile.getUrl(),"both",id,"0");
                                    myRef.child("Time Line").child(id).setValue(timeLine);
                                    progressDialog.dismiss();
                                    dialog.cancel();
                                }
                            });
                        }
                    });

                }
                catch (Exception e)
                {

                }


            }
        }
    }
}

package com.example.kankan.timepass;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment  {
    private RecyclerView reView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myData;
    private StorageReference myStore;
    private List<Profile> profileList;
    private List<String> onLineList;
    private SwipeRefreshLayout refreshLayout;


    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_all_users, container, false);

        profileList=new ArrayList<>();
        onLineList=new ArrayList<>();
        profileList.clear();
        onLineList.clear();
        myData= FirebaseDatabase.getInstance().getReference();
        myStore= FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        refreshLayout=v.findViewById(R.id.swipe);


        Toast.makeText(getActivity(),"onCreate Call",Toast.LENGTH_SHORT).show();


        reView=v.findViewById(R.id.reView);
        reView.setLayoutManager(new LinearLayoutManager(getActivity()));


        getAllTheUser();





        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllTheUser();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllTheUser();

    }
    private void getOnlineList()
    {
        onLineList.clear();
        myData.child("Payal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onLineList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    //Toast.makeText(getActivity(),snapshot.getKey().toString(),Toast.LENGTH_SHORT).show();
                    if(snapshot.getKey().toString().equals(user.getUid().toString()))
                    {

                    }
                    else {
                        String kas = snapshot.child("isOnline").getValue().toString();
                        onLineList.add(kas);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void getAllTheUser()
    {

        refreshLayout.setRefreshing(true);
        getOnlineList();
        profileList.clear();
        //final List<OnLine> onLineList1=new ArrayList<>();
        myData.child("Chat List").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    Profile profile=snapshot.getValue(Profile.class);

                    if(profile.getUserID().equals(user.getUid()))
                    {

                    }
                    else {
                        profileList.add(profile);
                    }
                }
                if(onLineList.size()==0)
                {
                    getAllTheUser();

                }
                else {


                   //Toast.makeText(getActivity(),"Profile:"+profileList.size()+"     OnlineList: "+onLineList.size(),Toast.LENGTH_SHORT).show();
                    ReViewAdapter adapter = new ReViewAdapter(profileList, getActivity(), onLineList);

                    reView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                }

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

package com.example.kankan.timepass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TimeLine> list;
    private Context context;
    private static boolean firstforText=true;
    private static boolean firstforBoth=true;
    private String previous;
    private int count=0;
    private DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
    private Profile currentProfile;

    public TimeLineAdapter() {
    }

    public TimeLineAdapter(List<TimeLine> list, Context context,DatabaseReference myRef,Profile currentProfile) {
        this.list = list;
        this.context = context;
        //this.myRef=myRef;
        this.currentProfile=currentProfile;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;

        if (viewType==0)
        {
            inflater=LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.timeline_adapter_layout_with_only_text,null);
            return new MyInnerWithText(view);
        }
        else
        {
            inflater=LayoutInflater.from(parent.getContext());
            View view=inflater.inflate(R.layout.timeline_adapter_layout,null);
            return new MyInnerWithImage(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType())
        {
            case 0:
                MyInnerWithText myInnerWithText=(MyInnerWithText)holder;
                ((MyInnerWithText) holder).txtProfileNameOnlyText.setText(list.get(position).getProfileName());
                ((MyInnerWithText) holder).txtTimelineText.setText(list.get(position).getText());
                Glide.with(context).load(list.get(position).getProfileImageUri()).into(((MyInnerWithText) holder).imgProfileOnlyText);


                break;
            case 1:
                MyInnerWithImage myInnerWithImage=(MyInnerWithImage)holder;
                ((MyInnerWithImage) holder).txtProfileNameImage.setText(list.get(position).getProfileName());
                ((MyInnerWithImage) holder).txtTimelineTExt.setText(list.get(position).getText());
                Glide.with(context).load(list.get(position).getUri()).into(((MyInnerWithImage) holder).imgUploadedPhoto);
                Glide.with(context).load(list.get(position).getProfileImageUri()).into(((MyInnerWithImage) holder).imgProfileImage);

                break;
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(list.get(position).getTypeOfTimeline().equals("onlyText"))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    class MyInnerWithImage extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imgProfileImage,imgUploadedPhoto,imgLikeBoth,imgCommentBoth;
        private TextView txtProfileNameImage,txtTimelineTExt;
        public MyInnerWithImage(View itemView) {
            super(itemView);
            imgProfileImage=itemView.findViewById(R.id.imgProfilePhotoTimeLine);
            imgUploadedPhoto=itemView.findViewById(R.id.imgPhotoThatUploadTOTimeline);
            txtProfileNameImage=itemView.findViewById(R.id.txtProfieNameTimeLine);
            txtTimelineTExt=itemView.findViewById(R.id.txtTimeLineBotnTextAndImageUpload);
            imgCommentBoth=itemView.findViewById(R.id.imgCommentBoth);


            imgCommentBoth.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.imgCommentBoth:
                    int position=getAdapterPosition();
                    Intent intent=new Intent(v.getContext(),CommentActivity.class);
                    intent.putExtra("TIME_LINE_ID",list.get(position).getTimeLineid());
                    v.getContext().startActivity(intent);

                    break;
            }
        }
    }
    class MyInnerWithText extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imgProfileOnlyText,imgLikeOnlyText,imgCommentOnlyText;
        private TextView txtProfileNameOnlyText,txtTimelineText;
        public MyInnerWithText(View itemView) {
            super(itemView);

            imgProfileOnlyText=itemView.findViewById(R.id.imgProfilePhotoTimeLineForOnlyText);
            txtProfileNameOnlyText=itemView.findViewById(R.id.txtProfieNameTimeLineforOnlyText);
            txtTimelineText=itemView.findViewById(R.id.txtTimeLineTextUpload);
            imgCommentOnlyText=itemView.findViewById(R.id.imgCommentOnlyText);
            imgCommentOnlyText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int posi=getAdapterPosition();
            switch (v.getId())
            {
                case R.id.imgCommentOnlyText:
                    int position=getAdapterPosition();
                    Intent intent=new Intent(v.getContext(),CommentActivity.class);
                    intent.putExtra("TIME_LINE_ID",list.get(position).getTimeLineid());
                    v.getContext().startActivity(intent);
                    break;
            }
        }
    }
}

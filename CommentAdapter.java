package com.example.kankan.timepass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyInnerComment>
{
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter() {
    }

    public CommentAdapter(Context context,List<Comment> commentList) {
        this.commentList = commentList;
        this.context=context;

    }

    @NonNull
    @Override
    public MyInnerComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.comment_set,parent,false);

        return new MyInnerComment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInnerComment holder, int position) {
        holder.txtComment.setText(commentList.get(position).getComment());
        holder.txtProfileName.setText(" "+commentList.get(position).getProfileName());
        Glide.with(context).load(commentList.get(position).getProfileImage()).into(holder.imgProfilePhoto);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyInnerComment extends RecyclerView.ViewHolder
    {
        private ImageView imgProfilePhoto;
        private TextView txtProfileName,txtComment;
        public MyInnerComment(View itemView) {
            super(itemView);

            imgProfilePhoto=itemView.findViewById(R.id.imgCommentLayoutProfilePhoto);
            txtProfileName=itemView.findViewById(R.id.txtCommentProfileName);
            txtComment=itemView.findViewById(R.id.txtCommentCurrent);
        }
    }
}

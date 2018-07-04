package com.example.kankan.timepass;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyInner> {
    private List<Profile> list;
    private Context context;

    public AllUserAdapter(List<Profile> list,Context context)  {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyInner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.all_user_show_layout,parent,false);
        return new MyInner(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInner holder, int position) {
        holder.txtName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getUrl()).into(holder.imageView);
        holder.imgOnline.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyInner extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView txtName;
        private ImageView imageView;
        private ImageView imgOnline;
        public MyInner(View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtAlluserShowProPic);
            imageView=itemView.findViewById(R.id.imgAlluserShowProPic);
            imgOnline=itemView.findViewById(R.id.imgAllUserShowOnline);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();


            Intent intent=new Intent(v.getContext(),ProfileActivity.class);
            intent.putExtra("ID",list.get(position).getUserID());
            intent.putExtra("NAME",list.get(position).getName());
            intent.putExtra("IMAGE",list.get(position).getUrl());
            intent.putExtra("SCHOOL",list.get(position).getSchool());
            intent.putExtra("COLLEGE",list.get(position).getCollege());
            intent.putExtra("RELATION",list.get(position).getRelation());
            v.getContext().startActivity(intent);

        }
    }

}

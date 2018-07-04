package com.example.kankan.timepass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReViewAdapter extends RecyclerView.Adapter<ReViewAdapter.MyInner>  {
    private List<Profile> list;
    private List<String> onLines;
    private Context context;


    public ReViewAdapter(List<Profile> list,Context context,List<String > onLines) {
        this.context=context;
        this.list = list;
        this.onLines=onLines;
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
        if(onLines.get(position).equals("Yes")) {
            holder.imgOnline.setVisibility(View.VISIBLE);
            //Toast.makeText(context,onLines.get(position).getIsOnline(),Toast.LENGTH_SHORT).show();
        }

        else {
            holder.imgOnline.setVisibility(View.INVISIBLE);
        }
        Glide.with(context).load(list.get(position).getUrl()).into(holder.imageView);
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

            Intent intent=new Intent(v.getContext(),ChatActivity.class);

            intent.putExtra("SENDER_NAME",list.get(position).getName());
            intent.putExtra("SENDER_PHOTO",list.get(position).getUrl());
            intent.putExtra("SENDER_EMAIL",list.get(position).getEmail());
            intent.putExtra("SENDER_ID",list.get(position).getUserID());
            v.getContext().startActivity(intent);


        }
    }
}

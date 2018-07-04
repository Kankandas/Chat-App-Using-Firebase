package com.example.kankan.timepass;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messageList;
    private String currentUser;
    private String sender;


    public ChatAdapter(List<Message> messageList, String currentUser, String sender) {
        this.messageList = messageList;
        this.currentUser = currentUser;
        this.sender = sender;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view;
        if(viewType==0)
        {
            inflater=LayoutInflater.from(parent.getContext());
            view=inflater.inflate(R.layout.set_chat_layout,parent,false);
            return new MyInnerCur(view);
        }
        else {
            inflater=LayoutInflater.from(parent.getContext());
            view=inflater.inflate(R.layout.set_sender_chat_layout,parent,false);
            return new MyInnerSend(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType())
        {
            case 0:
                MyInnerCur myInnerCur=(MyInnerCur)holder;
                    ((MyInnerCur) holder).txtCurrent.setText(messageList.get(position).getMessage());
                    ((MyInnerCur) holder).txtUserTime.setText(messageList.get(position).getTime());

                break;
            case 1:
                MyInnerSend myInnerSend=(MyInnerSend)holder;

                    ((MyInnerSend) holder).txtSender.setText(messageList.get(position).getMessage());
                    ((MyInnerSend) holder).txtSenderTime.setText(messageList.get(position).getTime());

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getCurrentUser().equals(currentUser))
        {
            return 0;
        }
        else
            return 1;
    }


    class MyInnerCur extends RecyclerView.ViewHolder
    {
        private TextView txtCurrent,txtUserTime;
        public MyInnerCur(View itemView) {
            super(itemView);
            txtCurrent=itemView.findViewById(R.id.txtCurUserMessage);
            txtUserTime=itemView.findViewById(R.id.txtCurUserMessageTime);
        }

    }

    class MyInnerSend extends RecyclerView.ViewHolder
    {
        private TextView txtSender,txtSenderTime;
        public MyInnerSend(View itemView) {
            super(itemView);
            txtSender=itemView.findViewById(R.id.txtSenderMessageKas);
            txtSenderTime=itemView.findViewById(R.id.txtSenderMessageTime);
        }
    }

}

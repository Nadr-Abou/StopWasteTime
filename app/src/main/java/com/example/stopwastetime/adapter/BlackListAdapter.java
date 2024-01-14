package com.example.stopwastetime.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwastetime.model.BlackList;
import com.example.stopwastetime.interfaccie.RecycleViewInterface;
import com.example.stopwastetime.R;

import java.util.ArrayList;

public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.AppRowViewHolder> {
    private final ArrayList<BlackList> blackLists;
    RecycleViewInterface recycleViewInterface;

    public BlackListAdapter(ArrayList<BlackList> blackLists, RecycleViewInterface recycleViewInterface) {
        this.blackLists = blackLists;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public AppRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_layout_card,parent,false);
        return new AppRowViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull AppRowViewHolder holder, int position) {
        holder.bind(blackLists.get(position));
    }

    @Override
    public int getItemCount() {
        return blackLists.size();
    }


    static class AppRowViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final ImageView icon;
        BlackListAdapter blackListAdapter;

        public AppRowViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.App_AppName_layout);
            this.icon = itemView.findViewById(R.id.App_AppIcon_layout);
            itemView.findViewById(R.id.removeButton).setOnClickListener(v -> {
                blackListAdapter.recycleViewInterface.onClick(getAdapterPosition());
            });
        }

        public void bind(BlackList blackList){
            name.setText(blackList.getName());
            icon.setImageDrawable(blackList.getImage());
        }

        public AppRowViewHolder linkAdapter(BlackListAdapter blackListAdapter){
            this.blackListAdapter = blackListAdapter;
            return this;
        }
    }
}

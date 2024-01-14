package com.example.stopwastetime.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwastetime.database.AppDatabase;
import com.example.stopwastetime.model.Excuses;
import com.example.stopwastetime.R;

import java.util.ArrayList;

public class ExcusesAdapter extends RecyclerView.Adapter<ExcusesAdapter.ExcusesViewHolder>{
    ArrayList<Excuses> excuses;

    public ExcusesAdapter(ArrayList<Excuses> excuses) {
        this.excuses = excuses;
    }

    @NonNull
    @Override
    public ExcusesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.excuses_layout_card,parent,false);
        return new ExcusesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcusesViewHolder holder, int position) {
        holder.bind(excuses.get(position));
    }

    @Override
    public int getItemCount() {
        return excuses.size();
    }


    class ExcusesViewHolder extends RecyclerView.ViewHolder{

        TextView justification;
        TextView freeTime;
        TextView blockTime;
        ImageButton removeButton;

        public ExcusesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.justification = itemView.findViewById(R.id.App_Excuse_layout);
            this.freeTime = itemView.findViewById(R.id.App_FreeTime_layout);
            this.blockTime = itemView.findViewById(R.id.App_BlockTime_layout);
            this.removeButton = itemView.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(v->{
                AppDatabase.getInstance(itemView.getContext()).excuseDao().deleteExcuse(excuses.get(getAdapterPosition()));
                excuses.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
        public void bind(Excuses excuses){
            justification.setText(excuses.getScusa());
            freeTime.setText(excuses.getTempoUtilizzo() + "m");
            blockTime.setText(excuses.getBlocco() + "m");
        }
    }
}

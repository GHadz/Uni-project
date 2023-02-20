package com.example.main.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;

import java.util.ArrayList;
//adapter for the recycler view that contains the conditions, similar to any other adapter
public class ConditionsRecyclerAdapter extends RecyclerView.Adapter<ConditionsRecyclerAdapter.ViewHolder>{
    private ArrayList <String> names; //contains the name of the conditions
    private static ArrayList<Integer> bools; // 0 or 1 depending if the condition is true
    private Context c;

    public ConditionsRecyclerAdapter(Context context) {
        c = context;
        bools = new ArrayList<>();
        names = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conditionlist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(names.get(position));
        holder.check.setClickable(true);

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //update the array
              if (isChecked) bools.set(holder.getAdapterPosition(),1);
              else bools.set(holder.getAdapterPosition(),0);
            }
        });
    }

    public static ArrayList<Integer> getBools() {
        return bools;
    }

    public static void setBools(ArrayList<Integer> bools) {
        ConditionsRecyclerAdapter.bools = bools;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public ArrayList getNames() {
        return names;
    }

    public void setNames(ArrayList names) {
        this.names = names; //set the main arraylist for the recycler view
        for (int i = 0; i < names.size(); i++) { //all false by default
            bools.add(0);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private CheckBox check;
        private int id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCondition);
            check = itemView.findViewById(R.id.cb);

        }
    }
}


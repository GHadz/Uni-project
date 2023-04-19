package com.example.main.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.main.R;

import java.util.ArrayList;
//adapter for the condition list (used differently in both ride details and condition)
public class CustomAdapter  extends BaseAdapter {

    private Context context;
    public static ArrayList<Integer> idsChecked; //ids checked used in the alert dialog arraylist
    public static ArrayList<Model> modelArrayList; //arraylist of the model of the data (condition name,boolean);
    private boolean click;

    public CustomAdapter(Context context,boolean click) {
        this.context = context;
        this.click = click;
    }
    public CustomAdapter(Context context,boolean click,CompoundButton.OnCheckedChangeListener c) {
        this.context = context;
        this.click = click;
    }

    @Override
    public int getViewTypeCount() {
        if(getCount()<1) return 1;
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    public ArrayList<Model> getModelArrayList() {
        return modelArrayList;
    }

    public void setModelArrayList(ArrayList<Model> modelArrayList) {
        CustomAdapter.modelArrayList = modelArrayList;
    }
    public static ArrayList<Integer> getIdsChecked() {
        return idsChecked;
    }

    public static void setIdsChecked(ArrayList<Integer> idsChecked) {
        CustomAdapter.idsChecked = idsChecked;
    }

    public void add(Model model)
    {
        modelArrayList.add(model);
        this.notifyDataSetChanged();
    }
    public void add(Model model,int i)
    {
        modelArrayList.add(i,model);
        this.notifyDataSetChanged();
    }
    public void remove(){

       if(!modelArrayList.isEmpty()){
           modelArrayList.remove(0);
           this.notifyDataSetChanged();
       }
    }
    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.conditionlist_item, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb);
            holder.condition = (TextView) convertView.findViewById(R.id.txtCondition);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }



        holder.condition.setText(modelArrayList.get(position).getCondition());
        holder.checkBox.setChecked(modelArrayList.get(position).getSelected());
        holder.checkBox.setClickable(click);
        if (click) {
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        idsChecked.add(position);
                        modelArrayList.get(position).setSelected(true);
                    } else {
                        int i = idsChecked.indexOf(position);
                        idsChecked.remove(i); //when unchecked
                        modelArrayList.get(position).setSelected(false);
                    }
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private CheckBox checkBox;
        private TextView condition;

    }

}

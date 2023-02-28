package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;

import java.util.ArrayList;
//adapter for the drives recycler view
public class DriveRecyclerViewAdapter extends RecyclerView.Adapter<DriveRecyclerViewAdapter.ViewHolder>{
    private ArrayList <drive> drives = new ArrayList();
    private Context c;
    private Fragment f;
    private FragmentActivity i;

    public DriveRecyclerViewAdapter(Context context, FragmentActivity i) {
    c = context;
    this.i = i;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drivelist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(drives.get(position).getDriverName());
        holder.txtCar.setText(drives.get(position).getDate());
        holder.txtDest.setText(drives.get(position).getDestination());
        holder.txtSource.setText(drives.get(position).getSource());
        String s = drives.get(position).getDriveId();
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(i,Drive_activity.class);
                intent.putExtra("id",s); //passing the drive id to the details activity so we can search for the ride
                i.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drives.size();
    }

    public ArrayList getDrives() {
        return drives;
    }

    public void setDrives(ArrayList drives) {
        this.drives = drives;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ //view holder to set the txt views and listeners
        private TextView txtName,txtCar,txtDest,txtSource;
        private CardView v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            txtCar = itemView.findViewById(R.id.textTime);
            txtDest = itemView.findViewById(R.id.textDest);
            txtSource = itemView.findViewById(R.id.textSource);
            v = itemView.findViewById(R.id.parent);
        }
    }
}

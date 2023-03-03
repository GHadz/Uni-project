package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AcceptedPassengersRecyclerAdapter extends RecyclerView.Adapter<AcceptedPassengersRecyclerAdapter.ViewHolder>{
    private ArrayList<Requests> requests;
    private final String URL ="http://"+ip+"//carpool/driver/accept.php";
    private final String URL2 ="http://"+ip+"//carpool/driver/reject.php";
    private Context c;
    private Fragment f;
    private FragmentActivity i;
    private LayoutInflater inflater;
    public AcceptedPassengersRecyclerAdapter(Context context, FragmentActivity i,LayoutInflater layoutInflater) {
        c = context;
        this.i = i;
        inflater = layoutInflater;
    }

    @NonNull
    @Override
    public AcceptedPassengersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_layout,parent,false);
        AcceptedPassengersRecyclerAdapter.ViewHolder holder = new AcceptedPassengersRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedPassengersRecyclerAdapter.ViewHolder holder, int position) {
        String a,b,c;

        a= holder.txtName.getText().toString();
        a+= requests.get(position).getPassengerName();
        c = holder.txtPhone.getText().toString();
        c+=requests.get(position).getPhone();
        holder.txtName.setText(a);
        holder.txtPhone.setText(c);
        String s = requests.get(position).getRequestID();
        holder.rating.setRating(requests.get(position).getPassRating());
        holder.txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Details about the request");
                // set the custom layout
                final View customLayout =inflater.inflate(R.layout.simple_details_layout, null);
                TextView txtDet = customLayout.findViewById(R.id.details);
                txtDet.setText(requests.get(holder.getAdapterPosition()).getDetails());
                builder.setView(customLayout);
                AlertDialog dialog = builder.create();
                dialog.show();
                Button btn = customLayout.findViewById(R.id.okBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(requests.get(holder.getAdapterPosition()).getRequestID(),holder.getAdapterPosition());
                Toast.makeText(i.getBaseContext(),"You accepted this request.",Toast.LENGTH_SHORT).show();
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject(requests.get(holder.getAdapterPosition()).getRequestID(),holder.getAdapterPosition());
                Toast.makeText(i.getBaseContext(),"You rejected this request.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public ArrayList getRequests() {
        return requests;
    }

    public void setRequests(ArrayList requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ //view holder to set the txt views and listeners
        private TextView txtName, txtDetails, txtPhone;
        private ImageButton accept,reject;
        private RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            txtPhone = itemView.findViewById(R.id.textPhone);
            txtDetails = itemView.findViewById(R.id.det);
            accept = itemView.findViewById(R.id.accept);
            reject= itemView.findViewById(R.id.reject);
            rating = itemView.findViewById(R.id.passengerRatingBar);
        }
    }


    public void accept(String reqId,int position){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success"))
                {
                    requests.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(c, "error".trim(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id",reqId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);

    }
    public void reject(String reqId,int position){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success"))
                {
                    requests.remove(position);
                    notifyDataSetChanged();
                }
                else Toast.makeText(c,"error".trim(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id",reqId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }
}

package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;
import static com.example.main.MainActivity.user_id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.example.main.MainActivity;
import com.example.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Requests> requests = new ArrayList();
    private Context c;
    private PassengerFragment frag;
    private final String URL ="http://"+ip+"//carpool/passenger/delete.php";
    public RequestsRecyclerViewAdapter(Context context,PassengerFragment f) {
        c = context;
        frag = f;
    }

    @NonNull
    @Override
    public RequestsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestlist_item,parent,false);
        RequestsRecyclerViewAdapter.ViewHolder holder = new RequestsRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(requests.get(position).getDriverName());
        holder.txtTime.setText(requests.get(position).getDate());
        holder.txtDest.setText(requests.get(position).getDestination());
        holder.txtSrc.setText(requests.get(position).getSource());
        String id1 = requests.get(position).getRequestID();
        //button to cancel the request with the listener
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success"))
                        {
                            frag.myReqButton();
                        }
                        else Toast.makeText(view.getContext(),"error".trim(),Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("id",id1);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                requestQueue.add(stringRequest);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName,txtTime,txtDest,txtSrc;
        private Button btn;
        private CardView v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            txtTime= itemView.findViewById(R.id.textTime);
            txtDest = itemView.findViewById(R.id.textDest);
            txtSrc= itemView.findViewById(R.id.textSource);
            btn = itemView.findViewById(R.id.del);
            v = itemView.findViewById(R.id.parent);
        }
    }



    public void delete(View view,int position,ViewHolder holder)
    {

    }
}

package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//adapter for the drives recycler view
public class DriveParentRecyclerAdapter extends RecyclerView.Adapter<DriveParentRecyclerAdapter.ViewHolder>{
    private final String URL ="http://"+ip+"//carpool/driver/getRequests.php";
    private final String URL3 ="http://"+ip+"//carpool/driver/acceptedRequests.php";
    private final String URL2 ="http://"+ip+"//carpool/driver/statusRide.php";
    private final String URL4 = "http://"+ip+"//carpool/passenger/conditions.php";
    private final String URL5 = "http://"+ip+"//carpool/passenger/drive_details.php";
    private boolean listDown;
    private ArrayList <Drive> drives;
    private ArrayList <Requests> requests;
    private ArrayList <Requests> requests2;
    private Boolean down;
    private CustomAdapter customAdapter;
    private Context context;
    private DriveChildRecyclerAdapter2 adapter2;
    private FragmentActivity i;
    private DriveChildRecyclerAdapter adapter;
    private LayoutInflater inflater; //for the dialog inflater
    private ListView list;
    private DriverFragment parent; //to access function from the parent fragment
    public DriveParentRecyclerAdapter(Context context, FragmentActivity i,LayoutInflater layout, DriverFragment p) {
        this.context = context;
        this.i = i;
        down = false;
        requests = new ArrayList<>();
        requests2 = new ArrayList<>();
        parent = p;
        drives = new ArrayList<>();
        adapter = new DriveChildRecyclerAdapter(context,i,layout,this);
        adapter2 = new DriveChildRecyclerAdapter2(context,i,layout);
        inflater = layout;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drive_parent_recycler_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String a,b,c;
        a = "";
        b= "";
        c= "";
        a= holder.txtStatus.getText().toString();
        if (a.equals("Ongoing")) holder.startBtn.setText(R.string.end);
        a+=drives.get(position).getStatus();
        c= holder.txtTime.getText().toString();
        c+=drives.get(position).getDate();
        b = holder.txtNbr.getText().toString();
        b+=Integer.toString(drives.get(position).getPassengerNbr());
        b+="/";
        b+=Integer.toString(drives.get(position).getCapacity());
        holder.txtStatus.setText(a);
        holder.txtTime.setText(c);
        holder.txtNbr.setText(b);
        String s = drives.get(position).getDriveId();
        //show the dialog on click
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Details about the drive");
                // set the custom layout
                final View customLayout =inflater.inflate(R.layout.activity_driver_driver, null);
                list = customLayout.findViewById(R.id.conditionList);
                getDetails(customLayout,s);
                getConditions(customLayout,s);
                builder.setView(customLayout);
                AlertDialog dialog = builder.create();
                dialog.show();
                TextView txtCondition = customLayout.findViewById(R.id.txtCondition);
                listDown = false;
                txtCondition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!listDown) {
                            list.setVisibility(View.VISIBLE);

                            listDown = true;
                        }
                        else {
                            listDown = false;
                            list.setVisibility(View.GONE);
                        }
                    }
                });
                Button btn = customLayout.findViewById(R.id.okBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
            });
            }
        });
        //cancel ride button
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "Cancelled";
                status(drives.get(holder.getAdapterPosition()).getDriveId(), holder.getAdapterPosition(),s,holder);
                Toast.makeText(v.getContext(),"Ride was cancelled",Toast.LENGTH_SHORT).show();
            }
        });
        //change the status of the ride
        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "";
                if (drives.get(holder.getAdapterPosition()).getStatus().equals("Starting soon")) s = "Ongoing";
                else if (drives.get(holder.getAdapterPosition()).getStatus().equals("Ongoing")) s = "Ended";
                status(drives.get(holder.getAdapterPosition()).getDriveId(), holder.getAdapterPosition(),s,holder );
            }
        });
        LinearLayoutManager horizontal = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        LinearLayoutManager horizontal2 = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        holder.rec.setLayoutManager(horizontal);
        holder.rec2.setLayoutManager(horizontal2);

        //get the requests
        getMyReqs(holder,holder.rec, drives.get(position).getDriveId());
        getMyAcceptedReq(holder,holder.rec2, drives.get(position).getDriveId());
        //show requests button
        holder.req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!down){
                    if(adapter.getRequests().isEmpty() && !holder.sw.isChecked()) holder.txtEmpty.setVisibility(View.VISIBLE);
                        else if (adapter2.getRequests().isEmpty() && holder.sw.isChecked()) holder.txtEmpty.setVisibility(View.VISIBLE);
                             else if(!holder.sw.isChecked()) holder.rec.setVisibility(View.VISIBLE);
                             else holder.rec2.setVisibility(View.VISIBLE);
                    holder.req.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                    down = true;
                    holder.sw.setVisibility(View.VISIBLE);
                }
                else{
                    holder.sw.setVisibility(View.GONE);
                    holder.rec.setVisibility(View.GONE);
                    holder.rec2.setVisibility(View.GONE);
                    holder.txtEmpty.setVisibility(View.GONE);
                    holder.req.setImageResource(R.drawable.ic_baseline_arrow_drop_down_white);
                    down = false;
                }
            }
        });
        //switch between pending and accepted requests
        holder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.txtEmpty.setVisibility(View.GONE);
                if(adapter.getRequests().isEmpty() && !holder.sw.isChecked()) holder.txtEmpty.setVisibility(View.VISIBLE);
                else if (adapter2.getRequests().isEmpty() && holder.sw.isChecked()) {
                    holder.txtEmpty.setVisibility(View.VISIBLE);

                }
                else if(!holder.sw.isChecked()) holder.rec.setVisibility(View.VISIBLE);
                else holder.rec2.setVisibility(View.VISIBLE);
                holder.req.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                down = true;
                holder.sw.setVisibility(View.VISIBLE);
                if (isChecked){

                    holder.sw.setText(R.string.accepted);
                    holder.rec2.setVisibility(View.VISIBLE);
                    holder.rec.setVisibility(View.GONE);

                }
                else {

                    holder.sw.setText(R.string.pending);
                    holder.rec.setVisibility(View.VISIBLE);
                    holder.rec2.setVisibility(View.GONE);
                }
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

    public class ViewHolder extends RecyclerView.ViewHolder { //view holder to set the txt views and listeners
        private TextView txtStatus, txtTime, txtNbr,txtEmpty,txtHeader;
        private CardView v;
        private Button btn,startBtn;
        private RecyclerView rec,rec2;
        private ImageButton req;
        private SwitchCompat sw;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStatus = itemView.findViewById(R.id.txtTime);
            txtTime = itemView.findViewById(R.id.txtdest);
            txtNbr = itemView.findViewById(R.id.txtCount);
            v = itemView.findViewById(R.id.parent);
            btn = itemView.findViewById(R.id.del);
            startBtn = itemView.findViewById(R.id.start);
            rec = itemView.findViewById(R.id.child);
            rec2 = itemView.findViewById(R.id.child2);
            req = itemView.findViewById(R.id.btnReq);
            txtEmpty = itemView.findViewById(R.id.txtEmpty);
            sw = itemView.findViewById(R.id.listHeader);
        }
    }

    public DriveChildRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void addAccepted(Requests r)
    {
        adapter2.addRequests(r);
    }


    public void getMyAcceptedReq(ViewHolder h, RecyclerView r, String driveId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONArray array;
                try {

                    array = new JSONArray(response);
                    String id,name,det,phone;
                    int rating;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        id = o.getString("id");
                        name = o.getString("name");
                        phone = o.getString("phone");
                        det = o.getString("details");
                        rating = o.getInt("rating");
                        requests2.add(new Requests(id,name,rating ,det,phone));
                    }
                    adapter2.setRequests(requests2);
                    r.setAdapter(adapter2);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(i.getBaseContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(i.getBaseContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", driveId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void getMyReqs(ViewHolder h, RecyclerView r, String driveId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONArray array;
                try {

                    array = new JSONArray(response);
                    String id,name,det,phone;
                    int rating;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        id = o.getString("id");
                        name = o.getString("name");
                        phone = o.getString("phone");
                        det = o.getString("details");
                        rating = o.getInt("rating");
                        requests.add(new Requests(id,name,rating ,det,phone));
                    }
                    adapter.setRequests(requests);
                   r.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(i.getBaseContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(i.getBaseContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", driveId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //change status, function works for cancel,start and end ride
    public void status(String rideId,int position,String status,ViewHolder h){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success"))
                {
                    if(status.equals("Cancelled")) {
                        drives.remove(position);
                        Toast.makeText(context,"Ride was cancelled.",Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals("Ongoing")) {
                        h.txtStatus.setText("Status:".concat(status));
                        h.startBtn.setText(R.string.end);
                        drives.get(position).setStatus(status);
                        Toast.makeText(context,"Ride has started.",Toast.LENGTH_SHORT).show();
                    }
                    else if(status.equals("Ended")) {
                        drives.remove(position);
                        Toast.makeText(context,"Ride has ended.",Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    parent.checkEmpty();
                }
                else Toast.makeText(context,"error".trim(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id",rideId);
                data.put("status",status);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
//get details when we click on ride container
    public void getDetails(@NonNull View v, String id){
        TextView txtSrc = v.findViewById(R.id.txtSrc);
        TextView txtDest = v.findViewById(R.id.txtDest);
        TextView txtDet = v.findViewById(R.id.txtDet);
        String s,des,det;
s= txtSrc.getText().toString(); des =txtDest.getText().toString();det =txtDet.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL5, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                JSONArray array;
                try {
                    array = new JSONArray(response);
                    String name, date, dest, details,source,phone,seats;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        details =det+ o.getString("details");
                        source = s+ o.getString("source");
                        dest = des + o.getString("destination");
                        txtSrc.setText(source);
                        txtDest.setText(dest);
                        txtDet.setText(details);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
//get the conditions
    public void getConditions(View v, String id) {


        ArrayList<Model> listItems;
        listItems = new ArrayList<>();

        customAdapter = new CustomAdapter(context,false);
        customAdapter.setModelArrayList(listItems);
        list.setAdapter(customAdapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray array;

                try {
                    array = new JSONArray(response);
                    boolean b; String cond; int d;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        cond = o.getString("name");
                        d = o.getInt("bool");
                        if(d==0)b=false;
                        else b=true;
                        customAdapter.add(new Model(b,cond));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        customAdapter.remove();

    }

}


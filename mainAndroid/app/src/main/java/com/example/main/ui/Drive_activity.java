package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;
import static com.example.main.MainActivity.user_id;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
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

public class Drive_activity extends AppCompatActivity {
private ArrayList<Model> listItems;
private int z; //passes number of seats to dialogue
private EditText subDet;
private boolean listDown; //check if condition list is down or up
private CustomAdapter adapter;
private Button button;
private RatingBar rating;
private ListView list;
private NumberPicker nbrPick;
private String id;
private int countAccepted,countPending;
private String n,d,s,des,det,sea,p; //the texts in txt boxes

private final String URL = "http://"+ip+"//carpool/passenger/drive_details.php";
private final String URL1 = "http://"+ip+"//carpool/passenger/conditions.php";
private final String URL2 = "http://"+ip+"//carpool/passenger/requestRide.php";
private final String URL3 = "http://"+ip+"//carpool/passenger/cancelReq.php";
private final String URL4 = "http://"+ip+"//carpool/passenger/pendingReq.php";
private TextView txtDName,txtStart,txtSrc,txtDest,txtDet,txtSeats,txtPhone,txtCondition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //passing the drive id
        list = findViewById(R.id.conditionList);
        button = findViewById(R.id.reqBtn);
        rating = findViewById(R.id.ratingBar);
        listDown = false;
        txtCondition = findViewById(R.id.txtCondition);
        txtDName = findViewById(R.id.txtPassengers);
        txtStart = findViewById(R.id.txtStart);
        txtSrc = findViewById(R.id.txtSrc);
        txtDest = findViewById(R.id.txtDest);
        txtDet = findViewById(R.id.txtDet);
        txtSeats = findViewById(R.id.txtSeats);
        txtPhone = findViewById(R.id.txtPhone);

        //get content of txtView
        n=txtDName.getText().toString(); d =txtStart.getText().toString();p =txtPhone.getText().toString(); s= txtSrc.getText().toString(); des =txtDest.getText().toString();det =txtDet.getText().toString(); sea = txtSeats.getText().toString();
        onOpen();

        listItems=new ArrayList<>();
        getConditions();
        //shows and hides conditions list
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
    }
    // on open fetches all the ride details from the database and prepares the main button of the page (text and on click listener)
    public void onOpen(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONArray array;


                try {
                    array = new JSONArray(response);
                    String name, date, dest, details,source,phone,seats;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        name =n+ o.getString("name");
                        date = d+o.getString("date");
                        details =det+ o.getString("details");
                        source = s+ o.getString("source");
                        dest = des + o.getString("destination");
                        phone = p + o.getString("phone");
                        seats = sea + o.getString("seats");
                        countAccepted = o.getInt("countAccepted");
                        pendingReq();
                      //  nbrPick.setMaxValue(o.getInt("seats"));
                        z = o.getInt("seats");
                        rating.setRating(o.getInt("rating"));
                        txtSeats.setText(seats);
                        txtDName.setText( name);
                        txtStart.setText(date);
                        txtSrc.setText(source);
                        txtDest.setText(dest);
                        txtDet.setText(details);
                        txtPhone.setText(phone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Drive_activity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Drive_activity.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("userId",user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Drive_activity.this);
        requestQueue.add(stringRequest);

    }


    //get pending req count (didn't work in same query) main button used inside it to check for pending requests
    public void pendingReq()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("failure"))
                {countPending = 0;
                }
                else {

                    countPending = Integer.parseInt(response);
                }
                mainButton();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Drive_activity.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("userId",user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Drive_activity.this);
        requestQueue.add(stringRequest);
    }

    //gets the ride conditions that can be displayed when clicked on
    public void getConditions() {

        adapter = new CustomAdapter(Drive_activity.this,false);
        adapter.setModelArrayList(listItems);
        list.setAdapter(adapter);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
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
                        adapter.add(new Model(b,cond));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Drive_activity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Drive_activity.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Drive_activity.this);
        requestQueue.add(stringRequest);
        adapter.remove();

    }

    /*used inside onOpen
    checks if a request has been made for this ride, if its been accepted gives the option to leave the ride,
    if its pending we can cancel it, else allows us to make request by showing required fields for request*/
    public void mainButton()
    {
        if(countAccepted > 0)
            button.setText(R.string.leave);
        else if (countPending > 0)
            button.setText(R.string.cancel);
        else button.setText(R.string.make);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countAccepted > 0){
                    leaveRide();
                }
                else if (countPending > 0)
                {
                    cancelReq();
                }
                else showDet();
            }
        });

    }
    //function used in mainButton to show Dialogue where u can make the request
    public void showDet()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.reqdialogue_layout, null);
        builder.setView(customLayout);
        subDet= customLayout.findViewById(R.id.inDetails);
        nbrPick = customLayout.findViewById(R.id.nbrPicker);
        nbrPick.setMinValue(1);
        nbrPick.setMaxValue(z);
        Button submit = customLayout.findViewById(R.id.submitBtn);
        Button cancel = customLayout.findViewById(R.id.cancelBtn);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                makeReq(dialog);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //makes the req and inserts into database
    public void makeReq(Dialog dialog)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if(response.equals("success"))
            {
                Toast.makeText(Drive_activity.this,"Request Sent",Toast.LENGTH_SHORT).show();
                onOpen();
            }
            else Toast.makeText(Drive_activity.this,"Some error happened",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Drive_activity.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                String a = subDet.getText().toString();
                int b = nbrPick.getValue();
                String c = Integer.toString(b);
                data.put("id", id);
                data.put("userId",user_id);
                data.put("details",a);
                data.put("nbr",c);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Drive_activity.this);
        requestQueue.add(stringRequest);

    }
    //cancel a pending request
    public void cancelReq()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success"))
                {
                    Toast.makeText(Drive_activity.this,"Request has been cancelled",Toast.LENGTH_SHORT).show();
                    countPending--;
                    mainButton();
                }
                else Toast.makeText(Drive_activity.this,"Some error happened",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Drive_activity.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("userId",user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Drive_activity.this);
        requestQueue.add(stringRequest);
        onOpen();
    }
    //cancel an accepted request
    public void leaveRide() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(Drive_activity.this, "You have left the ride", Toast.LENGTH_SHORT).show();
                    countAccepted--;
                    mainButton();
                } else
                    Toast.makeText(Drive_activity.this, "Some error happened", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Drive_activity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("userId", user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Drive_activity.this);
        requestQueue.add(stringRequest);
        onOpen();
    }
}
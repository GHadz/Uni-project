package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;
import static com.example.main.MainActivity.user_id;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DriverFragment extends Fragment {
private TextView txtUp,txt,fanar,subDet;
private NumberPicker max;
private final String URL3 ="http://"+ip+"//carpool/driver/myDrives.php";
private final String URL4 ="http://"+ip+"//carpool/driver/getLocations.php";
private final String URL5 ="http://"+ip+"//carpool/passenger/getAllConditions.php";
private final String URL6 ="http://"+ip+"//carpool/driver/insertRide.php";
private final String URL7 ="http://"+ip+"//carpool/driver/addConditions.php";
private HashMap<String,String> locationId;
private ArrayList<String> location,condition,ids;
private ArrayList<Drive> drives;
private boolean conditionsDown,toFanar;
private RecyclerView  conditionRecycler,drivesRecycler;
private ArrayAdapter<String> adapter;
private View v;
private Spinner drop;
private ImageButton btnDrop,switchBtn,addBtn;
private Button start;
private ConditionsRecyclerAdapter conRecAdapter;
private DriveParentRecyclerAdapter driveAdapter;
private TimePicker timePicker;
private String driveDetails;
private DatePicker picker;
private String yearVar,monthVar,dayVar,hour,minutes,fullDate;
private String maxCapacity;
private String sIds,sConds;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_driver,container,false);
        driveDetails = fullDate = maxCapacity = "";
        conditionsDown = true;
        toFanar = true;
        txt = v.findViewById(R.id.dropTxt);
        fanar = v.findViewById(R.id.Fanar);
        txtUp = v.findViewById(R.id.txtUp);
        drop = v.findViewById(R.id.dropDown);
        btnDrop = v.findViewById(R.id.dropBtn);
        switchBtn = v.findViewById(R.id.btn);
        addBtn=v.findViewById(R.id.plusBtn);
        conditionRecycler = v.findViewById(R.id.selectedConditions);
        drivesRecycler = v.findViewById(R.id.driveList);
        conRecAdapter = new ConditionsRecyclerAdapter(v.getContext());
        driveAdapter = new DriveParentRecyclerAdapter(v.getContext(),getActivity(),getLayoutInflater(),this);
        drivesRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        onOpen();
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toFanar){txt.setText(R.string.to); fanar.setText(R.string.from); toFanar =false;}
                else {fanar.setText(R.string.to); txt.setText(R.string.from); toFanar = true;}
            }
        });
        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conditionsDown) {
                    conditionRecycler.setVisibility(View.GONE);
                    conditionsDown = false;
                }
                else {
                    conditionRecycler.setVisibility(View.VISIBLE);
                    conditionsDown = true;
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDet();
            }
        });
        start = v.findViewById(R.id.newBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRide();
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onOpen()
    {

        getMyDrives();
        getLocations();
        getConditions();
    }

    //get the locations for the spinner
    public void getLocations()
    {
        location = new ArrayList<>();
        locationId = new HashMap<>();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                location.add("Select Location");
                JSONArray array;
                try {
                    array = new JSONArray(response);
                    String name,locId;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        name = o.getString("name");
                        location.add(name);
                        locId = o.getString("id");
                        locationId.put(name,locId);
                    }
                    adapter = new ArrayAdapter<String>(v.getContext(), R.layout.custom_spinner,location);
                    drop.setAdapter(adapter);//set the array that was imported from the database for the list
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error2",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
    //get the conditions to be displayed in alert dialog
    public void getConditions()
    {

        condition = new ArrayList<>();
        ids = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL5, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array;
                try {
                    array = new JSONArray(response);
                    String name,id;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        id = o.getString("id");
                        name = o.getString("name");
                        condition.add(name);
                        ids.add(id);
                    }
                    conRecAdapter.setNames(condition);

                    conditionRecycler.setAdapter(conRecAdapter);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error3",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
    //get the drives that have already been inserted in database
    public void getMyDrives()
    {
        drives = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                JSONArray array;
                try {
                    array = new JSONArray(response);
                    String id,date,status;
                    int nbr,max;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                    id = o.getString("id");
                    date = o.getString("date");
                    status = o.getString("status");
                    nbr = o.getInt("nbrPass");
                    max = o.getInt("Capacity");
                    drives.add(new Drive(id,date,nbr,status,max));
                    }

                    driveAdapter.setDrives(drives);
                    drivesRecycler.setAdapter(driveAdapter);
                    checkEmpty();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
    //insert new ride
    public void insertRide() {
        //check if everything was inputted
        if (maxCapacity.equals("") || driveDetails.equals("") || fullDate.equals("")) {
            Toast.makeText(v.getContext(), "Enter all required details", Toast.LENGTH_SHORT).show();
            addBtn.setBackgroundTintList((ContextCompat.getColorStateList(getContext(), R.color.red)));
            changeColor();
        } else if (drop.getSelectedItem().toString().equals("Select Location")) {
            Toast.makeText(v.getContext(), "Enter source and destination.", Toast.LENGTH_SHORT).show();
            txt.setTextColor(getResources().getColor(R.color.red));
            addBtn.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txt.setTextColor(getResources().getColor(R.color.blue));
                }
            }, 1500);

        }
        else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL6, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("failure")) {
                        Toast.makeText(v.getContext(), "Something went wrong....", Toast.LENGTH_SHORT).show();
                    } else {
                    insertConditions(response);
                        Toast.makeText(v.getContext(), "New ride has been added", Toast.LENGTH_SHORT).show();
                        driveDetails = fullDate = maxCapacity = "";
                        addBtn.setImageResource(R.drawable.ic_baseline_add_24);
                        getMyDrives();
                        checkEmpty();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("userId", user_id);
                    data.put("date", fullDate);
                    data.put("details", driveDetails);
                    data.put("max", maxCapacity);

                        String dropLoc = drop.getSelectedItem().toString();
                        if (toFanar) {
                            data.put("src", locationId.get(dropLoc));
                            data.put("dest", locationId.get("Fanar"));
                        } else {
                            data.put("dest", locationId.get(dropLoc));
                            data.put("src", locationId.get("Fanar"));
                        }

                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            requestQueue.add(stringRequest);
        }
    }
    public void insertConditions(String rideId)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL7, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("failure"))
                {
                    Toast.makeText(v.getContext(),"Something went wrong....",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("rideId",rideId);
                sIds=sConds= "";
                if (!conRecAdapter.getNames().isEmpty()) //pass variable as one string that we split inside the php
                {
                    for (int i = 0; i < ids.size(); i++) {
                        if(sIds.equals("")) sIds = ids.get(i);
                        else {
                            sIds += ',';
                            sIds += ids.get(i);
                        }
                        if(sConds.equals("")) sConds = conRecAdapter.getBools().get(i).toString();
                        else {
                            sConds += ',';
                            sConds += conRecAdapter.getBools().get(i).toString();
                        }
                    }
                }
                data.put("condId",sIds);
                data.put("condBool",sConds);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
    public void showDet()
    {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Details");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.add_drive_dialog_layout, null);
        builder.setView(customLayout);
        max = customLayout.findViewById(R.id.numb);
        max.setMaxValue(20);
        subDet= customLayout.findViewById(R.id.inDetails);
        picker = customLayout.findViewById(R.id.editTextDate);
        picker.setMinDate(calendar2.getTimeInMillis());
        picker.setMaxDate(calendar.getTimeInMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    yearVar =Integer.toString(year);
                    monthVar = Integer.toString(monthOfYear);
                    dayVar = Integer.toString(dayOfMonth);
                }
            });
        }
        timePicker = customLayout.findViewById(R.id.timePicker);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = Integer.toString(hourOfDay);
                minutes =Integer.toString(minute);
            }
        });
        Button submit = customLayout.findViewById(R.id.submitBtn);
        Button cancel = customLayout.findViewById(R.id.cancelBtn);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearVar = Integer.toString(picker.getYear());
                monthVar = Integer.toString(picker.getMonth());
                dayVar = Integer.toString(picker.getDayOfMonth());
                fullDate = yearVar+"-"+monthVar+"-"+dayVar+" "+hour+":"+minutes+":"+"00";
                driveDetails = subDet.getText().toString();
                maxCapacity = Integer.toString(max.getValue());
                addBtn.setImageResource(R.drawable.ic_baseline_check_24);
                dialog.dismiss();

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
    public void checkEmpty(){
        if(drives.isEmpty()) {
            txtUp.setVisibility(View.VISIBLE);
            txtUp.setText(R.string.myridesEmpty);
        }
        else txtUp.setVisibility(View.GONE);
    }

    public void changeColor()
    {
        addBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                addBtn.setBackgroundTintList((ContextCompat.getColorStateList(getContext(), R.color.blue)));
            }
        }, 1500);
    }
}
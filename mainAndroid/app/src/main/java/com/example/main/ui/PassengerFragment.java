package com.example.main.ui;


import static com.example.main.LOGIN.Login_SignUp_Main.ip;
import static com.example.main.MainActivity.user_id;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PassengerFragment extends Fragment {
private Spinner drop;
private String sIds,sConds; //conditions and ids to be searched
private RecyclerView reqRecycler, conditionRecycler,drivesRecycler;
private ArrayList<drive> drives;
private ArrayList<String> location,condition,ids,addedIds;
private ArrayList<Model> conditionsModel;
private ArrayList<Requests> requests;
private final String URL ="http://"+ip+"//carpool/passenger/searchRide.php";
private final String URL2 ="http://"+ip+"//carpool/passenger/myRequestsAccepted.php";
private final String URL3 ="http://"+ip+"//carpool/passenger/myRequests.php";
private final String URL4 ="http://"+ip+"//carpool/passenger/getLocations.php";
private final String URL5 ="http://"+ip+"//carpool/passenger/getAllConditions.php";
private final String URL6 ="http://"+ip+"//carpool/passenger/searchWithConditions.php";
private Button searchBtn,myReq;
private ImageButton btn,dropDown,add;
private TextView txt,fanar,txtReq,txtUp,txtNoCond;
private ArrayAdapter<String> adapter;
private View v;
private DriveRecyclerViewAdapter RecAdapter;
private RequestsRecyclerViewAdapter RecAdapter2;
private ConditionsRecyclerAdapter conRecAdapter;
private CustomAdapter listAdapter;
private LinearLayoutManager horizontal;
private boolean toFanar,conditionsDown;



//create the view for the fragment then return it after initialising everything
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_passenger,container,false);
        drop = v.findViewById(R.id.dropDown);
        btn = v.findViewById(R.id.btn);
        searchBtn = v.findViewById(R.id.searchBtn);
        add = v.findViewById(R.id.plusBtn);
        dropDown = v.findViewById(R.id.dropBtn);
        myReq = v.findViewById(R.id.myReqs);
        txt = v.findViewById(R.id.textView2);
        fanar = v.findViewById(R.id.Fanar);
        txtNoCond = v.findViewById(R.id.noCond);
        txtReq = v.findViewById(R.id.txtReq);
        txtUp = v.findViewById(R.id.txtUp);
        toFanar= true;
        conditionsDown =false; //booleans to know if the lists are down or up
        getLocations();
        getConditions();
        add.setOnClickListener(new View.OnClickListener() { //display alert dialog to make request
            @Override
            public void onClick(View v) {
                showDet();
            }
        });
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConditions();

            }
        });
        drivesRecycler = v.findViewById(R.id.driveList);
        conditionRecycler = v.findViewById(R.id.selectedConditions);
        reqRecycler = v.findViewById(R.id.reqList);
        drives = new ArrayList<>();
        requests = new ArrayList<>();
        RecAdapter2 =  new RequestsRecyclerViewAdapter(v.getContext(),this);
        RecAdapter =  new DriveRecyclerViewAdapter(v.getContext(),getActivity());
        conRecAdapter = new ConditionsRecyclerAdapter(v.getContext());
        horizontal = new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,false);
        conditionRecycler.setLayoutManager(horizontal);
        drivesRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        reqRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        //switches between from uni to "to uni"
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toFanar){txt.setText(R.string.to); fanar.setText(R.string.from); toFanar =false;}
                else {fanar.setText(R.string.to); txt.setText(R.string.from); toFanar = true;}
            }
        });

        //searches for the ride with requirements
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drives = new ArrayList<>();
                requests = new ArrayList<>();
                RecAdapter.setDrives(drives);
                RecAdapter2.setRequests(requests);
                txtReq.setVisibility(View.INVISIBLE);
                txtUp.setVisibility(View.INVISIBLE);
                if (conRecAdapter.getNames().isEmpty()) search();
                else searchWithConditions();

            }
        });
        //check my request and upcoming rides
        myReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          myReqButton();

            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

//search for a ride
    public void search()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array;
                try {

                    array = new JSONArray(response);
                    String name, date,source, dest,id1;
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        id1 = o.getString("id");
                        name = o.getString("name");
                        date = o.getString("date");
                        source = o.getString("src");
                        dest = o.getString("dest");
                        drives.add(new drive(id1, name,date, source,dest));
                    }
                    if (drives.isEmpty()){txtUp.setText(R.string.noRide); txtUp.setVisibility(View.VISIBLE);}
                    else txtUp.setVisibility(View.INVISIBLE);//displays if there is no match
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
                }

                RecAdapter =  new DriveRecyclerViewAdapter(v.getContext(),getActivity());
                RecAdapter.setDrives(drives);
                drivesRecycler.setAdapter(RecAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(v.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                String dropLoc;
                if (!drop.getSelectedItem().toString().equals("No Location")) {
                    dropLoc = drop.getSelectedItem().toString();
                    if (toFanar) {
                        data.put("src", dropLoc);
                        data.put("dest", "Fanar");
                    } else {
                        data.put("dest", drop.getSelectedItem().toString());
                        data.put("src", "Fanar");
                    }
                }
                else
                {
                    if (toFanar) {
                        data.put("dest", "Fanar");
                    } else {
                        data.put("src", "Fanar");
                    }
                }
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }

    //find the request that were accepted = upcoming rides
    public void acceptedRequests()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray array;

                try {
                    array = new JSONArray(response);
                    String name, date,source, dest,id1;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        id1 = o.getString("id");
                        name = o.getString("name");
                        date = o.getString("date");
                        source = o.getString("src");
                        dest = o.getString("dest");
                        drives.add(new drive(id1, name,date, source,dest));
                    }
                    if (drives.isEmpty()){txtUp.setText(R.string.noUp);}
                    else txtUp.setText(R.string.upcoming_rides);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
                }

                RecAdapter.setDrives(drives);
                drivesRecycler.setAdapter(RecAdapter);
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
                Map<String, String> data = new HashMap<>();
                data.put("id", user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
//pending requests
    public void myRequests()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray array;

                try {
                    array = new JSONArray(response);
                    String id1,name, date, dest,src;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        id1 = o.getString("id");
                        name = o.getString("name");
                        date = o.getString("date");
                        dest = o.getString("dest");
                        src = o.getString("src");
                        requests.add(new Requests(id1,name,date,dest,src));
                    }
                    if (requests.isEmpty()){txtReq.setText(R.string.noReq);}//display if no match
                    else txtReq.setText(R.string.pending_requests);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
                }

                RecAdapter2.setRequests(requests);
                reqRecycler.setAdapter(RecAdapter2);
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
                Map<String, String> data = new HashMap<>();
                data.put("id", user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }
    // button that manages the above 2
    public void myReqButton(){
    drives = new ArrayList<>();
    requests = new ArrayList<>();
    RecAdapter.setDrives(drives);
    RecAdapter2.setRequests(requests);
    txtReq.setVisibility(View.VISIBLE);
    txtUp.setVisibility(View.VISIBLE);
    myRequests();
    acceptedRequests();


}

//get the locations for the spinner
    public void getLocations()
    {
        location = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                location.add("No Location");
                JSONArray array;
                try {
                    array = new JSONArray(response);
                    String name;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        name = o.getString("name");
                        location.add(name);
                    }
            adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item,location);
                    drop.setAdapter(adapter);//set the array that was imported from the database for the list
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
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
        conditionsModel = new ArrayList<>();
        condition = new ArrayList<>();
        ids = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL5, new Response.Listener<String>() {
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
                conditionsModel.add(new Model(false,name));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
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
//show the dialog and build it with a list and button click listners
    public void showDet()
    {
        ArrayList<Integer> idsChecked = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Add Conditions");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.pickconditions_layout, null);
        builder.setView(customLayout);

        Button submit = customLayout.findViewById(R.id.submitBtn);
        Button cancel = customLayout.findViewById(R.id.cancelBtn);
        ListView list = customLayout.findViewById(R.id.conditionList);
        listAdapter = new CustomAdapter(v.getContext(),true);
        listAdapter.setModelArrayList(conditionsModel);
        listAdapter.setIdsChecked(idsChecked);
        list.setAdapter(listAdapter);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a,b;
                addedIds = new ArrayList<>();
                ArrayList<String> tmp = new ArrayList<>();
                for (int i = 0; i < idsChecked.size(); i++) {
                    a=condition.get(idsChecked.get(i)); //to get the names of the checked conditions we want in our search
                    b=ids.get(idsChecked.get(i)); //position in list and array are the same so we use the position to find the id of the location
                    addedIds.add(b); // saves only the added locations
                    tmp.add(a);
                }
                conRecAdapter.setNames(tmp);
                conditionRecycler.setAdapter(conRecAdapter);
                showConditions();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
//drop down list of conditions
    public void showConditions(){

        if (conditionsDown)
        {

            conditionRecycler.setVisibility(View.GONE);
            conditionsDown =false;
            txtNoCond.setVisibility(View.GONE);
        }
        else {
            conditionRecycler.setVisibility(View.VISIBLE);
            if (conRecAdapter.getNames().isEmpty())txtNoCond.setVisibility(View.VISIBLE);
            conditionsDown = true;
        }
    }
//search with conditions added
    public void searchWithConditions()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL6, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array;
                try {

                    if (response.equals("failure"))
                    {
                        Toast.makeText(v.getContext(),"Some error happened",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.equals("empty")){
                        if (drives.isEmpty()){txtUp.setText(R.string.noRide); txtUp.setVisibility(View.VISIBLE);}
                        else txtUp.setVisibility(View.INVISIBLE);//displays if there is no match
                        return;
                    }
                    array = new JSONArray(response);
                    String name, date,source, dest,id1;
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        id1 = o.getString("id");
                        name = o.getString("name");
                        date = o.getString("date");
                        source = o.getString("src");
                        dest = o.getString("dest");
                        drives.add(new drive(id1, name,date, source,dest));
                    }
                    if (drives.isEmpty()){txtUp.setText(R.string.noRide); txtUp.setVisibility(View.VISIBLE);}
                    else txtUp.setVisibility(View.INVISIBLE);//displays if there is no match
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
                }

                RecAdapter =  new DriveRecyclerViewAdapter(v.getContext(),getActivity());
                RecAdapter.setDrives(drives);
                drivesRecycler.setAdapter(RecAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(v.getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                String dropLoc;
                if (!drop.getSelectedItem().toString().equals("No Location")) {
                    dropLoc = drop.getSelectedItem().toString();
                    if (toFanar) {
                        data.put("src", dropLoc);
                        data.put("dest", "Fanar");
                    } else {
                        data.put("dest", drop.getSelectedItem().toString());
                        data.put("src", "Fanar");
                    }
                }
                else
                {
                    if (toFanar) {
                        data.put("dest", "Fanar");
                    } else {
                        data.put("src", "Fanar");
                    }
                }

                sIds=sConds= "";
                if (!conRecAdapter.getNames().isEmpty()) //pass variable as one string that we split inside the php
                {
                    for (int i = 0; i < addedIds.size(); i++) {
                       if(sIds.equals("")) sIds = addedIds.get(i);
                       else {
                           sIds += ',';
                           sIds += addedIds.get(i);
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
}

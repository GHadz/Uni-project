package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;
import static com.example.main.MainActivity.user_id;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.ScrollView;
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
import java.util.List;
import java.util.Map;

public class ReviewFragment extends Fragment {
private final String URL ="http://"+ip+"//carpool/reviews/usersToReview.php";
private final String URL1 ="http://"+ip+"//carpool/reviews/makeReview.php";
private final String URL2 ="http://"+ip+"//carpool/reviews/myReviews.php";
private RecyclerView recyclerView;
private View v;
private SearchView searchView;
private ListView listView;
private EditText det;
private Button submit;
private NumberPicker numberPicker;
private RatingBar ratingBar;
private ArrayAdapter<String> adapter;
private ArrayList<String> listArray,ids,users,types,rideIds;
private ReviewsRecyclerAdapter reviewsAdapter;
private ArrayList<Review> reviews;
private ConstraintLayout scrollView;
private String chosenId,chosenType,chosenDrive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_review, container, false);
        chosenId = chosenDrive = chosenType = "";
        searchView = v.findViewById(R.id.searchView);
        recyclerView = v.findViewById(R.id.recyclerReviews);
        listView = v.findViewById(R.id.searchList);
        listView.setVisibility(View.GONE);
        submit = v.findViewById(R.id.submitBtn);
        scrollView = v.findViewById(R.id.parent);
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.clearFocus();
                scrollView.requestFocus();
            }
        });
        det = v.findViewById(R.id.imageButton);
        ratingBar = v.findViewById(R.id.RatingBar);
        ratingBar.setMax(5);
        numberPicker = v.findViewById(R.id.nbrPicker);
        numberPicker.setMaxValue(5);

        myReviews();
        findUsers();

        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                ratingBar.setRating(numberPicker.getValue());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenId = ids.get(position);
                chosenType = types.get(position);
                chosenDrive = rideIds.get(position);
                searchView.setQuery(listArray.get(position),false);
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    listView.setVisibility(View.GONE);
                }

            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReq();
            }
        });
        return v;
    }
    public void findUsers()
    {
        ids = new ArrayList<>(); users= new ArrayList<>(); types= new ArrayList<>(); rideIds= new ArrayList<>();
        listArray = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONArray array;
                try {

                    array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ids.add(o.getString("id"));
                        users.add(o.getString("name"));
                        types.add(o.getString("type"));
                        rideIds.add(o.getString("rideId"));
                        listArray.add(o.getString("name") +" ("+o.getString("type")+")");
                    }

                    adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1,listArray);
                    listView.setAdapter(adapter);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            searchView.clearFocus();
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            listView.setVisibility(View.VISIBLE);
                            adapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public void makeReq()
    {
        if (!chosenId.equals("") && !chosenType.equals("") && !det.getText().toString().equals(""))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (response.equals("success"))
                    {
                        Toast.makeText(v.getContext(),"Review made successfully!",Toast.LENGTH_SHORT).show();
                    }
                    else if (response.equals("failure"))
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
                    data.put("userId",user_id);
                    data.put("reviewedId",chosenId);
                    data.put("details",det.getText().toString());
                    data.put("rideId",chosenDrive);
                    data.put("type",chosenType);
                    data.put("rating",Float.toString(ratingBar.getRating()));
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            requestQueue.add(stringRequest);
        }
        else  Toast.makeText(v.getContext(),"Make sure to fill in all the fields.",Toast.LENGTH_SHORT).show();
    }
    public void myReviews()
    {
        reviews = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONArray array;
                try {

                    array = new JSONArray(response);
                    String det,name,type; int rate;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        det = o.getString("details");
                        name = o.getString("name");
                        type = o.getString("type");
                        rate = o.getInt("rating");
                        reviews.add(new Review(det,name,type,rate));

                    }
                    reviewsAdapter = new ReviewsRecyclerAdapter(getLayoutInflater(),getActivity());
                    reviewsAdapter.setReviews(reviews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
                    recyclerView.setAdapter(reviewsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
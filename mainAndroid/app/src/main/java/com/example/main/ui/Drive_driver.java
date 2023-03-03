package com.example.main.ui;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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

public class Drive_driver extends AppCompatActivity {

private final String URL2 = "http://"+ip+"//carpool/driver/acceptedPassengers.php";



private String id;
private String n,d,s,des,det,sea,p; //the texts in txt boxes
private TextView txtStart,txtSrc,txtDest,txtDet,txtSeats,txtCondition;

private int z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_driver);
        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //passing the drive id


//shows and hides conditions list


    }


    public void getPassengers()
    {

    }

}
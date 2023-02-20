package com.example.main;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.main.LOGIN.Login_SignUp_Main;
import com.example.main.ui.drive;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.main.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static String user_id; //id to be passed to all the fragments/classes
    private Intent intent;
    private Button btn;
    private String Url = "http://"+ip+"//carpool/passenger/header.php";
    private TextView username,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        user_id = intent.getStringExtra("id"); //get the id from the login
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        btn = findViewById(R.id.logout);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View header = navigationView.getHeaderView(0);//get the header of the nav view so we can edit the text in it
        username = header.findViewById(R.id.username);
        email = header.findViewById(R.id.email);
        details();
        /*Passing each menu ID as a set of Ids because each
        menu should be considered as top level destinations.*/
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_passenger, R.id.nav_driver, R.id.nav_account)
                .setOpenableLayout(drawer)
                .build(); //each fragment id from navigation xml
        /* control the navigation from the fragments through
         the nav bar and drawer*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        btn.setOnClickListener(new View.OnClickListener() { //logout
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login_SignUp_Main.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void details()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override //response function
            public void onResponse(String response) {  //if php echoes failure then password is wrong
                    JSONArray array;
                    try {
                        array = new JSONArray(response);
                        String name,emailString;
                        name = emailString ="";
                        for (int i = 0; i < array.length(); i++)
                        {
                            JSONObject o = array.getJSONObject(i);
                            name = o.getString("name");
                            emailString = o.getString("email");

                        }
                        username.setText(name);
                        email.setText(emailString);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }

            }
        }, new Response.ErrorListener() {
            @Override //error listener function in case connection doesn't work
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            //if all goes well and connection happens then we override the getParams function so it puts email and password in php variable
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id", user_id); //email will server as the id so the php file finds it using POST

                return data;
            }
        };
        //setup requestQueue then add the string request to it
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}
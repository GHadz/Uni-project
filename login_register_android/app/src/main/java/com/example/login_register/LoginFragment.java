package com.example.login_register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private EditText inEmail, inPassword;
    private TextView txt;
    private Button button;
    private String email, password;
    private String Url = "http://192.168.1.108//login/login.php";
    private View.OnClickListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);
        email = password = "";
        inEmail = view.findViewById(R.id.inEmail);
        inPassword = view.findViewById(R.id.inPassword);
        button = view.findViewById(R.id.button);
        txt = view.findViewById(R.id.textView3);
        button.setOnClickListener(this::login);

        return view;
    }

    //login function
    public void login(View view) {
        email = inEmail.getText().toString().trim();
        password = inPassword.getText().toString().trim();
        //check if fields empty
        if (!email.equals("") && !password.equals("")) {   //String request, requires method (post or get), url, response listener and error listener
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                @Override //response function
                public void onResponse(String response) {   //if php echoes success then login successful move to next activity
                    if (response.equals("success")) {
                        Intent intent = new Intent(view.getContext(), success.class);
                        startActivity(intent);

                        //if php echoes failure then password is wrong
                    } else if (response.equals("failure")) {
                        Toast.makeText(view.getContext(), "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override //error listener function in case connection doesn't work
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(view.getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                //if all goes well and connection happens then we override the getParams function so it puts email and password in php variable
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email); //email will server as the id so the php file finds it using POST
                    data.put("password", password);
                    return data;
                }
            };
            //setup requestQueue then add the string request to it
            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(stringRequest);
        } else //if fields empty
        {
            Toast.makeText(view.getContext(), "Fields can not be empty", Toast.LENGTH_SHORT).show();
        }
    }


}



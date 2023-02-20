package com.example.main.LOGIN;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.main.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private EditText inFName,inLName,inName,inEmail,inPhone,inPassword,inPassword2;
    private TextView tvStatus;
    private Button button;

    private String Url = "http://"+ip+"//carpool/login/register.php";
    private String fName,lName,name,email,password,password2,phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.register, container, false);
        inFName = view.findViewById(R.id.FName);
        inLName = view.findViewById(R.id.LName);
        inName = view.findViewById(R.id.Name);
        inEmail = view.findViewById(R.id.inEmail);
        inPhone = view.findViewById(R.id.Phone);
        inPassword = view.findViewById(R.id.inPassword);
        inPassword2 = view.findViewById(R.id.inPassword2);
        tvStatus = view.findViewById(R.id.tvStatus);
        button = view.findViewById(R.id.button);
        lName =fName =phone=name = email = password = password2 = "";
        //setting on click listeners and the fetch functions inside the constructor
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName = inFName.getText().toString().trim();
                lName = inLName.getText().toString().trim();
                name = inName.getText().toString().trim();
                email = inEmail.getText().toString().trim();
                phone = inPhone.getText().toString().trim();
                password = inPassword.getText().toString().trim();
                password2 = inPassword2.getText().toString().trim();

                if (!password.equals(password2)){ //check if re-enter password is correct
                    Toast.makeText(view.getContext(),"Re-enter password correctly",Toast.LENGTH_SHORT).show();
                }
                else if (!name.equals("")&&!password.equals("")&&!email.equals("")){

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success"))
                            {
                                tvStatus.setText("success register");
                                Toast.makeText(view.getContext(),"You registered successfully!",Toast.LENGTH_SHORT).show();
                                button.setClickable(false); //so that the button stops working
                                new android.os.Handler().postDelayed( //go back to login activity after delay
                                        new Runnable() {
                                            public void run() {
                                                inName.setInputType(-1); inEmail.setInputType(-1); inPassword.setInputType(-1); inPassword2.setInputType(-1);
                                            }
                                        },
                                        300);
                            }
                            else if (response.equals("failure"))
                            {
                                Toast.makeText(view.getContext(),"Something went wrong....",Toast.LENGTH_SHORT).show();
                                tvStatus.setText("something went wrong");
                            }
                            else if (response.equals("User_exists"))
                            {
                                Toast.makeText(view.getContext(),"A user with this email already exists",Toast.LENGTH_SHORT).show();
                                tvStatus.setText("User already exists");
                            }
                            else if (response.equals("not_valid"))
                            {
                                Toast.makeText(view.getContext(),"Enter valid email address",Toast.LENGTH_SHORT).show();
                                tvStatus.setText("Email invalid");
                            }
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
                            Map<String,String> data = new HashMap<>();
                            data.put("name",name);
                            data.put("email",email);
                            data.put("password",password);
                            data.put("fName",fName);
                            data.put("lName",lName);
                            data.put("phone",phone);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

        return view;
    }
}
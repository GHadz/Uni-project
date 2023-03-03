package com.example.main.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import static com.example.main.LOGIN.Login_SignUp_Main.ip;
import static com.example.main.MainActivity.user_id;

import androidx.annotation.NonNull;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AccountFragment extends Fragment implements View.OnFocusChangeListener {
private View v;
private RatingBar driver,passenger;
private Button registerBtn;
private EditText inFName,inLName,inName,inEmail,inPhone,inPassword,txtCheck, passTxt,pass2Txt;
private boolean passChanged,check;
private String fName,lName,username,email,phone,pass;
private Integer driverRating,passengerRating;
private LinearLayout c;
private String Url = "http://"+ip+"//carpool/account/accDetails.php";
private String Url2 = "http://"+ip+"//carpool/account/passCheck.php";
private String Url3 = "http://"+ip+"//carpool/account/saveChanges.php";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_account,container,false);
        inFName = v.findViewById(R.id.FName);
        inLName = v.findViewById(R.id.LName);
        c = v.findViewById(R.id.container);
        inName = v.findViewById(R.id.Name);
        passChanged = false;
        inEmail = v.findViewById(R.id.inEmail);
        inPhone = v.findViewById(R.id.Phone);
        inPassword = v.findViewById(R.id.inPassword);
        registerBtn = v.findViewById(R.id.register);
        driver = v.findViewById(R.id.passengerRecycler);
        passenger = v.findViewById(R.id.passengerRatingBar);
        onOpen();
        inName.setOnFocusChangeListener(this);
        inFName.setOnFocusChangeListener(this);
        inLName.setOnFocusChangeListener(this);
        inName.setOnFocusChangeListener(this);
        inEmail.setOnFocusChangeListener(this);

        inPhone.setOnFocusChangeListener(this);
        inPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passChanged)showDet();
                else if (passChanged){
                    Toast.makeText(v.getContext(), "Password already changed", Toast.LENGTH_SHORT).show();
                    inPassword.setText(pass);
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveChanges();
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
//get the information from database
    public void onOpen(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                JSONArray array;
                try {
                    array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        fName = o.getString("fname");
                        lName = o.getString("lname");
                        username =o.getString("username");
                        email = o.getString("email");
                        phone = o.getString("phone");
                        pass = o.getString("pass");
                        driverRating = o.getInt("driverRating");
                        passengerRating = o.getInt("passengerRating");
                    }
                    driver.setRating(driverRating);
                    passenger.setRating(passengerRating);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),"error",Toast.LENGTH_SHORT).show();
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
                Map<String, String> data = new HashMap<>();
                data.put("id", user_id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(stringRequest);
    }

//focus change whenever we click on and off and edit text
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
        {
        switch (v.getId())
        {
            case R.id.FName:
                inFName.setText(fName);
                break;
            case R.id.LName:
                inLName.setText(lName);
                break;
            case R.id.Name:
                inName.setText(username);
                break;
            case R.id.Phone:
                inPhone.setText(phone);
                break;
            case R.id.inEmail:
                inEmail.setText(email);
                break;
            case R.id.inPassword:
                Toast.makeText(v.getContext(),"hah",Toast.LENGTH_SHORT).show();
                break;
            }
        }
        else{
            switch (v.getId())
            {
                case R.id.FName:
                    fName = inFName.getText().toString();
                    inFName.setText("");
                    break;
                case R.id.LName:
                   lName= inLName.getText().toString();
                    inLName.setText("");
                    break;
                case R.id.Name:
                    username =inName.getText().toString();
                    inName.setText("");
                    break;
                case R.id.Phone:
                   phone= inPhone.getText().toString();
                    inPhone.setText("");
                    break;
                case R.id.inEmail:
                    email = inEmail.getText().toString();
                    inEmail.setText("");
                    break;
                case R.id.inPassword:
                    pass = inPassword.getText().toString();
                    inPassword.setText("");
                    break;
            }
        }
    }
    //show password dialog
    public void showDet()
    {

         check = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Change password");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.password_dialog_layout, null);
        builder.setView(customLayout);
        txtCheck = customLayout.findViewById(R.id.checkPass);
        passTxt = customLayout.findViewById(R.id.txtPass);
        pass2Txt = customLayout.findViewById(R.id.txtPass2);
        Button submit = customLayout.findViewById(R.id.submitBtn);
        Button cancel = customLayout.findViewById(R.id.cancelBtn);
        Button checkBtn = customLayout.findViewById(R.id.checkBtn);
        AlertDialog dialog = builder.create();

        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check)
                {
                    dialog.dismiss();
                }
                else{
                    String p1 = passTxt.getText().toString();
                    String p2 = pass2Txt.getText().toString();
                    if (p1.equals(""))Toast.makeText(v.getContext(), "Enter new valid password", Toast.LENGTH_SHORT).show();
                    else if (!p1.equals(p2)){
                        Toast.makeText(v.getContext(), "Re-enter password correctly", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        pass = p1;
                        inPassword.setText(pass);
                        passChanged = true;
                        dialog.dismiss();
                        Toast.makeText(v.getContext(), "New Password registered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            checkPassword();
            }
        });
    }
//check the password before allowing to change it
    public void checkPassword() {
       String c = txtCheck.getText().toString();

        if (!c.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("failure")) {
                        Toast.makeText(v.getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();

                    } else  {
                        check = true;
                        passTxt.setVisibility(View.VISIBLE);
                        pass2Txt.setVisibility(View.VISIBLE);
                        Toast.makeText(v.getContext(), "You can now change your password.", Toast.LENGTH_SHORT).show();
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
                    String s = txtCheck.getText().toString().trim();
                    Map<String, String> data = new HashMap<>();
                    data.put("id", user_id);
                    data.put("password",s);
                    return data;
                }
            };
            //setup requestQueue then add the string request to it
            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            requestQueue.add(stringRequest);
        } else //if fields empty
        {
            Toast.makeText(v.getContext(), "Please Enter your password", Toast.LENGTH_SHORT).show();
        }
    }
    //save the changes made
    public void saveChanges()
{
    c.requestFocus();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url3, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            if (response.equals("success"))
            {
                Toast.makeText(v.getContext(),"Changes successfully made!",Toast.LENGTH_SHORT).show();
                registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"Changes already made, leave then try again.",Toast.LENGTH_SHORT).show();
                    }
                });
                inPassword.setClickable(false);
                inPassword.setFocusable(false);


            }
            else if (response.equals("failure"))
            {
                Toast.makeText(v.getContext(),"Nothing was changed or some error happened",Toast.LENGTH_SHORT).show();

            }
            else if (response.equals("User_exists"))
            {
                Toast.makeText(v.getContext(),"A user with this email already exists",Toast.LENGTH_SHORT).show();
            }
            else if (response.equals("not_valid"))
            {
                Toast.makeText(v.getContext(),"Enter valid email address",Toast.LENGTH_SHORT).show();
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
            data.put("id",user_id);
            data.put("name",username);
            data.put("email",email);
            if(passChanged)data.put("password",pass);
            data.put("fName",fName);
            data.put("lName",lName);
            data.put("phone",phone);
            return data;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
    requestQueue.add(stringRequest);
}


}
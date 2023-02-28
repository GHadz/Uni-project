package com.example.main.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.main.LOGIN.Login_SignUp_Main.ip;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.main.R;


public class AccountFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_account,container,false);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
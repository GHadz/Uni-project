package com.example.main.LOGIN;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
//adapter for the fragments
public class ViewPagerAdapter extends FragmentStateAdapter {
private String ip;
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//sets which fragment to show according to th eposition
        if (position==1){
        RegisterFragment register = new RegisterFragment();

        return register;
        }
        else{
            LoginFragment login = new LoginFragment();

            return login;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

package com.example.foodapp.Adapter_Customer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.Fagment_Staff.Drinks_Staff_Fragment;
import com.example.foodapp.Fagment_Staff.Food_Staff_Fragment;


public class ViewPagerMenuStaffAdapter extends FragmentStateAdapter {




    public ViewPagerMenuStaffAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Food_Staff_Fragment();
            case 1:
                return new Drinks_Staff_Fragment();
            default:
                return new Drinks_Staff_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

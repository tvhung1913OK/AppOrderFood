package com.example.foodapp.Adapter_Customer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.Fragment_Customer.DrinkFragment;
import com.example.foodapp.Fragment_Customer.FoodsFragment;

public class ViewPagerMenuAdapter extends FragmentStateAdapter {


    public ViewPagerMenuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FoodsFragment();
            case 1:
                return new DrinkFragment();
            default:
                return new FoodsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

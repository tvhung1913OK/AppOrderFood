package com.example.foodapp.Fagment_Staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.Adapter_Customer.ViewPagerMenuAdapter;
import com.example.foodapp.Adapter_Customer.ViewPagerMenuStaffAdapter;
import com.example.foodapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Menu_Fragment_Staff extends Fragment {

    BottomNavigationView bottomNavigationView;



    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    ViewPagerMenuStaffAdapter adapter;

    ImageView img_rs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment_staff, container, false);

        viewPager2 = view.findViewById(R.id.paperMenustaff);
        tabLayout = view.findViewById(R.id.tabMenustaff);

        adapter = new ViewPagerMenuStaffAdapter(requireActivity());
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Foods");
                        break;
                    case 1:
                        tab.setText("Drink");
                        break;
                }
            }
        }).attach();

        return view;
    }
}

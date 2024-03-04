package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;


import com.example.foodapp.Fagment_Staff.Menu_Fragment_Staff;
import com.example.foodapp.Fagment_Staff.MoreFragment_Staff;
import com.example.foodapp.Fagment_Staff.Home_Fragment;
import com.example.foodapp.Fagment_Staff.StaffCategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout, new Home_Fragment()).commit();

        bottomNavigationView.getMenu().findItem(R.id.history).setVisible(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    fm.beginTransaction().replace(R.id.frameLayout, new Home_Fragment()).commit();
                    break;
                case R.id.category:
                    fm.beginTransaction().replace(R.id.frameLayout, new StaffCategoryFragment()).commit();
                    break;
                case R.id.menu:
                    fm.beginTransaction().replace(R.id.frameLayout, new Menu_Fragment_Staff()).commit();
                    break;
                case R.id.more:
                    fm.beginTransaction().replace(R.id.frameLayout, new MoreFragment_Staff()).commit();
                    break;
            }
            return true;
        });
    }
}
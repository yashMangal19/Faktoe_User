package com.example.faktoe_user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.faktoe_user.Cart.FragmentUserCart;
import com.example.faktoe_user.OrderManagement.FragmentOrderManagement;
import com.example.faktoe_user.R;
import com.example.faktoe_user.UserHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class UserDashboard extends AppCompatActivity {
    ChipNavigationBar bnv;
    private String UserState, UserCity, UserId, UserPhoneNumber;
    SearchView searchView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        bnv = (ChipNavigationBar) findViewById(R.id.dashboardBottomNavigation);
        searchView = findViewById(R.id.searchView);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(UserDashboard.this);
        UserState = mPreferences.getString(getString(R.string.UserState),"").toUpperCase();
        UserCity = mPreferences.getString(getString(R.string.UserDistrict),"");
        UserPhoneNumber = mPreferences.getString(getString(R.string.UserPhoneNumber),"");
        UserId = mPreferences.getString(getString(R.string.UserId),"");

        bnv.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){
            @Override
            public void onItemSelected(int i) {
                if(i == R.id.bottomNavCart){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.userDashboardFragContainer,new FragmentUserCart());
                    fragmentTransaction.commit();
                }
                else if(i == R.id.bottomNavHome){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.userDashboardFragContainer,new UserHomeFragment());
                    fragmentTransaction.commit();
                }
                else if(i == R.id.bottomNavOrders){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.userDashboardFragContainer,new FragmentOrderManagement());
                    fragmentTransaction.commit();
                }
            }
        });

        //Setting home fragment as default in fragment container
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.userDashboardFragContainer, new UserHomeFragment()).commit();

        }
    }
}
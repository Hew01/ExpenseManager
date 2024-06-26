package com.example.emanager.views.activites;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.emanager.adapters.TransactionsAdapter;
import com.example.emanager.models.Transaction;
import com.example.emanager.utils.Constants;
import com.example.emanager.utils.Helper;
import com.example.emanager.viewmodels.MainViewModel;
import com.example.emanager.views.fragments.AddTransactionFragment;
import com.example.emanager.R;
import com.example.emanager.databinding.ActivityMainBinding;
import com.example.emanager.views.fragments.AccountsFragment;
import com.example.emanager.views.fragments.MoreFragment;
import com.example.emanager.views.fragments.SettingFragment;
import com.example.emanager.views.fragments.StatsFragment;
import com.example.emanager.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar;
    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
     */


    public static MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Log.v("aloalo","aloalolao");

        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();

        Log.v("Login","before");
        //viewModel.clearUserCredentials();
        // Kiểm tra UserId trong SharedPreferences
        if (getUserIdFromPreferences().isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            Constants.UserId = getUserIdFromPreferences();
            String email=getEmailFromPreferences();
            String password=getPasswordFromPreferences();
            viewModel.setupSync(email,password);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionsFragment());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactions) {
                    getSupportFragmentManager().popBackStack();
                } else if(item.getItemId() == R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                else if(item.getItemId()==R.id.accounts)
                {
                    transaction.replace(R.id.content, new AccountsFragment());
                    transaction.addToBackStack(null);
                }
                else if(item.getItemId()==R.id.more)
                {
                    transaction.replace(R.id.content, new SettingFragment());
                    transaction.addToBackStack(null);
                }

                transaction.commit();
                return true;
            }
        });




    }

    private String getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("UserId", "");
    }
    private String getEmailFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("Email", "");
    }
    private String getPasswordFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getString("Password", "");
    }


    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
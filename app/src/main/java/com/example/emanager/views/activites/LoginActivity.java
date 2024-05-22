package com.example.emanager.views.activites;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.emanager.R;
import com.example.emanager.viewmodels.MainViewModel;
import com.example.emanager.views.fragments.RegisterFragment;
import com.example.emanager.views.fragments.TransactionsFragment;


public class LoginActivity extends AppCompatActivity {

    public MainViewModel viewModel;

    public LoginActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = MainActivity.viewModel;

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userEmail = findViewById(R.id.userEmail);
                EditText password = findViewById(R.id.password);

                String email = userEmail.getText().toString();
                String pass = password.getText().toString();

                viewModel.setupSync(email, pass);


                finish();

            }
        });

        findViewById(R.id.signupText).setOnClickListener(c -> {
            RegisterFragment registerFragment = new RegisterFragment();
            registerFragment.show(getSupportFragmentManager(), null);
        });

    }

}
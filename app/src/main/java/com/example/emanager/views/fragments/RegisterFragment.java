package com.example.emanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emanager.R;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

import com.example.emanager.utils.Constants;
import com.example.emanager.viewmodels.MainViewModel;

public class RegisterFragment extends DialogFragment {

    public MainViewModel viewModel;
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        view.findViewById(R.id.signupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = view.findViewById(R.id.rgUserEmail);
                EditText password = view.findViewById(R.id.rgPassword);

                registerUser(email.getText().toString(),password.getText().toString());

            }
        });
        return view;
    }
    private void registerUser(String email, String password) {
        App app = new App(new AppConfiguration.Builder(Constants.AppId).build());
        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                Toast.makeText(getContext(), "Registration successful! Logging in...", Toast.LENGTH_SHORT).show();
                viewModel.setupSync(email, password);
            } else {
                Toast.makeText(getContext(), "Registration failed: " + it.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
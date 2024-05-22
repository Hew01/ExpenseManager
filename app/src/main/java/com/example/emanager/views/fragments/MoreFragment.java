package com.example.emanager.views.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emanager.databinding.FragmentMoreBinding;
import com.example.emanager.viewmodels.MainViewModel;
import com.example.emanager.views.activites.LoginActivity;
import com.example.emanager.views.activites.MainActivity;


public class MoreFragment extends Fragment {


    FragmentMoreBinding binding;
    public MainViewModel viewModel;
    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.signoutBtn.setOnClickListener(c->{
            showSignOutDialog();
        });
        binding.UserEmail.setText(requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("Email", ""));

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void showSignOutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.clearUserCredentials();
                        viewModel.clearRealmConfiguration();
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
package com.example.emanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emanager.R;
import com.example.emanager.databinding.FragmentAccountsBinding;
import com.example.emanager.databinding.FragmentAddAccountBinding;
import com.example.emanager.models.Account;
import com.example.emanager.viewmodels.MainViewModel;
import com.example.emanager.views.activites.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class AddAccountFragment extends BottomSheetDialogFragment {


    public AddAccountFragment() {
        // Required empty public constructor
    }

    FragmentAddAccountBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAddAccountBinding.inflate(inflater);

        binding.createAccountBtn.setOnClickListener(c->{
            String name=binding.accountNameAdd.getText().toString();
            Double amount=Double.parseDouble(binding.accountAmountAdd.getText().toString());
            new ViewModelProvider(requireActivity()).get(MainViewModel.class).addAccount(new Account(amount,name));

            dismiss();
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}
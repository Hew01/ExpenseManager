package com.example.emanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emanager.R;
import com.example.emanager.adapters.AccountsOtherAdapter;
import com.example.emanager.databinding.FragmentAccountsBinding;
import com.example.emanager.databinding.FragmentTransactionsBinding;
import com.example.emanager.models.Account;
import com.example.emanager.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;


public class AccountsFragment extends Fragment  {

    FragmentAccountsBinding binding;
    public MainViewModel viewModel;
    AccountsOtherAdapter adapter;
    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountsBinding.inflate(inflater);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        double networth = viewModel.getNetWorth();
        binding.networthTV.setText(String.valueOf(networth));
        if(networth<0)
        {
            binding.debtTV.setText(String.valueOf(networth*-1));
        }
        else {
            binding.moneyTV.setText(String.valueOf(networth));
        }
        viewModel.networth.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double networth) {
                binding.networthTV.setText(String.valueOf(networth));
                if(networth<0)
                {
                    binding.debtTV.setText(String.valueOf(networth*-1));
                }
                else {
                    binding.moneyTV.setText(String.valueOf(networth));
                }
            }
        });
        adapter = new AccountsOtherAdapter(getContext());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.rcvAC.setLayoutManager(linearLayoutManager);
        adapter.setDataAC(viewModel.getListAccount());
        binding.rcvAC.setAdapter(adapter);
        viewModel.accounts.observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                adapter.setDataAC(accounts);
            }
        });

        binding.addAccountBtn.setOnClickListener(c->{
            new AddAccountFragment().show(getParentFragmentManager(), null);
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}
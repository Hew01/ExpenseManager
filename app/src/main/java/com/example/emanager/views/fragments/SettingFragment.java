package com.example.emanager.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.emanager.R;
import com.example.emanager.databinding.FragmentSettingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSettingBinding binding;
    private Switch darkModeSwitch;

    private LinearLayout exchangeRateItem;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentSettingBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        exchangeRateItem = root.findViewById(R.id.exchangeRateItem);

        darkModeSwitch = root.findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Xử lý khi chuyển đổi chế độ tối
            if(isChecked){
                //Toast.makeText(requireContext(), "Chế độ nền tối: Bật", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                //Toast.makeText(requireContext(), "Chế độ nền tối: Tắt", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        final LinearLayout btnEditProfileItem = binding.editProfileItem;
        final LinearLayout btnChangePasswordItem = binding.changePasswordItem;

        btnEditProfileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of SecondFragment
                ProfileFragment profileFragment = new ProfileFragment();

                // Get the FragmentManager and start a transaction.
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.content, profileFragment) // replace the container with the new fragment
                        .addToBackStack(null) // add this transaction to the back stack
                        .commit(); // commit the transaction
            }
        });

        btnChangePasswordItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of SecondFragment
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

                // Get the FragmentManager and start a transaction.
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.content, changePasswordFragment) // replace the container with the new fragment
                        .addToBackStack(null) // add this transaction to the back stack
                        .commit(); // commit the transaction
            }
        });

        exchangeRateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // URL cần mở
                String url = "https://www.sacombank.com.vn/cong-cu/ty-gia.html";
                // Tạo Intent với action ACTION_VIEW
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Thiết lập URL cho Intent
                intent.setData(Uri.parse(url));
                // Bắt đầu activity mới với intent
                startActivity(intent);
            }
        });

        return root;

        //return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}
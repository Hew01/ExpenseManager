package com.example.emanager.views.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.emanager.R;
import com.example.emanager.databinding.FragmentProfileBinding;
import com.example.emanager.databinding.FragmentSettingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentProfileBinding binding;
    private ImageView btnBack;
    private EditText nameEditText;
    private EditText emailEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private EditText birthdayEditText;
    private ImageView avatarImageView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        btnBack = root.findViewById(R.id.backButton);
        avatarImageView = root.findViewById(R.id.avatarImageView);
        nameEditText = root.findViewById(R.id.nameEditText);
        emailEditText = root.findViewById(R.id.emailEditText);
        maleRadioButton = root.findViewById(R.id.maleRadioButton);
        femaleRadioButton = root.findViewById(R.id.femaleRadioButton);
        birthdayEditText = root.findViewById(R.id.birthdayEditText);

        Button saveButton = root.findViewById(R.id.saveButton);
        ImageView changeAvatarButton = root.findViewById(R.id.changeAvatarButton);

        nameEditText.setText("Vinh");
        emailEditText.setText("vinh@gmmail.com");
        birthdayEditText.setText("1/1/2002");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEditMode = nameEditText.isEnabled();

                if (isEditMode) {
                    // Tắt khả năng chỉnh sửa và thay đổi nút
                    nameEditText.setEnabled(false);
                    emailEditText.setEnabled(false);
                    maleRadioButton.setEnabled(false);
                    femaleRadioButton.setEnabled(false);
                    birthdayEditText.setEnabled(false);

                    nameEditText.setTextColor(Color.parseColor("#AAAAAA")); // Màu xám nhạt
                    emailEditText.setTextColor(Color.parseColor("#AAAAAA"));
                    maleRadioButton.setTextColor(Color.parseColor("#AAAAAA"));
                    femaleRadioButton.setTextColor(Color.parseColor("#AAAAAA"));
                    birthdayEditText.setTextColor(Color.parseColor("#AAAAAA"));

                    saveButton.setText("Chỉnh sửa");
                } else {
                    // Bật khả năng chỉnh sửa và thay đổi nút
                    nameEditText.setEnabled(true);
                    emailEditText.setEnabled(true);
                    maleRadioButton.setEnabled(true);
                    femaleRadioButton.setEnabled(true);
                    birthdayEditText.setEnabled(true);

                    nameEditText.setTextColor(Color.parseColor("#000000")); // Màu đen
                    emailEditText.setTextColor(Color.parseColor("#000000"));
                    maleRadioButton.setTextColor(Color.parseColor("#000000"));
                    femaleRadioButton.setTextColor(Color.parseColor("#000000"));
                    birthdayEditText.setTextColor(Color.parseColor("#000000"));

                    saveButton.setText("Lưu");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc fragment hiện tại
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return root;
        //return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
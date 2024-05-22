package com.example.emanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emanager.R;
import com.example.emanager.databinding.FragmentChangePasswordBinding;
import com.example.emanager.databinding.FragmentSettingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentChangePasswordBinding binding;
    private ImageView btnBack;
    private EditText curentPasswordEditText, passwordEditText, confirmPasswordEditText;
    private ImageView curentPasswordVisibilityToggle, passwordVisibilityToggle, confirmPasswordVisibilityToggle;
    private boolean isCurentPasswordVisible = false;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        btnBack = root.findViewById(R.id.backButton);
        curentPasswordEditText = root.findViewById(R.id.curentPasswordEditText);
        passwordEditText = root.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = root.findViewById(R.id.confirmPasswordEditText);
        curentPasswordVisibilityToggle = root.findViewById(R.id.curentPasswordVisibilityToggle);
        passwordVisibilityToggle = root.findViewById(R.id.passwordVisibilityToggle);
        confirmPasswordVisibilityToggle = root.findViewById(R.id.confirmPasswordVisibilityToggle);
        Button submitButton = root.findViewById(R.id.submitButton);

        curentPasswordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCurentPasswordVisibility();
            }
        });

        passwordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        confirmPasswordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConfirmPasswordVisibility();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPassword();
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
        //return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    private void toggleCurentPasswordVisibility() {
        if (isCurentPasswordVisible) {
            curentPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            curentPasswordVisibilityToggle.setImageResource(R.drawable.ic_visibility_off);
        } else {
            curentPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            curentPasswordVisibilityToggle.setImageResource(R.drawable.ic_visibility);
        }
        isCurentPasswordVisible = !isCurentPasswordVisible;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility_off);
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordVisibilityToggle.setImageResource(R.drawable.ic_visibility);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            confirmPasswordVisibilityToggle.setImageResource(R.drawable.ic_visibility_off);
        } else {
            confirmPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            confirmPasswordVisibilityToggle.setImageResource(R.drawable.ic_visibility);
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
    }

    private void submitPassword() {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
        } else {
            // Xử lý logic gửi mật khẩu ở đây

            Toast.makeText(getActivity(), "Mật khẩu đã được cập nhật", Toast.LENGTH_SHORT).show();
        }
    }
}
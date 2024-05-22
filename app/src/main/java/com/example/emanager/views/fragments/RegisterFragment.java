package com.example.emanager.views.fragments;

import android.app.DatePickerDialog;
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

import com.example.emanager.databinding.FragmentRegisterBinding;
import com.example.emanager.models.UserE;
import com.example.emanager.utils.Constants;
import com.example.emanager.utils.Helper;
import com.example.emanager.viewmodels.MainViewModel;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

public class RegisterFragment extends DialogFragment {

    public MainViewModel viewModel;
    public RegisterFragment() {
        // Required empty public constructor
    }

    FragmentRegisterBinding binding;
    private Date selectedDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = binding.etEmail;
                EditText password = binding.etPassword;
                if(binding.etPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString()))
                {
                    registerUser(email.getText().toString(),password.getText().toString());
                }
                else{
                    Toast.makeText(getContext(), "Comfirm password not correct!!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.YEAR, datePicker.getYear());

                    //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                    String dateToShow = Helper.formatDate(calendar.getTime());

                    binding.date.setText(dateToShow);
                    selectedDate = calendar.getTime();

                });
                datePickerDialog.show();
            }
        });
        return binding.getRoot();
    }
    private void registerUser(String email, String password) {
        App app = new App(new AppConfiguration.Builder(Constants.AppId).build());
        app.getEmailPassword().registerUserAsync(email, password, it -> {
            if (it.isSuccess()) {
                UserE user= new UserE();
                user.set_id(new ObjectId());
                user.setName(binding.etName.getText().toString());
                user.setEmail(binding.etEmail.getText().toString());
                user.setPassword(binding.etPassword.getText().toString());
                user.setBirthDay(selectedDate);
                user.setOwner_id(app.currentUser().getId());
                if(binding.maleRadioButton.isChecked())
                {
                    user.setGender("Male");
                }
                else if(binding.femaleRadioButton.isChecked())
                {
                    user.setGender("Female");
                }

                Toast.makeText(getContext(), "Registration successful! Logging in...", Toast.LENGTH_SHORT).show();
                viewModel.setupSyncWithUser(email, password,user);

                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Registration failed: " + it.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
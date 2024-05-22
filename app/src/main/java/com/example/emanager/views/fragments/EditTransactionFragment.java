package com.example.emanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emanager.R;
import com.example.emanager.adapters.AccountsAdapter;
import com.example.emanager.adapters.CategoryAdapter;
import com.example.emanager.databinding.FragmentAddTransactionBinding;
import com.example.emanager.databinding.ListDialogBinding;
import com.example.emanager.models.Account;
import com.example.emanager.models.Category;
import com.example.emanager.models.Transaction;
import com.example.emanager.utils.Constants;
import com.example.emanager.utils.Helper;
import com.example.emanager.views.activites.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;

public class EditTransactionFragment extends BottomSheetDialogFragment {
    Transaction transaction;
    FragmentAddTransactionBinding binding;
    Realm realm;
    public EditTransactionFragment(Transaction transaction) {
        this.transaction = transaction;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getContext());  // Initialize Realm
        realm = Realm.getDefaultInstance();  // Get a Realm instance
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        binding.amount.setText(String.valueOf(transaction.getAmount()));
        binding.note.setText(transaction.getNote());
        binding.date.setText(Helper.formatDate(transaction.getDate()));
        binding.category.setText(transaction.getCategory());
        binding.account.setText(transaction.getAccount());

        if (transaction.getType().equals(Constants.INCOME)) {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));
        } else if (transaction.getType().equals(Constants.EXPENSE)) {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
        }

        binding.incomeBtn.setOnClickListener(view -> {
            final ObjectId transactionId = transaction.getId();  // Get the id on the UI thread

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Fetch the Transaction object within the transaction
                    Transaction transactionToUpdate = realm.where(Transaction.class)
                            .equalTo("_id", transactionId)  // Use the id we got on the UI thread
                            .findFirst();

                    if (transactionToUpdate != null) {
                        transactionToUpdate.setType(Constants.INCOME);
                    }
                }
            });
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));
        });

        binding.expenseBtn.setOnClickListener(view -> {
            final ObjectId transactionId = transaction.getId();  // Get the id on the UI thread

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Fetch the Transaction object within the transaction
                    Transaction transactionToUpdate = realm.where(Transaction.class)
                            .equalTo("_id", transactionId)  // Use the id we got on the UI thread
                            .findFirst();

                    if (transactionToUpdate != null) {
                        transactionToUpdate.setType(Constants.EXPENSE);
                    }
                }
            });
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
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

                    transaction.setDate(calendar.getTime());
                    //transaction.setId(calendar.getTime().getTime());
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(c-> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());
            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    final String categoryName = category.getCategoryName();  // Get the category name on the UI thread
                    final ObjectId transactionId = transaction.getId();  // Get the id on the UI thread

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            // Fetch the Transaction object within the transaction
                            Transaction transactionToUpdate = realm.where(Transaction.class)
                                    .equalTo("_id", transactionId)  // Use the id we got on the UI thread
                                    .findFirst();

                            if (transactionToUpdate != null) {
                                transactionToUpdate.setCategory(categoryName);  // Use the category name we got on the UI thread
                            }
                        }
                    });
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();
        });

        binding.account.setOnClickListener(c-> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountsDialog = new AlertDialog.Builder(getContext()).create();
            accountsDialog.setView(dialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0, "Cash"));
            accounts.add(new Account(0, "Bank"));
            accounts.add(new Account(0, "Momo"));
            accounts.add(new Account(0, "Other"));

            AccountsAdapter adapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    final String accountName = account.getAccountName();  // Get the account name on the UI thread
                    final ObjectId transactionId = transaction.getId();  // Get the id on the UI thread

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            // Fetch the Transaction object within the transaction
                            Transaction transactionToUpdate = realm.where(Transaction.class)
                                    .equalTo("_id", transactionId)  // Use the id we got on the UI thread
                                    .findFirst();

                            if (transactionToUpdate != null) {
                                transactionToUpdate.setAccount(accountName);  // Use the account name we got on the UI thread
                            }
                        }
                    });
                    accountsDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(adapter);

            accountsDialog.show();

        });

        binding.saveTransactionBtn.setOnClickListener(c-> {
            final ObjectId transactionId = transaction.getId();  // Get the id on the UI thread
            final String transactionType = transaction.getType();  // Get the transaction type on the UI thread
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Fetch the Transaction object within the transaction
                    Transaction transactionToUpdate = realm.where(Transaction.class)
                            .equalTo("_id", transactionId)  // Use the id we got on the UI thread
                            .findFirst();

                    if (transactionToUpdate != null) {
                        double amount = Double.parseDouble(binding.amount.getText().toString());
                        if (transactionType.equals(Constants.EXPENSE)) {
                            transactionToUpdate.setAmount(amount * -1);
                        } else {
                            transactionToUpdate.setAmount(amount);
                        }

                        String note = binding.note.getText().toString();
                        transactionToUpdate.setNote(note);
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Update the UI here...
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.getTransactions();
                    }

                    dismiss();
                }
            });

        });

        return binding.getRoot();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();  // Close the Realm instance when you're done with it
    }
}
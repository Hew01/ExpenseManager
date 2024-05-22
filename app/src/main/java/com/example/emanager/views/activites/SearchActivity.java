package com.example.emanager.views.activites;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emanager.R;
import com.example.emanager.adapters.TransactionsAdapter;
import com.example.emanager.models.Transaction;
import com.example.emanager.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rcvTransactions;
    private TransactionsAdapter transactionsAdapter;
    private SearchView searchView;
    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rcvTransactions = findViewById(R.id.rcv_transactions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTransactions.setLayoutManager(linearLayoutManager);

        transactionsAdapter = new TransactionsAdapter(this, mainViewModel.getTransactions().getValue());
        rcvTransactions.setAdapter(transactionsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvTransactions.addItemDecoration(itemDecoration);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getTransactions().observe(this, transactions -> {
            // Update the adapter with new data
            transactionsAdapter.setTransactions(transactions);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.filterTransactions(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainViewModel.filterTransactions(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
package com.febrizummiati.githubuserfinal.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.adapter.AdapterUser;
import com.febrizummiati.githubuserfinal.viewmodel.UserVM;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvUsers;
    private UserVM userVM;
    private AdapterUser adapter;
    private EditText editSearch;
    private ImageButton btnSearch;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.titleSearch);

        userVM = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserVM.class);

        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        rvUsers = findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        btnSearch.setOnClickListener(this);
        showLoading(false);

        userVM.getListUsers().observe(this, list -> {
            adapter = new AdapterUser();
            adapter.setLists(list);
            rvUsers.setAdapter(adapter);
            showLoading(false);

            adapter.setOnItemClickCallback(data -> {
                showLoading(true);
                Intent mIntent = new Intent(MainActivity.this, DetailActivity.class);
                mIntent.putExtra(DetailActivity.EXTRA_USER, data);
                startActivity(mIntent);
            });
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            showLoading(true);
            if (editSearch.getText().toString().isEmpty()) {
                editSearch.setError("Username Required");
                editSearch.setFocusable(true);
                showLoading(false);
            } else {
                String username = editSearch.getText().toString();
                userVM.setListUsers(username, getApplicationContext());
            }
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent mIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(mIntent);
        }
        if (item.getItemId() == R.id.favorite) {
            Intent favoriteIntent = new Intent(MainActivity.this, FavActivity.class);
            startActivity(favoriteIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
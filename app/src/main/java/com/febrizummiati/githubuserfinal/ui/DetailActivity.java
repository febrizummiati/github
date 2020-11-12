package com.febrizummiati.githubuserfinal.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.adapter.AdapterFragment;
import com.febrizummiati.githubuserfinal.model.User;
import com.febrizummiati.githubuserfinal.viewmodel.UserVM;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.febrizummiati.githubuserfinal.database.DatabaseContract.UserColumns.AVATAR_URL;
import static com.febrizummiati.githubuserfinal.database.DatabaseContract.UserColumns.CONTENT_URI;
import static com.febrizummiati.githubuserfinal.database.DatabaseContract.UserColumns.LOGIN;
import static com.febrizummiati.githubuserfinal.database.DatabaseContract.UserColumns.NAME;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imgAvatar;
    private TextView tvName, tvRepository, tvLocation, tvUsername, tvCompany, tvFollowers;
    private Button btnAddFavorite, btnDelFavorite;

    private final ContentValues values = new ContentValues();

    public String login = "";
    public Integer id;

    public static final String EXTRA_USER = "extra_user";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle(R.string.titleDetail);

        tvName = findViewById(R.id.name);
        tvUsername = findViewById(R.id.username);
        tvFollowers = findViewById(R.id.follower);
        tvRepository = findViewById(R.id.repository);
        tvLocation = findViewById(R.id.location);
        tvCompany = findViewById(R.id.company);
        imgAvatar = findViewById(R.id.avatar);
        btnAddFavorite = findViewById(R.id.btnAddFavorite);
        btnDelFavorite = findViewById(R.id.btnDelFavorite);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        if (user != null) {
            login = user.getLogin();
            Glide.with(getApplicationContext())
                    .load(user.getAvatarUrl())
                    .apply(new RequestOptions().override(150, 150))
                    .into(imgAvatar);

            UserVM userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserVM.class);

            userViewModel.setUserDataApi(user.getLogin());
            userViewModel.getUserDataApi().observe(this, user1 -> {
                String followers = getString(R.string.followers);
                String following = getString(R.string.following);
                String repository = getString(R.string.repository);

                tvUsername.setText(user1.getLogin());
                tvName.setText(user1.getName());
                tvFollowers.setText(user1.getFollower() + " " + followers + " " + " " + " " + user1.getFollowing() + " " + following);
                tvLocation.setText(user1.getLocation());
                tvCompany.setText(user1.getCompany());
                tvRepository.setText(user1.getRepository() + " " + repository);
                values.put(LOGIN, user1.getLogin());
                values.put(NAME, user1.getName());
                values.put(AVATAR_URL, user1.getAvatarUrl());
            });

            userViewModel.setUserLocal(getContentResolver());
            userViewModel.getUserLocal().observe(this, userLocals -> {
                boolean check = false;
                for (int i = 0; i < userLocals.size(); i++) {
                    if (userLocals.get(i).getLogin().equals(login)) {
                        check = true;
                        id = userLocals.get(i).getId();
                    }
                }
                if (check) {
                    btnDelFavorite.setVisibility(View.VISIBLE);
                    btnAddFavorite.setVisibility(View.GONE);
                } else {
                    btnDelFavorite.setVisibility(View.GONE);
                    btnAddFavorite.setVisibility(View.VISIBLE);
                }
            });

        }

        AdapterFragment fragmentAdapter = new AdapterFragment(this, getSupportFragmentManager(), user);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(fragmentAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        btnDelFavorite.setOnClickListener(this);
        btnAddFavorite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddFavorite:
                getContentResolver().insert(CONTENT_URI, values);
                Toast.makeText(this, getResources().getString(R.string.success_add), Toast.LENGTH_SHORT).show();
                btnDelFavorite.setVisibility(View.VISIBLE);
                btnAddFavorite.setVisibility(View.GONE);
                break;
            case R.id.btnDelFavorite:
                Uri uriWithId = Uri.parse(CONTENT_URI + "/" + id);
                getContentResolver().delete(uriWithId, null, null);
                btnDelFavorite.setVisibility(View.GONE);
                btnAddFavorite.setVisibility(View.VISIBLE);
                Toast.makeText(this, getResources().getString(R.string.removeFavorite), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intentSetting = new Intent(DetailActivity.this, SettingActivity.class);
            startActivity(intentSetting);
        }

        if (item.getItemId() == R.id.favorite) {
            Intent favoriteIntent = new Intent(DetailActivity.this, FavActivity.class);
            startActivity(favoriteIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.febrizummiati.githubuserfinal.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.adapter.FragmentAdapter;
import com.febrizummiati.githubuserfinal.api.ApiClient;
import com.febrizummiati.githubuserfinal.api.ApiInterface;
import com.febrizummiati.githubuserfinal.model.User;
import com.febrizummiati.githubuserfinal.viewmodel.UserVM;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

import static com.febrizummiati.githubuserfinal.db.DatabaseContract.UserColumns.AVATAR_URL;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.UserColumns.CONTENT_URI;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.UserColumns.LOGIN;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.UserColumns.NAME;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView imgAvatar;
    private TextView tvName, tvRepository, tvLocation, tvUsername, tvCompany, tvFollowers;
    private Button btnFavorite;

    ContentValues values = new ContentValues();

    public String login = "";
    public Integer id;

    private final static String addtofavorite = "Add To Favorite";
    private final static String removefromfavorite = "Remove From Favorite";

    public static final String EXTRA_USER = "extra_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String titleDetail = getString(R.string.titleDetail);
        Objects.requireNonNull(getSupportActionBar()).setTitle(titleDetail);

        tvName = findViewById(R.id.name);
        tvUsername = findViewById(R.id.username);
        tvFollowers = findViewById(R.id.follower);
        tvRepository = findViewById(R.id.repository);
        tvLocation = findViewById(R.id.location);
        tvCompany = findViewById(R.id.company);
        imgAvatar = findViewById(R.id.avatar);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(this);

        String followers = getString(R.string.followers);
        String following = getString(R.string.following);
        String repository = getString(R.string.repository);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        getDetail(user.getLogin());
        Glide.with(getApplicationContext())
                .load(user.getUrlAvatar())
                .apply(new RequestOptions().override(150, 150))
                .into(imgAvatar);

        UserVM userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserVM.class);

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
                btnFavorite.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                btnFavorite.setText(removefromfavorite);
                Toast.makeText(DetailActivity.this, btnFavorite.getText(), Toast.LENGTH_SHORT).show();
            } else {
                btnFavorite.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                btnFavorite.setText(addtofavorite);
            }
        });

        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, getSupportFragmentManager(), user);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(fragmentAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        btnFavorite.setOnClickListener(this);
    }

    private void getDetail(String username) {
        ApiInterface Service;
        retrofit2.Call<User> Call;
        try {
            Service = ApiClient.getApi().create(ApiInterface.class);
            Call = Service.getDetailUser(username);
            Call.enqueue(new Callback<User>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                    String followers = getString(R.string.followers);
                    String following = getString(R.string.following);
                    String repository = getString(R.string.repository);
                    User mUser = response.body();
                    tvName.setText(mUser.getName());
                    tvUsername.setText(mUser.getLogin());
                    tvFollowers.setText(mUser.getFollower() + " " + followers + " " + " " + " " + mUser.getFollowing() + " " + following);
                    tvLocation.setText(mUser.getLocation());
                    tvCompany.setText(mUser.getCompany());
                    tvRepository.setText(mUser.getRepository() + " " + repository);
                }

                @Override
                public void onFailure(retrofit2.Call<User> call, Throwable t) {
                    Log.d("Message", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnFavorite) {
            if (btnFavorite.getText() == addtofavorite){

                getContentResolver().insert(CONTENT_URI, values);
                addFavorite();
                Toast.makeText(this, getResources().getString(R.string.success_add_to_favorite), Toast.LENGTH_SHORT).show();

            } else if (btnFavorite.getText() == removefromfavorite) {

                Uri uriWithId = Uri.parse(CONTENT_URI + "/" + id);
                getContentResolver().delete(uriWithId, null, null );
                removeFavorite();
                Toast.makeText(this, getResources().getString(R.string.success_remove_from_favorite), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addFavorite(){
        btnFavorite.setBackgroundColor(getResources().getColor(R.color.colorSubTitle));
        btnFavorite.setText(getResources().getString(R.string.removefromfavorite));
    }

    public void removeFavorite() {
        btnFavorite.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnFavorite.setText(getResources().getString(R.string.addFavorite));
    }
}
package com.febrizummiati.githubuserfinal.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.util.ReminderReceiver;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    ReminderReceiver alarmReceiver;

    private String PREF_REMINDER = "pref_reminder";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle(R.string.setting);

        ImageButton btnLanguage = findViewById(R.id.btn_language);
        TextView txtLanguage = findViewById(R.id.txt_lang);
        Switch switchReminder = findViewById(R.id.switch_reminder);
        alarmReceiver = new ReminderReceiver();

        btnLanguage.setOnClickListener(this);
        txtLanguage.setOnClickListener(this);

        preferences = getSharedPreferences(PREF_REMINDER, Context.MODE_PRIVATE);
        if (preferences != null) {
            boolean reminderCheck = preferences.getBoolean(PREF_REMINDER, false);

            if (reminderCheck) {
                switchReminder.setChecked(true);
            } else {
                switchReminder.setChecked(false);
            }
        }

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                String repeatTime = "09:00";
                String repeatMessage = getResources().getString(R.string.message_alarm);
                Context context = getApplicationContext();
                alarmReceiver.setRepeatAlarm(context, ReminderReceiver.TITLE, repeatTime, repeatMessage);
                saveSetting(true);
            } else {
                alarmReceiver.cancelReminder(this);
                saveSetting(false);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_language || v.getId()==R.id.txt_lang) {
            Intent changeLanguageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(changeLanguageIntent);
        }
    }

    public void saveSetting(Boolean reminder_setting) {
        preferences = getSharedPreferences(PREF_REMINDER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_REMINDER, reminder_setting);
        editor.apply();
    }
}
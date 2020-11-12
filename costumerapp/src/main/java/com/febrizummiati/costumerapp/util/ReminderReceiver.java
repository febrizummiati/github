package com.febrizummiati.costumerapp.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.febrizummiati.costumerapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String TITLE = "Github App";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private final static int ID_REMINDER = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        showAlarmNotification(context, message);
    }

    public void setRepeatAlarm(Context context, String type, String time, String message) {
        if (dateInvalid(time)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, R.string.alarm_on, Toast.LENGTH_SHORT).show();
    }

    private void showAlarmNotification(Context context, String message) {
        String ID_CHANNEL = "Channel_1";
        String NAME_CHANNEL = "AlarmManager channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID_CHANNEL)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(ReminderReceiver.TITLE)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 10000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(ID_CHANNEL, NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(ID_CHANNEL);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(ID_REMINDER, notification);
        }

    }

    private final static String TIME_FORMAT = "HH:mm";

    private boolean dateInvalid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(ReminderReceiver.TIME_FORMAT, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, R.string.alarm_off, Toast.LENGTH_SHORT).show();
    }
}

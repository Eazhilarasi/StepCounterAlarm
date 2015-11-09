package com.example.eazhilarasi.stepcounter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        PendingIntent Sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        NotificationManager manager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);


        //intent to call the activity which shows on ringing
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
       // Ringtone r = RingtoneManager.getRingtone(context, notification);
      //  r.play();
        MediaPlayer player;
        player = MediaPlayer.create(context, notification);
        player.setLooping(true);
        player.start();
        int value = intent.getExtras().getInt("NoOfSteps");

        //display that alarm is ringing
        Toast.makeText(context, "Alarm Ringing...!!!", Toast.LENGTH_LONG).show();
        Intent i = new Intent();
        i.putExtra("NoOfSteps", value);
        i.setClassName("com.example.eazhilarasi.stepcounter", "com.example.eazhilarasi.stepcounter.PutAlarmOff");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}

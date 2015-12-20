package com.mad.albumapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Trishaan on 12/11/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.d("demo", "Clicked");
        Intent intent_launchapp = new Intent(context, MainActivity.class);
        intent_launchapp.putExtras(intent.getExtras());
        intent_launchapp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent_launchapp);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.d("demo","hello");
        if (intent == null)
            return;

        JSONObject data;
        String title = "";
        String message = "";

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d("demo", "Push received: " + json);

            data = json.getJSONObject("data");
            title = data.getString("title");
            message = data.getString("message");

        } catch (JSONException e) {
            Log.d("demo", "Push message json exception: " + e.getMessage());
        }

        int icon = R.mipmap.ic_launcher;
        Intent cintent = new Intent(context, MainActivity.class);

        cintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        cintent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
    }
}
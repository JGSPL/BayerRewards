package com.procialize.vivo_app.gcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.procialize.vivo_app.InnerDrawerActivity.LivePollActivity;
import com.procialize.vivo_app.InnerDrawerActivity.NotificationActivity;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;


import java.util.Random;


/**
 * Created by Rajesh on 22-02-2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //private NotificationManager notificationManager1;
    SessionManager session;
    int notificationId;
    String ADMIN_CHANNEL_ID = "001";
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }
        notificationId = new Random().nextInt(60000);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("Fames bond"))
                .setContentText(getEmojiFromString(remoteMessage.getData().get("message"))).setAutoCancel(true).setSound(defaultSoundUri);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId *//* ID of notification *//*, notificationBuilder.build());*/

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("Procialize Info")
                // .setColor(Color.parseColor("#ffff00"))
                .setColorized(true)
                //.setContentTitle(remoteMessage.getData().get("Fames bond"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(remoteMessage.getData().get("message"))))
                .setContentText(getEmojiFromString(remoteMessage.getData().get("message")));

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());


        // Session Manager
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {

            // Post notification of received message.
            /*String tempString = msg.substring(0, 4);

            Toast.makeText(getBaseContext(), tempString,
                    Toast.LENGTH_SHORT).show();

            System.out.println(tempString);

            if (tempString.equalsIgnoreCase("poll")) {
                sendPollNotification(msg);
            } else {

                sendOrgNotification(remoteMessage.getData().get("message"));

            }*/
            sendOrgNotification(remoteMessage.getData().get("message"));


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    // Put the message into a notification and post it.
    private void sendOrgNotification(String msg) {

        System.out.print("Inside Gcm Service Service Service");

        /*notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);*/

        System.out.print("Inside Gcm Service");

       /* String tempString = msg.substring(0, 3);

        Toast.makeText(getBaseContext(), tempString, Toast.LENGTH_SHORT).show();

        System.out.println(tempString);

        if (tempString.equalsIgnoreCase("poll")) {

        }*/

        // Opens Notification Activty
        Intent notificationIntent = new Intent(getApplicationContext(),
                NotificationActivity.class);
        notificationIntent.putExtra("fromNotification", "fromNotification");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), new Random().nextInt(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

       /* NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("Fames bond")
                // .setColor(Color.parseColor("#ffff00"))
                .setColorized(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(msg)))
                .setContentText(getEmojiFromString(msg)).setSound(alarmSound);*/
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("Procialize")
                // .setColor(Color.parseColor("#ffff00"))
                .setColorized(true)
                //.setContentTitle(remoteMessage.getData().get("Fames bond"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getEmojiFromString(msg)))
                .setContentText(getEmojiFromString(msg));

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);

        // contentIntent.writePendingIntentOrNullToParcel(sender, out)
        if (msg != null) {

            notificationManager.notify(notificationId, mBuilder.build());

            Intent countIntent = new Intent("myBroadcastIntent");
            countIntent.putExtra("countBroadCast", "countBroadCast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(countIntent);
        }

    }

    private int getNotificationIcon() {
        boolean selectIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return selectIcon ? R.drawable.logo : R.drawable.logo;
    }

    public static String getEmojiFromString(String emojiString) {

        if (!emojiString.contains("\\u")) {

            return emojiString;
        }
        String EmojiEncodedString = "";

        int position = emojiString.indexOf("\\u");

        while (position != -1) {

            if (position != 0) {
                EmojiEncodedString += emojiString.substring(0, position);
            }

            String token = emojiString.substring(position + 2, position + 6);
            emojiString = emojiString.substring(position + 6);
            EmojiEncodedString += (char) Integer.parseInt(token, 16);
            position = emojiString.indexOf("\\u");
        }
        EmojiEncodedString += emojiString;

        return EmojiEncodedString;
    }

    // Put the message into a notification and post it.
    private void sendPollNotification(String msg) {

        System.out.print("Inside Gcm Service Service Service");

        //notificationManager = (NotificationManager) this
        //    .getSystemService(Context.NOTIFICATION_SERVICE);

        System.out.print("Inside Gcm Service");

        String url = msg.substring(msg.lastIndexOf("#") + 1);

        String finalTempMsg = msg.substring(msg.lastIndexOf("^") + 1);

        String[] parts = finalTempMsg.split("\\#");

        String finalMsg = "Please poll for " + parts[0];

        // Opens Notification Activty
        Intent notificationIntent = new Intent(getApplicationContext(),
                LivePollActivity.class);
        notificationIntent.putExtra("pollUrl", url);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), new Random().nextInt(),
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mBuilder = new NotificationCompat.Builder(
                    this)

                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Procialize")
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(finalMsg))
                    .setContentText(finalMsg).setSound(alarmSound);


            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
        } else {
            mBuilder = new NotificationCompat.Builder(
                    this)

                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Procialize")
                    .setColor(getResources().getColor(R.color.colorPrimary))

                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(finalMsg))
                    .setContentText(finalMsg).setSound(alarmSound);


            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
        }


        // contentIntent.writePendingIntentOrNullToParcel(sender, out)
        if (msg != null) {

            notificationManager.notify(notificationId, mBuilder.build());

            Intent countIntent = new Intent("myBroadcastIntent");
            countIntent.putExtra("countBroadCast", "countBroadCast");
            LocalBroadcastManager.getInstance(this).sendBroadcast(countIntent);
        }

    }

}
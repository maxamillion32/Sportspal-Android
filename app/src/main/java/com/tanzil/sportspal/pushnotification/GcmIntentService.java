package com.tanzil.sportspal.pushnotification;


import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.view.activity.MainActivity;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GcmIntentService extends IntentService {
    private static final String TAG = GcmIntentService.class.getSimpleName();
    public int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager;
    Notification myNotification;
    ActivityManager mActivityManager;
//    private GpDatabase gpDatabase;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras.getString("message");
        String extra = extras.toString();
        String data = extras.getString("invitee_name");

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        SPLog.e(TAG, "Extra Key : " + extra);
        SPLog.e(TAG, "Full Message : " + message);
        SPLog.e(TAG, "Data : " + data);
        SPLog.e(TAG, "Extras : " + extras);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), extras.getString("extra"), extras);
                SPLog.e(TAG, "In First Condition.");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), extras.getString("extra"), extras);
                SPLog.e(TAG, "In Second Condition.");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                sendNotification(message, extra, extras);

                SPLog.e(TAG, "In Third Condition.");
                SPLog.e(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    private void sendNotification(String msg, String extra, Bundle extras) {
        int num = (int) System.currentTimeMillis();
        int requestID = (int) System.currentTimeMillis();

        mActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String[] activePackages;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            activePackages = getActivePackages();
        } else {
            activePackages = getActivePackagesCompat();
        }
        if(activePackages == null)
            activePackages = new String[0];

        if (activePackages.length > 0) {
            for (String activePackage : activePackages) {
                if (!activePackage.equals(getApplicationContext().getPackageName().toString())) {

                    SPLog.e(TAG, "IF-->Extra: " + extra);
                    SPLog.e("IF-->Push Notification Message", msg);

                    //int requestID = (int) System.currentTimeMillis();
                    Intent notificationIntent;
//                    if(ModelManager.modelMgr!=null){
                        notificationIntent = new Intent(this, MainActivity.class);
//                   }else{
//                        notificationIntent = new Intent(this, SplashView.class);
//                   }
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    notificationIntent.setAction("com.tanzil.sportspal" + requestID);
                    notificationIntent.putExtra("extra", extra);
                    notificationIntent.putExtra("bundle", extras);
                   notificationIntent.putExtra("NotificationType",  extras.getString("notification_type"));

                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestID, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

                    // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    myNotification = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(msg)
                            .setTicker("Notification!")
                            .setWhen(num)
                            .setLights(Color.GREEN, 500, 500)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(getNotificationIcon())
                            .setAutoCancel(true)
                            .setContentIntent(contentIntent).build();

                    notificationManager.notify(requestID, myNotification);
                }
            }
        } else {
            SPLog.e(TAG, "else---> Extra: " + extra);
            SPLog.e("Push Notification Message:: else---> ", msg);

          //  int requestID = (int) System.currentTimeMillis();
            Intent notificationIntent;
//            if(ModelManager.modelMgr!=null){
                notificationIntent = new Intent(this, MainActivity.class);
//            }else{
//                notificationIntent = new Intent(this, SplashView.class);
//            }

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent.setAction("com.tanzil.sportspal" + requestID);
            notificationIntent.putExtra("extra", extra);
            notificationIntent.putExtra("bundle", extras);
            notificationIntent.putExtra("NotificationType", extras.getString("notification_type"));
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            myNotification = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(msg)
                    .setTicker("Notification!")
                    .setWhen(num)
                    .setLights(Color.GREEN, 500, 500)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(getNotificationIcon())
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent).build();

            notificationManager.notify(requestID, myNotification);
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see IntentService#onCreate()
     */
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.ic_launcher : R.drawable.ic_launcher;
    }


    private String[] getActivePackagesCompat() {
        final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    private String[] getActivePackages() {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }


    public void filterMessagesFromNotification(JSONObject pushJson) {
//        try {
//            gpDatabase = new GpDatabase(GcmIntentService.this);
//
//            JSONObject msgData = pushJson.getJSONObject("message");
//            JSONObject userData = msgData.getJSONObject("user");
//
//            if (pushJson.getString(pushJson.getString("type")).equalsIgnoreCase(Constants.MATCH_CHAT)) {
//
//            } else if (pushJson.getString(pushJson.getString("type")).equalsIgnoreCase(Constants.GROUP_CHAT)) {
//
//            } else {
//                ConversationI newCon = new ConversationI();
//                newCon.setConId(msgData.getString("channel_id"));
//                newCon.setConType(msgData.getString("type"));
//                newCon.setConName(userData.getString("name"));
//                newCon.setConMsg(msgData.getString("content"));
//                newCon.setConTime(msgData.getString("date_time"));
//                newCon.setConImage(msgData.getString("avatar"));
//
//                MessageI newMsg = new MessageI();
//                newMsg.setMessageID(msgData.getString("message_id"));
//                newMsg.setMsgUserId(userData.getString("user_id"));
//                newMsg.setMsgUserName(userData.getString("name"));
//                newMsg.setMsgUserImage(userData.getString("avatar"));
//                newMsg.setMessageType(msgData.getString("content_type"));
//                newMsg.setMessageText(msgData.getString("content"));
//                newMsg.setMessageTime(Integer.parseInt(msgData.getString("date_time")));
//                gpDatabase.addConversationFromNotification(newCon,newMsg);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
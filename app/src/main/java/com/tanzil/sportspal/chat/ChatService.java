package com.tanzil.sportspal.chat;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by mukesh on 1/6/15.
 */


public class ChatService extends Service {

    private final IBinder mBinder = new MyLocalBinder();
    private ChatThread mChatThread;
    private NotificationManager mNM;
    private int mId = 0;
 //   private Handler handler;

    @Override
    public void onCreate() {
        System.out.print("SERVICE IS CREATED");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        EventBus.getDefault().register(this);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                       /* Bundle data = msg.getData();
                        HashMap<String, Object> fields = new HashMap<String, Object>();
                        showNotification("GamePlan", data.getString("msgText"));*/
                        break;


                }
            }
        };
        mChatThread = new ChatThread(getApplication(), handler, this);
        if (!mChatThread.isAlive()) {
            ChatThread.isRuningThead = true;
            mChatThread.start();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        SPLog.e("ChatService", "in onUnbind");
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        SPLog.e("ChatService", "in onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(getApplicationContext());
        try {
            SPLog.e("isInterrupted", "" + mChatThread.isInterrupted() + "---isAlive--" + mChatThread.isAlive());
            ChatThread.isRuningThead = false;
            //messageHistroy(Constants.LOGOUT_UNSUBSCRIBE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        SPLog.e("ChatService", "in onRebind");
        super.onRebind(intent);
    }


    public void messageHistroy(int messagei) {
        Handler handler = mChatThread.getHandler();
        android.os.Message msg = new android.os.Message();
        Bundle data = new Bundle();
        data.putString("userId", "parter");
        switch (messagei) {
            /*case Constants.CHAT_HISTORY:
                data.putString("messageText", "histroy");
                break;
            case Constants.LOGOUT_UNSUBSCRIBE:
                data.putString("messageText", "Logout");
                break;*/
        }
        msg.setData(data);
        msg.what = messagei;
        handler.sendMessage(msg);
    }


    public class MyLocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    private void showNotification(String title, String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message);

        mNM.notify(mId++, mBuilder.build());
    }

    public void onEventMainThread(String getMsg) {

    }

}

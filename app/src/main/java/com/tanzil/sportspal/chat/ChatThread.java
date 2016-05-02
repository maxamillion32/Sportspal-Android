package com.tanzil.sportspal.chat;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.PnGcmMessage;
import com.pubnub.api.PnMessage;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import com.tanzil.sportspal.Utility.SPLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import de.greenrobot.event.EventBus;

/**
 * Created by mukesh on 1/6/15.
 */
public class ChatThread extends Thread {

    private static final String TAG = ChatThread.class.getSimpleName();

    public Application application;
    public Context activity;
  //  private Pubnub mPubnub = null;
    private Handler mMyHandler;
    private final Handler serviceHandler;
    public static final int SEND_CHAT = 0;
    public static final int CHAT_HISTROY = 100;
    public static final int NOTIFICATION = 10;
    //private GpDatabase gpDatabase;
    private boolean groupDeleted;
    private boolean canIAddThisMessageInList = true;
    String sk = "";
    public static boolean isRuningThead;
    public static Looper myLooper = null;


    public ChatThread(Application context, Handler handler, Context act) {
        System.out.print("ChatThread Con");
        //chatManager = ModelManager.getInstance().getchatManager();
       // profileManager = ModelManager.getInstance().getProfileManager();
     //   notificationManager = ModelManager.getInstance().getNotificationManager();
        application = context;
        activity = act;
        serviceHandler = handler;
       // String mubscriberKey = GpPreferences.readString(application, GpPreferences.SUBSCRIBE_KEY, "");//; ModelManager.getInstance().getAuthManager().getUserLoginInfo().get(0).getSubscriberKey();
       // sk = mubscriberKey;
        //mPubnub = new Pubnub("", mubscriberKey);
       // gpDatabase = GpDatabase.getInstance(context);
    }

    public Handler getHandler() {
        return mMyHandler;
    }

    @Override
    public void run() {
        System.out.print("ChatThread>>> " + isRuningThead);
        while (isRuningThead) {
            System.out.print("ChatThread run");
            Looper.prepare();
            subscribeUser();
            EventBus.getDefault().register(this);
            mMyHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case SEND_CHAT:
                            break;
                        case CHAT_HISTROY:
                            getHistroy();
                            break;
                        case NOTIFICATION:
                            sendNotification();
                            break;
                       /* case Constants.LOGOUT_UNSUBSCRIBE:
                            unSubscribeUser();
                            break;*/

                    }
                }
            };
            Looper.loop();
            EventBus.getDefault().unregister(this);
            myLooper = null;
        }
    }

    public void onEventMainThread(String getMsg) {
        if (getMsg.equalsIgnoreCase("Logined in chat")) {

        }
    }

    private void getHistroy() {
       /* String userID = GpPreferences.readString(application, GpPreferences.USER_ID, "");
        String mduid = ChatUtils.getMD5HashKey(userID);
        String timeToken = GpPreferences.readString(application, GpPreferences.CHAT_ACTIVE_TIME, "");
        GpLog.e(TAG, "timeTokentimeTokentimeToken--->" + timeToken);
        if (GpUtil.isEmptyString(timeToken)) {
            mPubnub.history(mduid, false, histroyCallback);
        } else {
            long timeInLong = Long.parseLong(timeToken);
            mPubnub.history(mduid, (timeInLong * 10000000), true, histroyCallback);
        }*/

    }

    private void subscribeUser() {
       /* try {
            mPubnub.setCacheBusting(false);
            String userID = GpPreferences.readString(application, GpPreferences.USER_ID, "");
            String mduid = ChatUtils.getMD5HashKey(userID);
            mPubnub.setAuthKey(ChatUtils.getMD5HashKey(userID));
            mPubnub.setUUID(mduid);
            mPubnub.isResumeOnReconnect();
            GpLog.e("fffff", "mduidmduidmduid---> " + mduid);
            mPubnub.subscribe(mduid, pubnubConnectionCallback);//USERID
            sendRegistrationId();
        } catch (PubnubException e) {
            e.printStackTrace();
        }*/
    }

    private void unSubscribeUser() {
      /*  try {
            deRegistrationId();
            mPubnub.unsubscribeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    private void sendRegistrationId() {

     /*   String deviceId = GpPreferences.readString(application, GpPreferences.DEVICE_ID, "");
        String userID = GpPreferences.readString(application, GpPreferences.USER_ID, "");
        String mduid = ChatUtils.getMD5HashKey(userID);
        mPubnub.enablePushNotificationsOnChannel(mduid, deviceId, callbackpn);
        try {
            mPubnub.presence(userID, callbackpn);
        } catch (PubnubException e) {
            e.printStackTrace();
        }*/
    }

    private void deRegistrationId() {
      /*  GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(application);
        try {
            gcm.unregister();
        }
        catch (IOException e) {
            System.out.println("Error Message: " + e.getMessage());
        }
        String deviceId = GpPreferences.readString(application, GpPreferences.DEVICE_ID, "");
        String userID = GpPreferences.readString(application, GpPreferences.USER_ID, "");
        String mduid = ChatUtils.getMD5HashKey(userID);
        mPubnub.disablePushNotificationsOnChannel(mduid, deviceId, callbackpn);*/
    }


    public Callback callbackpn = new Callback() {
        @Override
        public void successCallback(String channel, Object message) {
            Log.i(TAG, "Success on Channel rrrrrrrrrrrrrrrrrrrrrrrrrrr" + channel + " : " + message);
            // sendNotification();

        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.i(TAG, "Error On Channeloooooooooooooooooooooooooo " + channel + " : " + error);
        }
    };


    public void sendNotification() {
       /* PnGcmMessage gcmMessage = new PnGcmMessage();
        JSONObject jso = new JSONObject();
        try {
            jso.put("GCMSays", "himmmm");
        } catch (JSONException e) {
        }
        gcmMessage.setData(jso);

        String userID = GpPreferences.readString(application, GpPreferences.USER_ID, "");
        String mduid = ChatUtils.getMD5HashKey(userID);
        PnMessage message = new PnMessage(mPubnub, mduid, callbackpn, gcmMessage);
        try {
            message.publish();
        } catch (PubnubException e) {
            e.printStackTrace();
        }*/
    }


    private Callback pubnubConnectionCallback = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {

            SPLog.e(TAG, "SUBSCRIBE 1: CONNECT on channel:" + channel + " : " + message.getClass() + " : " + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            SPLog.e(TAG, "SUBSCRIBE 2: DISCONNECT on channel:" + channel + " : " + message.getClass() + " : " + message.toString());
        }

        @Override
        public void reconnectCallback(String channel, Object message) {
            SPLog.e(TAG, "SUBSCRIBE 3: RECONNECT on channel:" + channel + " : " + message.getClass() + " : " + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            SPLog.e(TAG, "SUBSCRIBE 4: " + channel + " : " + message.getClass() + " : " + message.toString());
            canIAddThisMessageInList = true;
            groupDeleted = false;
           /* JSONObject msgJsonData = null;
            //MessageI newMsg = null;
            ConversationI cnvri = null;
            ArrayList<MessageI> messageIArrayList = null;

            try {
                msgJsonData = new JSONObject(message.toString());

                if (msgJsonData.has("pn_gcm")) {
                    NotificationI notiBean = new NotificationI();
                    JSONObject gcmData = msgJsonData.getJSONObject("pn_gcm");
                    JSONObject notiData = gcmData.getJSONObject("data");
                    notiBean.setMessage_text(notiData.getString("message"));
                    if (notiData.getString("notification_type").equalsIgnoreCase("28")) {
                        EventBus.getDefault().post("UpdateProfileData");
                    } else {
                        if (notiData.has("notification_id"))
                            notiBean.setId(notiData.getString("notification_id"));
                        if (notiData.has("search_id"))
                            notiBean.setSearch_id(notiData.getString("search_id"));
                        if (notiData.has("match_id"))
                            notiBean.setMatch_id(notiData.getString("match_id"));
                        notiBean.setNotificationType(notiData.getString("notification_type"));
                        notificationManager.getPubnubNotificationData().add(notiBean);
                        EventBus.getDefault().post("NotificationFromPubnub True");
                    }
                } else {
                    JSONObject msgData = msgJsonData.getJSONObject("message");
                    JSONObject userData = msgData.getJSONObject("user");

                    if (chatManager.getConversations().size() > 0) {
                        boolean check = false;
                        for (int m = 0; m < chatManager.getConversations().size(); m++) {
                            if (msgData.getString("channel_id").equalsIgnoreCase(chatManager.getConversations().get(m).getConId())) {

                                ArrayList<MessageI> msgArray = null;
                                if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {
                                    if (msgData.getString("team").equalsIgnoreCase("s"))
                                        msgArray = chatManager.getConversations().get(m).getSquadmessages();
                                    else
                                        msgArray = chatManager.getConversations().get(m).getTeamMessages();
                                } else {
                                    msgArray = chatManager.getConversations().get(m).getMessageIArrayList();
                                }

                                for (int c = 0; c < msgArray.size(); c++) {
                                    if (msgData.getString("message_id").equalsIgnoreCase(msgArray.get(c).getMessageID())) {
                                        MessageI updateMsg = new MessageI();
                                        updateMsg.setMessageID(msgData.getString("message_id"));
                                        if (msgData.getString("content_type").equalsIgnoreCase("Image")) {
                                            updateMsg.setChatImage(msgData.getString("image_url"));
                                            if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {
                                                if (msgData.getString("team").equalsIgnoreCase("s"))
                                                    chatManager.getConversations().get(m).getSquadmessages().get(c).setChatImage(msgData.getString("image_url"));
                                                else
                                                    chatManager.getConversations().get(m).getTeamMessages().get(c).setChatImage(msgData.getString("image_url"));
                                            } else {
                                                chatManager.getConversations().get(m).getMessageIArrayList().get(c).setChatImage(msgData.getString("image_url"));
                                            }
                                        }
                                        updateMsg.setMessageTime(Integer.parseInt(msgData.getString("date_time")));
                                        chatManager.setUpdateMessageTime(updateMsg);
                                        canIAddThisMessageInList = false;
                                        break;
                                    }
                                }
                                check = true;
                                if (canIAddThisMessageInList) {

                                    chatManager.setMsgTotalCount(chatManager.getMsgTotalCount() + 1);

                                    newMsg = new MessageI();
                                    ConversationI updateCon = new ConversationI();
                                    updateCon.setConId(msgData.getString("channel_id"));
                                    updateCon.setConType(msgData.getString("type"));
                                    updateCon.setUser_id(profileManager.getProfileInfo("", false).get(0).getID());
                                    chatManager.getConversations().get(m).setUser_id(profileManager.getProfileInfo("", false).get(0).getID());
                                    chatManager.getConversations().get(m).setConId(msgData.getString("channel_id"));
                                    chatManager.getConversations().get(m).setConType(msgData.getString("type"));
                                    chatManager.getConversations().get(m).setUnReadCount(chatManager.getConversations().get(m).getUnReadCount() + 1);

                                    if (msgData.getString("type").equalsIgnoreCase(Constants.INDIVIDUAL_CHAT)) {

                                        updateCon.setConName(userData.getString("name"));
                                        updateCon.setConImage(userData.getString("avatar"));
                                        chatManager.getConversations().get(m).setConName(userData.getString("name"));
                                        chatManager.getConversations().get(m).setConImage(userData.getString("avatar"));

                                        if (msgData.getString("content_type").equalsIgnoreCase("Image")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                updateCon.setConLastUser("You");
                                                newMsg.setIsSender(4);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(5);
                                            }
                                            newMsg.setChatImage(msgData.getString("image_url"));
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {

                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                updateCon.setConLastUser("You");
                                                newMsg.setIsSender(4);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(5);
                                            }
                                            String lat = msgData.getString("latitude");
                                            String lng = msgData.getString("longitude");
                                            newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                            newMsg.setSharedLng(lng);
                                            newMsg.setSharedLat(lat);
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {

                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(6);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(7);
                                            }
                                            newMsg.setShareConName(msgData.getString("contact_name"));
                                            newMsg.setShareConNumber(msgData.getString("contact_number"));
                                        } else {

                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                newMsg.setIsSender(0);
                                            } else {
                                                newMsg.setIsSender(1);
                                            }
                                        }

                                    } else if (msgData.getString("type").equalsIgnoreCase(Constants.GROUP_CHAT)) {
                                        if (msgData.has("group_action")) {
                                            if (msgData.getString("group_action").equalsIgnoreCase("group deleted")) {
                                                chatManager.getConversations().remove(m);
                                                gpDatabase.deleteMessages(msgData.getString("channel_id"));
                                                groupDeleted = true;
                                                EventBus.getDefault().post("DeleteConversation True");
                                                break;
                                            } else if (msgData.getString("group_action").equalsIgnoreCase("group updated")) {
                                                updateCon.setGroupAction(msgData.getString("group_action"));
                                                chatManager.getConversations().get(m).setGroupAction(msgData.getString("group_action"));
                                                if (msgData.has("group_image")) {
                                                    if (!GpUtil.isEmptyString(msgData.getString("group_image"))) {
                                                        chatManager.getConversations().get(m).setConImage(msgData.getString("group_image"));
                                                    }
                                                }
                                                if (msgData.has("group_name")) {
                                                    if (!GpUtil.isEmptyString(msgData.getString("group_name"))) {
                                                        chatManager.getConversations().get(m).setConName(msgData.getString("group_name"));
                                                    }
                                                }
                                                if (msgData.has("admin_id")) {
                                                    updateCon.setGroupAdmin(msgData.getString("admin_id"));
                                                    chatManager.getConversations().get(m).setGroupAdmin(msgData.getString("admin_id"));
                                                }
                                                newMsg.setIsSender(3);
                                            } else {
                                                updateCon.setGroupAction(msgData.getString("group_action"));
                                                chatManager.getConversations().get(m).setGroupAction(msgData.getString("group_action"));
                                                if (msgData.has("admin_id")) {
                                                    updateCon.setGroupAdmin(msgData.getString("admin_id"));
                                                    chatManager.getConversations().get(m).setGroupAdmin(msgData.getString("admin_id"));
                                                }
                                                newMsg.setIsSender(3);
                                            }
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Image")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(4);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(5);
                                            }
                                            newMsg.setChatImage(msgData.getString("image_url"));
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(4);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(5);
                                            }
                                            String lat = msgData.getString("latitude");
                                            String lng = msgData.getString("longitude");
                                            newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                            newMsg.setSharedLng(lng);
                                            newMsg.setSharedLat(lat);
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(6);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(7);
                                            }
                                            newMsg.setShareConName(msgData.getString("contact_name"));
                                            newMsg.setShareConNumber(msgData.getString("contact_number"));
                                        } else {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(0);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(1);
                                            }
                                        }
                                        if (!groupDeleted) {
                                            updateCon.setConName(msgData.getString("group_name"));
                                            updateCon.setConImage(msgData.getString("group_image"));
                                            chatManager.getConversations().get(m).setConName(msgData.getString("group_name"));
                                            chatManager.getConversations().get(m).setConImage(msgData.getString("group_image"));
                                        }

                                    } else if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {
                                        updateCon.setConName(msgData.getString("venue_name"));
                                        updateCon.setConMatchTime(msgData.getString("match_time"));
                                        updateCon.setConImage("");

                                        chatManager.getConversations().get(m).setConName(msgData.getString("venue_name"));//VenueName
                                        chatManager.getConversations().get(m).setConMatchTime(msgData.getString("match_time"));
                                        chatManager.getConversations().get(m).setConImage("");

                                        if (msgData.has("group_action")) {
                                            updateCon.setGroupAction(msgData.getString("group_action"));
                                            chatManager.getConversations().get(m).setGroupAction(msgData.getString("group_action"));
                                            if (msgData.has("admin_id")) {
                                                updateCon.setGroupAdmin(msgData.getString("admin_id"));
                                                chatManager.getConversations().get(m).setGroupAdmin(msgData.getString("admin_id"));
                                            }
                                            newMsg.setIsSender(3);
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Image")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(4);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(5);
                                            }
                                            newMsg.setChatImage(msgData.getString("image_url"));
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(4);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(5);
                                            }
                                            String lat = msgData.getString("latitude");
                                            String lng = msgData.getString("longitude");
                                            newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                            newMsg.setSharedLng(lng);
                                            newMsg.setSharedLat(lat);
                                        } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                                newMsg.setIsSender(6);
                                            } else {
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                                newMsg.setIsSender(7);
                                            }
                                            newMsg.setShareConName(msgData.getString("contact_name"));
                                            newMsg.setShareConNumber(msgData.getString("contact_number"));
                                        } else {

                                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                                newMsg.setIsSender(0);
                                                updateCon.setConLastUser("You");
                                                chatManager.getConversations().get(m).setConLastUser("You");
                                            } else {
                                                newMsg.setIsSender(1);
                                                updateCon.setConLastUser(userData.getString("name"));
                                                chatManager.getConversations().get(m).setConLastUser(userData.getString("name"));
                                            }
                                        }

                                        newMsg.setPlayPosition(userData.getString("position"));
                                        if (msgData.getString("team").equalsIgnoreCase("s"))
                                            newMsg.setPlayerTeam(userData.getString("team"));


                                    }
                                    updateCon.setConMsg(msgData.getString("content"));
                                    updateCon.setConTime(msgData.getString("date_time"));
                                    chatManager.getConversations().get(m).setConMsg(msgData.getString("content"));
                                    chatManager.getConversations().get(m).setConTime(msgData.getString("date_time"));

                                    newMsg.setMessageID(msgData.getString("message_id"));
                                    newMsg.setMsgUserId(userData.getString("user_id"));
                                    newMsg.setMsgUserName(userData.getString("name"));
                                    newMsg.setMsgUserImage(userData.getString("avatar"));
                                    newMsg.setMessageType(msgData.getString("content_type"));
                                    newMsg.setMessageText(msgData.getString("content"));
                                    if (msgData.has("team"))
                                        newMsg.setMatchChatType(msgData.getString("team"));
                                    // newMsg.setMessageTime(msgData.getString("date_time"));
                                    newMsg.setMessageTime(Integer.parseInt(msgData.getString("date_time")));
                                    GpPreferences.writeString(application.getApplicationContext(), GpPreferences.CHAT_ACTIVE_TIME, msgData.getString("date_time"));
                                    newMsg.setCnvrId(msgData.getString("channel_id"));

                                    if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {
                                        if (msgData.getString("team").equalsIgnoreCase("s"))
                                            chatManager.getConversations().get(m).getSquadmessages().add(newMsg);
                                        else
                                            chatManager.getConversations().get(m).getTeamMessages().add(newMsg);
                                    } else {
                                        chatManager.getConversations().get(m).getMessageIArrayList().add(newMsg);
                                    }
                                    System.out.println("IF channel_id-->" + msgData.getString("channel_id"));
                                    gpDatabase.addConversation(updateCon);
                                    gpDatabase.addMessage(newMsg);
                                }
                                break;
                            }
                        }
                        if (!check) {
                            messageIArrayList = new ArrayList<MessageI>();
                            cnvri = new ConversationI();
                            newMsg = new MessageI();

                            chatManager.setMsgTotalCount(chatManager.getMsgTotalCount() + 1);

                            cnvri.setConId(msgData.getString("channel_id"));
                            cnvri.setConType(msgData.getString("type"));
                            cnvri.setUser_id(profileManager.getProfileInfo("", false).get(0).getID());

                            if (msgData.getString("type").equalsIgnoreCase(Constants.INDIVIDUAL_CHAT)) {

                                cnvri.setConName(userData.getString("name"));
                                cnvri.setConImage(userData.getString("avatar"));

                                if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(4);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(5);
                                    }
                                    newMsg.setChatImage(msgData.getString("image_url"));
                                } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(4);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(5);
                                    }
                                    String lat = msgData.getString("latitude");
                                    String lng = msgData.getString("longitude");
                                    newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                    newMsg.setSharedLng(lng);
                                    newMsg.setSharedLat(lat);
                                } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(6);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(7);
                                    }
                                    newMsg.setShareConName(msgData.getString("contact_name"));
                                    newMsg.setShareConNumber(msgData.getString("contact_number"));
                                } else {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        newMsg.setIsSender(0);
                                    } else {
                                        newMsg.setIsSender(1);
                                    }
                                }
                            } else if (msgData.getString("type").equalsIgnoreCase(Constants.GROUP_CHAT)) {
                                if (msgData.has("group_action")) {
                                    cnvri.setGroupAction(msgData.getString("group_action"));
                                    if (msgData.has("admin_id"))
                                        cnvri.setGroupAdmin(msgData.getString("admin_id"));
                                    newMsg.setIsSender(3);
                                } else if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(4);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(5);
                                    }
                                    newMsg.setChatImage(msgData.getString("image_url"));
                                } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(4);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(5);
                                    }
                                    String lat = msgData.getString("latitude");
                                    String lng = msgData.getString("longitude");
                                    newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                    newMsg.setSharedLng(lng);
                                    newMsg.setSharedLat(lat);
                                } else {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        newMsg.setIsSender(0);
                                        cnvri.setConLastUser("You");
                                    } else {
                                        newMsg.setIsSender(1);
                                        cnvri.setConLastUser(userData.getString("name"));
                                    }
                                }
                                cnvri.setConName(msgData.getString("group_name"));
                                cnvri.setConImage(msgData.getString("group_image"));
                            } else if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {

                                cnvri.setConName(msgData.getString("venue_name"));//VenueName
                                cnvri.setConMatchTime(msgData.getString("match_time"));
                                cnvri.setConImage("");

                                newMsg.setPlayPosition(userData.getString("position"));
                                if (msgData.getString("team").equalsIgnoreCase("s"))
                                    newMsg.setPlayerTeam(userData.getString("team"));

                                if (msgData.has("group_action")) {
                                    cnvri.setGroupAction(msgData.getString("group_action"));
                                    if (msgData.has("admin_id"))
                                        cnvri.setGroupAdmin(msgData.getString("admin_id"));
                                    newMsg.setIsSender(3);
                                } else if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(4);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(5);
                                    }
                                    newMsg.setChatImage(msgData.getString("image_url"));
                                } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        cnvri.setConLastUser("You");
                                        newMsg.setIsSender(4);
                                    } else {
                                        cnvri.setConLastUser(userData.getString("name"));
                                        newMsg.setIsSender(5);
                                    }
                                    String lat = msgData.getString("latitude");
                                    String lng = msgData.getString("longitude");
                                    newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                    newMsg.setSharedLng(lng);
                                    newMsg.setSharedLat(lat);
                                } else {
                                    if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                        newMsg.setIsSender(0);
                                        cnvri.setConLastUser("You");
                                    } else {
                                        newMsg.setIsSender(1);
                                        cnvri.setConLastUser(userData.getString("name"));
                                    }
                                }
                            }
                            cnvri.setConMsg(msgData.getString("content"));
                            cnvri.setConTime(msgData.getString("date_time"));
                            newMsg.setMessageID(msgData.getString("message_id"));
                            newMsg.setMsgUserId(userData.getString("user_id"));
                            newMsg.setMsgUserName(userData.getString("name"));
                            newMsg.setMsgUserImage(userData.getString("avatar"));
                            newMsg.setMessageType(msgData.getString("content_type"));
                            newMsg.setMessageText(msgData.getString("content"));
                            newMsg.setMessageTime(Integer.parseInt(msgData.getString("date_time")));

                            GpPreferences.writeString(application.getApplicationContext(), GpPreferences.CHAT_ACTIVE_TIME, msgData.getString("date_time"));

                            cnvri.setUnReadCount(1);

                            newMsg.setCnvrId(msgData.getString("channel_id"));
                            if (msgData.has("team"))
                                newMsg.setMatchChatType(msgData.getString("team"));
                            messageIArrayList.add(newMsg);

                            if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {
                                if (msgData.getString("team").equalsIgnoreCase("s"))
                                    cnvri.setSquadmessages(messageIArrayList);
                                else
                                    cnvri.setTeamMessages(messageIArrayList);
                            } else {
                                cnvri.setMessageIArrayList(messageIArrayList);
                            }
                            gpDatabase.addConversation(cnvri);
                            gpDatabase.addMessage(newMsg);
                            chatManager.getConversations().add(cnvri);
                        }
                    } else {
                        messageIArrayList = new ArrayList<MessageI>();
                        cnvri = new ConversationI();
                        newMsg = new MessageI();
                        chatManager.setMsgTotalCount(chatManager.getMsgTotalCount() + 1);
                        cnvri.setConId(msgData.getString("channel_id"));
                        cnvri.setConType(msgData.getString("type"));
                        cnvri.setUser_id(profileManager.getProfileInfo("", false).get(0).getID());
                        if (msgData.getString("type").equalsIgnoreCase(Constants.INDIVIDUAL_CHAT)) {

                            cnvri.setConName(userData.getString("name"));
                            cnvri.setConImage(userData.getString("avatar"));
                            if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(4);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(5);
                                }
                                newMsg.setChatImage(msgData.getString("image_url"));
                            } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(4);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(5);
                                }
                                String lat = msgData.getString("latitude");
                                String lng = msgData.getString("longitude");
                                newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                newMsg.setSharedLng(lng);
                                newMsg.setSharedLat(lat);
                            } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(6);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(7);
                                }
                                newMsg.setShareConName(msgData.getString("contact_name"));
                                newMsg.setShareConNumber(msgData.getString("contact_number"));
                            } else {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    newMsg.setIsSender(0);
                                } else {
                                    newMsg.setIsSender(1);
                                }
                            }
                        } else if (msgData.getString("type").equalsIgnoreCase(Constants.GROUP_CHAT)) {

                            if (msgData.has("group_action")) {
                                cnvri.setGroupAction(msgData.getString("group_action"));
                                if (msgData.has("admin_id"))
                                    cnvri.setGroupAdmin(msgData.getString("admin_id"));
                                newMsg.setIsSender(3);
                            } else if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(4);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(5);
                                }
                                newMsg.setChatImage(msgData.getString("image_url"));
                            } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(4);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(5);
                                }
                                String lat = msgData.getString("latitude");
                                String lng = msgData.getString("longitude");
                                newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                newMsg.setSharedLng(lng);
                                newMsg.setSharedLat(lat);
                            } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(6);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(7);
                                }
                                newMsg.setShareConName(msgData.getString("contact_name"));
                                newMsg.setShareConNumber(msgData.getString("contact_number"));
                            } else {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    newMsg.setIsSender(0);
                                    cnvri.setConLastUser("You");
                                } else {
                                    newMsg.setIsSender(1);
                                    cnvri.setConLastUser(userData.getString("name"));
                                }
                            }

                            cnvri.setConName(msgData.getString("group_name"));
                            cnvri.setConImage(msgData.getString("group_image"));

                        } else if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {

                            cnvri.setConName(msgData.getString("venue_name"));//VenueName
                            cnvri.setConMatchTime(msgData.getString("match_time"));
                            cnvri.setConImage("");
                            newMsg.setPlayPosition(userData.getString("position"));
                            if (msgData.getString("team").equalsIgnoreCase("s"))
                                newMsg.setPlayerTeam(userData.getString("team"));


                            if (msgData.has("group_action")) {
                                cnvri.setGroupAction(msgData.getString("group_action"));
                                if (msgData.has("admin_id"))
                                    cnvri.setGroupAdmin(msgData.getString("admin_id"));
                                newMsg.setIsSender(3);
                            } else if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(4);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(5);
                                }
                                newMsg.setChatImage(msgData.getString("image_url"));
                            } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(4);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(5);
                                }
                                String lat = msgData.getString("latitude");
                                String lng = msgData.getString("longitude");
                                newMsg.setChatImage(GpUtil.getStaticMapUrl(lat + "," + lng));
                                newMsg.setSharedLng(lng);
                                newMsg.setSharedLat(lat);
                            } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    cnvri.setConLastUser("You");
                                    newMsg.setIsSender(6);
                                } else {
                                    cnvri.setConLastUser(userData.getString("name"));
                                    newMsg.setIsSender(7);
                                }
                                newMsg.setShareConName(msgData.getString("contact_name"));
                                newMsg.setShareConNumber(msgData.getString("contact_number"));
                            } else {
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    newMsg.setIsSender(0);
                                    cnvri.setConLastUser("You");
                                } else {
                                    newMsg.setIsSender(1);
                                    cnvri.setConLastUser(userData.getString("name"));
                                }
                            }
                        }
                        cnvri.setConMsg(msgData.getString("content"));
                        cnvri.setConTime(msgData.getString("date_time"));
                        newMsg.setMessageID(msgData.getString("message_id"));
                        newMsg.setMsgUserId(userData.getString("user_id"));
                        newMsg.setMsgUserName(userData.getString("name"));
                        newMsg.setMsgUserImage(userData.getString("avatar"));
                        newMsg.setMessageType(msgData.getString("content_type"));
                        newMsg.setMessageText(msgData.getString("content"));
                        GpPreferences.writeString(application.getApplicationContext(), GpPreferences.CHAT_ACTIVE_TIME, msgData.getString("date_time"));
                        cnvri.setUnReadCount(1);

                        newMsg.setMessageTime(Integer.parseInt(msgData.getString("date_time")));
                        newMsg.setCnvrId(msgData.getString("channel_id"));
                        if (msgData.has("team"))
                            newMsg.setMatchChatType(msgData.getString("team"));
                        messageIArrayList.add(newMsg);


                        if (msgData.getString("type").equalsIgnoreCase(Constants.MATCH_CHAT)) {
                            if (msgData.getString("type").equalsIgnoreCase("s"))
                                cnvri.setSquadmessages(messageIArrayList);
                            else
                                cnvri.setTeamMessages(messageIArrayList);
                        } else {
                            cnvri.setMessageIArrayList(messageIArrayList);
                        }
                        chatManager.getConversations().add(cnvri);
                        gpDatabase.addConversation(cnvri);
                        gpDatabase.addMessage(newMsg);
                    }

                    EventBus.getDefault().post("UpdateCounter");
                    EventBus.getDefault().post("ChatMessageRecieve True");
                    if (Constants.ISNOTIFICATION_ON) {

                    } else {
                        //sendNotification(msgJsonData.getString("message"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            SPLog.e(TAG, "SUBSCRIBE 5: ERROR on channel " + channel + " : " + error.toString());
        }
    };

    // retrieve last 100 messages
    private Callback histroyCallback = new Callback() {
        public void successCallback(String channel, Object response) {
          /*  try {
                SPLog.largeLog(TAG, "----mmmmmmm----->" + response.toString());
                System.out.println(response.toString() + "... channel=>successCallback" + channel);
                JSONArray jsonarray = new JSONArray(response.toString());
                JSONArray obj = jsonarray.getJSONArray(0);
                chatManager.getConversations().clear();
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject objMsg = obj.getJSONObject(j);
                    if (objMsg.has("pn_gcm")) {
                        GpLog.e("CHAT_SERVICE", "histroyCallback------> " + j);
                    } else {
                        JSONObject msgData = objMsg.getJSONObject("message");
                        JSONObject usrData = msgData.getJSONObject("user");

                        ConversationI histroyCon = new ConversationI();
                        MessageI histroyMsg = new MessageI();

                        if (msgData.has("channel_id"))
                            histroyCon.setConId(msgData.getString("channel_id"));
                        if (msgData.has("type"))
                            histroyCon.setConType(msgData.getString("type"));
                        if (usrData.has("name"))
                            histroyCon.setConName(usrData.getString("name"));
                        if (usrData.has("avatar"))
                            histroyCon.setConImage(usrData.getString("avatar"));
                        if (msgData.has("group_action"))
                            histroyCon.setGroupAction(msgData.getString("group_action"));
                        if (msgData.has("admin_id"))
                            histroyCon.setGroupAdmin(msgData.getString("admin_id"));
                        if (msgData.has("content"))
                            histroyCon.setConMsg(msgData.getString("content"));
                        if (msgData.has("date_time"))
                            histroyCon.setConTime(msgData.getString("date_time"));
                        if (msgData.has("venue_name"))
                            histroyCon.setConName(msgData.getString("venue_name"));
                        if (msgData.has("group_name"))
                            histroyCon.setConName(msgData.getString("group_name"));
                        if (usrData.has("name"))
                            histroyCon.setConLastUser(usrData.getString("name"));

                        if (msgData.has("match_time"))
                            histroyCon.setConMatchTime(msgData.getString("match_time"));
                        if (!GpUtil.isEmptyString(profileManager.getProfileInfo("", false).get(0).getID()))
                            histroyCon.setUser_id(profileManager.getProfileInfo("", false).get(0).getID());
                        // Message
                        if (msgData.has("channel_id"))
                            histroyMsg.setCnvrId(msgData.getString("channel_id"));
                        if (msgData.has("message_id"))
                            histroyMsg.setMessageID(msgData.getString("message_id"));
                        if (msgData.has("content_type"))
                            histroyMsg.setMessageType(msgData.getString("content_type"));
                        if (msgData.has("content"))
                            histroyMsg.setMessageText(msgData.getString("content"));
                        if (msgData.has("date_time"))
                            histroyMsg.setMessageTime(Integer.parseInt(msgData.getString("date_time")));
                        if (msgData.has("image_url"))
                            histroyMsg.setChatImage(msgData.getString("image_url"));
                        if (msgData.has("latitude"))
                            histroyMsg.setChatImage(GpUtil.getStaticMapUrl(msgData.getString("latitude") + "," + msgData.getString("longitude")));
                        if (msgData.has("longitude"))
                            histroyMsg.setSharedLng(msgData.getString("longitude"));
                        if (msgData.has("latitude"))
                            histroyMsg.setSharedLat(msgData.getString("latitude"));
                        if (msgData.has("contact_name"))
                            histroyMsg.setShareConName(msgData.getString("contact_name"));
                        if (msgData.has("contact_number"))
                            histroyMsg.setShareConNumber(msgData.getString("contact_number"));
                        if (usrData.has("position"))
                            histroyMsg.setPlayPosition(usrData.getString("position"));
                        if (usrData.has("team"))
                            histroyMsg.setPlayerTeam(usrData.getString("team"));
                        if (msgData.has("team"))
                            histroyMsg.setMatchChatType(msgData.getString("team"));


                        //User
                        if (usrData.has("user_id"))
                            histroyMsg.setMsgUserId(usrData.getString("user_id"));
                        if (usrData.has("name"))
                            histroyMsg.setMsgUserName(usrData.getString("name"));
                        if (usrData.has("avatar"))
                            histroyMsg.setMsgUserImage(usrData.getString("avatar"));

                        if (msgData.getString("content_type").equalsIgnoreCase("image")) {
                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                histroyMsg.setIsSender(4);
                            } else {
                                histroyMsg.setIsSender(5);
                            }
                        } else if (msgData.getString("content_type").equalsIgnoreCase("Location")) {
                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                histroyMsg.setIsSender(4);
                            } else {
                                histroyMsg.setIsSender(5);
                            }
                        } else if (msgData.getString("content_type").equalsIgnoreCase("Contact")) {
                            if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                histroyMsg.setIsSender(6);
                            } else {
                                histroyMsg.setIsSender(7);
                            }
                        } else if (msgData.has("group_action")) {
                            histroyMsg.setIsSender(3);
                        } else {
                            if (msgData.has("sender_id"))
                                if (profileManager.getProfileInfo("", false).get(0).getID().equalsIgnoreCase(msgData.getString("sender_id"))) {
                                    histroyMsg.setIsSender(0);
                                } else {
                                    histroyMsg.setIsSender(1);
                                }
                        }
                        int c = gpDatabase.getConUreadCount(histroyCon.getConId());
                        histroyCon.setUnReadCount(++c);
                        gpDatabase.addConversation(histroyCon);
                        gpDatabase.addMessage(histroyMsg);
                    }
                }
                EventBus.getDefault().post("ChatHistory True");
            } catch (Exception e) {
                EventBus.getDefault().post("ChatHistory False");
                e.printStackTrace();
            }*/
        }

        public void errorCallback(String channel, PubnubError error) {
            System.out.println(error.toString() + "... channel=>errorCallback" + channel);
        }
    };

    public void sendNotification(String messagei) {
        //pass data to service
        android.os.Message msg = new android.os.Message();
        Bundle data = new Bundle();
        data.putString("msgText", messagei);
        //data.putString("chatID", chatId);
        msg.setData(data);
     //   msg.what = Constants.NOTIFICATION;
        serviceHandler.sendMessage(msg);
    }


}

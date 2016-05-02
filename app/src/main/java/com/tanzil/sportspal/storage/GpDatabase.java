package com.tanzil.sportspal.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.model.bean.SpConversation;
import com.tanzil.sportspal.model.bean.SpMessage;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by mukesh on 20/8/15.
 */
public class GpDatabase extends SQLiteOpenHelper implements ConverSationI, MessageI {

    // The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.sourcefuse.gameplan/databases/";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "GpAppData.sqlite";
    public static SQLiteDatabase dbObj;
    private static int INITIALIZE_COUNTER = 0;
    private final Context mCtx;
    private String packageName;


    private static String MESSAGE_COUNT_QUERY = "SELECT COUNT (*) FROM " + GpDataBaseContract.Message.TABLE_NAME + " WHERE " + GpDataBaseContract.Message.CONVERSTION_ID + "=?";
    private static GpDatabase sInstance;

    public static synchronized GpDatabase getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new GpDatabase(context.getApplicationContext());
        }
        return sInstance;
    }


    public GpDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.packageName = this.mCtx.getPackageName();
        DB_PATH = "/data/data/" + this.packageName + "/databases/";
        try {
            db.execSQL(GpDataBaseContract.Conversation.CREATE_TABLE);
            db.execSQL(GpDataBaseContract.Message.CREATE_TABLE);
            SPLog.e("onCreate:", "Created");
        } catch (Exception ex) {
            SPLog.e("Exception:", ex.toString());
        }
    }

    public synchronized SQLiteDatabase openDataBase() throws SQLException {
        // Open the database
        if (dbObj == null) {
            if (getWritableDatabase() != null) {
                dbObj = getWritableDatabase();
                String myPath = DB_PATH + DATABASE_NAME;
                dbObj = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.NO_LOCALIZED_COLLATORS
                                | SQLiteDatabase.OPEN_READWRITE);
            }
        }
        return dbObj;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public ArrayList<SpConversation> getAllConversations(String user_id) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        ArrayList<SpConversation> conversationList = new ArrayList<SpConversation>();
        SpConversation conBean;
        Cursor gPlanCursor = dbObj.query(GpDataBaseContract.Conversation.TABLE_NAME, null, GpDataBaseContract.Conversation.CONVERSATION_USER_ID + " = ? ", new String[]{user_id}, null, null, GpDataBaseContract.Conversation.CONVERSATION_TIME + " DESC");
        if (gPlanCursor.moveToFirst()) {
            do {
                conBean = new SpConversation();
               /* conBean.setConId(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_ID)));
                conBean.setConType(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_TYPE)));
                conBean.setConName(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_NAME)));
                conBean.setConImage(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_IMAGE)));
                conBean.setConMsg(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_MGS)));
                conBean.setConTime(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_TIME)));
                conBean.setConLastUser(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_LAST_USER)));
                conBean.setConMatchTime(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_MATCH_TIME)));
                conBean.setGroupAction(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_ACTION)));
                conBean.setGroupAdmin(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_ADMIN)));
                conBean.setUser_id(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.CONVERSATION_USER_ID)));
                conBean.setUnReadCount(gPlanCursor.getInt(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.UNREAD_COUNT)));*/
                conversationList.add(conBean);
            }
            while (gPlanCursor.moveToNext());
        }
        return conversationList;
    }

    @Override
    public void deleteAllConversations() {

        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        dbObj.delete(GpDataBaseContract.Conversation.TABLE_NAME, null, null);

    }

    @Override
    public void deleteConversations(String conversationId) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        dbObj.delete(GpDataBaseContract.Conversation.TABLE_NAME,
                GpDataBaseContract.Conversation.CONVERSATION_ID + " = ?", new String[]{conversationId});
    }


    @Override
    public int setConversation(ArrayList<SpConversation> conversation) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        int index = INITIALIZE_COUNTER;
        while (index < conversation.size()) {
            try {
                SpConversation con = conversation.get(index);
               /* if (!hasConversation(con.getConId(), con.getUser_id())) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ID, con.getConId());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_TYPE, con.getConType());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_NAME, con.getConName());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_IMAGE, con.getConImage());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_MGS, con.getConMsg());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_TIME, con.getConTime());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_LAST_USER, con.getConLastUser());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_MATCH_TIME, con.getConMatchTime());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ACTION, con.getGroupAction());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ADMIN, con.getGroupAdmin());
                    contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_USER_ID, con.getUser_id());
                    contentValues.put(GpDataBaseContract.Conversation.UNREAD_COUNT, con.getUnReadCount());
                    dbObj.insert(GpDataBaseContract.Conversation.TABLE_NAME, null, contentValues);
                } else {
                    updateConversation(con);
                }*/
            } catch (Exception sqlException) {
                SPLog.e("GP DATABASE SQL", sqlException.getMessage().toString());
                sqlException.printStackTrace();
                return -1;
            }
            index++;
        }
        return index;
    }


    public int addConversation(SpConversation con) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();
      /*  try {
            if (!hasConversation(con.getConId(), con.getUser_id())) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ID, con.getConId());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_TYPE, con.getConType());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_NAME, con.getConName());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_IMAGE, con.getConImage());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_MGS, con.getConMsg());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_TIME, con.getConTime());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_LAST_USER, con.getConLastUser());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_MATCH_TIME, con.getConMatchTime());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ACTION, con.getGroupAction());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ADMIN, con.getGroupAdmin());
                contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_USER_ID, con.getUser_id());
                contentValues.put(GpDataBaseContract.Conversation.UNREAD_COUNT, con.getUnReadCount());
                dbObj.insert(GpDataBaseContract.Conversation.TABLE_NAME, null, contentValues);
            } else {
                updateConversation(con);
            }
        } catch (Exception exception) {
            SPLog.e("GP DATABASE SQL", exception.getMessage().toString());
            exception.printStackTrace();
            return -1;
        }
*/
        return 1;
    }


    public boolean hasConversation(String conversationId, String conOwnerId) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        Cursor conversatioCur = dbObj.query(GpDataBaseContract.Conversation.TABLE_NAME, null, GpDataBaseContract.Conversation.CONVERSATION_ID + " = ? and " + GpDataBaseContract.Conversation.CONVERSATION_USER_ID + " = ?", new String[]{conversationId, conOwnerId}, null, null, null);
        conversatioCur.moveToFirst();
        boolean result;
        result = conversatioCur.getCount() > 0;
        SPLog.e("jcdnndncdnkcnresult", "result--->" + result);
        return result;
    }


    public int getConUreadCount(String conId) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        int count = 0;
        Cursor gPlanCursor;
        try {
            gPlanCursor = dbObj.query(false, GpDataBaseContract.Conversation.TABLE_NAME, new String[]{GpDataBaseContract.Conversation.UNREAD_COUNT}, GpDataBaseContract.Conversation.CONVERSATION_ID + "=?", new String[]{conId}, null, null, null, null);//" ASC,DESC"
            gPlanCursor.moveToFirst();
            count = gPlanCursor.getInt(gPlanCursor.getColumnIndex(GpDataBaseContract.Conversation.UNREAD_COUNT));
        } catch (Exception sqlException) {
            SPLog.e("SQLException", sqlException.getMessage().toString());
            sqlException.printStackTrace();
            return -1;
        }
        SPLog.e("UNreadcountcountcountcountcount", "" + count);
        return count;
    }


    public int addNewConversation(SpConversation con) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();
        try {
            ContentValues contentValues = new ContentValues();
           /* contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ID, con.getConId());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_TYPE, con.getConType());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_NAME, con.getConName());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_IMAGE, con.getConImage());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_MGS, con.getConMsg());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_TIME, con.getConTime());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_LAST_USER, con.getConLastUser());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_MATCH_TIME, con.getConMatchTime());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ACTION, con.getGroupAction());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_ADMIN, con.getGroupAdmin());
            contentValues.put(GpDataBaseContract.Conversation.CONVERSATION_USER_ID, con.getUser_id());
            contentValues.put(GpDataBaseContract.Conversation.UNREAD_COUNT, con.getUnReadCount());*/
            dbObj.insert(GpDataBaseContract.Conversation.TABLE_NAME, null, contentValues);

        } catch (Exception exception) {
            SPLog.e("GP DATABASE SQL", exception.getMessage().toString());
            exception.printStackTrace();
            return -1;
        }

        return 1;
    }


    @Override
    public int updateConversation(SpConversation ConversationBean) {
        // TODO Auto-generated method stub
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();
        try {
          /*  ConversationI cnvrsastion = ConversationBean;
            // if (hasConversation(cnvrsastion.getConId())) {
            ContentValues conversationData = new ContentValues();
            if (cnvrsastion.getConId() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_ID, cnvrsastion.getConId());

            if (cnvrsastion.getConType() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_TYPE, cnvrsastion.getConType());

            if (cnvrsastion.getConName() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_NAME, cnvrsastion.getConName());

            if (cnvrsastion.getConImage() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_IMAGE, cnvrsastion.getConImage());

            if (cnvrsastion.getConMsg() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_MGS, cnvrsastion.getConMsg());

            if (cnvrsastion.getConTime() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_TIME, cnvrsastion.getConTime());

            if (cnvrsastion.getConLastUser() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_LAST_USER, cnvrsastion.getConLastUser());

            if (cnvrsastion.getConMatchTime() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_MATCH_TIME, cnvrsastion.getConMatchTime());

            if (cnvrsastion.getGroupAction() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_ACTION, cnvrsastion.getGroupAction());

            if (cnvrsastion.getGroupAdmin() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_ADMIN, cnvrsastion.getGroupAdmin());

            if (cnvrsastion.getUser_id() != null)
                conversationData.put(GpDataBaseContract.Conversation.CONVERSATION_USER_ID, cnvrsastion.getUser_id());

            // if (cnvrsastion.getUnReadCount() != 0)
            conversationData.put(GpDataBaseContract.Conversation.UNREAD_COUNT, cnvrsastion.getUnReadCount());

            dbObj.update(GpDataBaseContract.Conversation.TABLE_NAME, conversationData, GpDataBaseContract.Conversation.CONVERSATION_ID + " =? ", new String[]{cnvrsastion.getConId()});
            //  }*/
        } catch (Exception sqlException) {
            SPLog.e("SQLException", sqlException.getMessage().toString());
            sqlException.printStackTrace();
            return -1;
        }
        SPLog.e("Database updated", "updated");
        return 1;

    }


    @Override
    public ArrayList<SpMessage> getAllMessage(String cnvrId, String lStart, String lEnd, String matchChatType) {

      /*  if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        ArrayList<com.sourcefuse.gameplan.model.MessageI> messageList = new ArrayList<com.sourcefuse.gameplan.model.MessageI>();
        com.sourcefuse.gameplan.model.MessageI messageBean;
        String limit = lStart + "," + lEnd;
        Cursor gPlanCursor = null;

        if (GpUtil.isEmptyString(matchChatType)) {
            gPlanCursor = dbObj.query(false, GpDataBaseContract.Message.TABLE_NAME, null, GpDataBaseContract.Message.CONVERSTION_ID + "=?", new String[]{cnvrId}, null, null, GpDataBaseContract.Message.MESSAGE_TIME + " ASC", null);//" ASC,DESC"
        } else {
            //  gPlanCursor = dbObj.query(false, GpDataBaseContract.Message.TABLE_NAME, null, GpDataBaseContract.Message.CONVERSTION_ID + "=?", new String[]{cnvrId}, null, null, GpDataBaseContract.Message.MESSAGE_TIME + " ASC", null);//" ASC"
            try {
                gPlanCursor = dbObj.query(false, GpDataBaseContract.Message.TABLE_NAME, null, GpDataBaseContract.Message.CONVERSTION_ID + "=? and " + GpDataBaseContract.Message.MATCH_CHAT_TYPE + "=?", new String[]{cnvrId, matchChatType}, null, null, GpDataBaseContract.Message.MESSAGE_TIME + " ASC", null);//" ASC"
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

       /* if (gPlanCursor.moveToFirst()) {
            do {
               *//* messageBean = new com.sourcefuse.gameplan.model.MessageI();
                messageBean.setMessageID(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MESSAGE_ID)));
                messageBean.setCnvrId(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.CONVERSTION_ID)));
                messageBean.setMessageType(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MESSAGE_TYPE)));
                messageBean.setIsSender(gPlanCursor.getInt(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.IS_SENDER)));
                messageBean.setMessageText(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MESSAGE_TEXT)));
                messageBean.setMessageTime(gPlanCursor.getInt(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MESSAGE_TIME)));
                messageBean.setPlayPosition(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.PLAY_POSITION)));
                messageBean.setPlayerTeam(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.PLAYER_TEAM)));
                messageBean.setChatImage(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.CHAT_IMAGE)));
                messageBean.setMsgUserId(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MSG_USR_ID)));
                messageBean.setMsgUserName(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MSG_USR_NAME)));
                messageBean.setMsgUserImage(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MSG_USR_IMG)));
                messageBean.setSharedLat(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.SHAREED_LAT)));
                messageBean.setSharedLng(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.SHAREED_LNG)));
                messageBean.setShareConName(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.SHAREED_CON_MAME)));
                messageBean.setShareConNumber(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.SHAREED_CON_NO)));
                messageBean.setMatchChatType(gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MATCH_CHAT_TYPE)));

                GpLog.e(">>>>>>>", "---------" + gPlanCursor.getInt(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MESSAGE_TIME)) + " ************* " + gPlanCursor.getString(gPlanCursor.getColumnIndex(GpDataBaseContract.Message.MESSAGE_TEXT)));*//*
                //messageList.add(messageBean);
            }
            while (gPlanCursor.moveToNext());
        }*/
        return null;
    }

    @Override
    public void deleteMessages(String conversationId) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        dbObj.delete(GpDataBaseContract.Message.TABLE_NAME, GpDataBaseContract.Message.CONVERSTION_ID + " = ?", new String[]{conversationId});
        dbObj.delete(GpDataBaseContract.Conversation.TABLE_NAME, GpDataBaseContract.Conversation.CONVERSATION_ID + " = ?", new String[]{conversationId});
    }

    @Override
    public void deleteAllMessages() {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        dbObj.delete(GpDataBaseContract.Message.TABLE_NAME, null, null);
    }


    public boolean hasMessage(String messageId) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();
        boolean result;
        SPLog.e("messageId", "messageId---hasMessage-->" + messageId);
        Cursor messageCur = dbObj.query(GpDataBaseContract.Message.TABLE_NAME, null, GpDataBaseContract.Message.MESSAGE_ID + " = ?", new String[]{messageId}, null, null, null);
        if (messageCur != null) {
            messageCur.moveToFirst();

            result = messageCur.getCount() > 0;
        } else {
            result = false;
        }
        return result;
    }


    @Override
    public int setAllMessage(ArrayList<SpMessage> messageBean) {
       /* if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        int index = INITIALIZE_COUNTER;
        while (index < messageBean.size()) {
            try {
                com.sourcefuse.gameplan.model.MessageI msg = messageBean.get(index);

                if (!GpUtil.isEmptyString(msg.getMessageID()))
                    if (!hasMessage(msg.getMessageID())) {

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(GpDataBaseContract.Message.MESSAGE_ID, msg.getMessageID());
                        contentValues.put(GpDataBaseContract.Message.CONVERSTION_ID, msg.getCnvrId());
                        contentValues.put(GpDataBaseContract.Message.MESSAGE_TYPE, msg.getMessageType());
                        contentValues.put(GpDataBaseContract.Message.IS_SENDER, msg.getIsSender());
                        contentValues.put(GpDataBaseContract.Message.MESSAGE_TEXT, msg.getMessageText());
                        contentValues.put(GpDataBaseContract.Message.MESSAGE_TIME, msg.getMessageTime());
                        contentValues.put(GpDataBaseContract.Message.PLAY_POSITION, msg.getPlayPosition());
                        contentValues.put(GpDataBaseContract.Message.PLAYER_TEAM, msg.getPlayerTeam());
                        contentValues.put(GpDataBaseContract.Message.CHAT_IMAGE, msg.getChatImage());
                        contentValues.put(GpDataBaseContract.Message.MSG_USR_ID, msg.getMsgUserId());
                        contentValues.put(GpDataBaseContract.Message.MSG_USR_NAME, msg.getMsgUserName());
                        contentValues.put(GpDataBaseContract.Message.MSG_USR_IMG, msg.getMsgUserImage());
                        contentValues.put(GpDataBaseContract.Message.SHAREED_LAT, msg.getSharedLat());
                        contentValues.put(GpDataBaseContract.Message.SHAREED_LNG, msg.getSharedLng());
                        contentValues.put(GpDataBaseContract.Message.SHAREED_CON_MAME, msg.getShareConName());
                        contentValues.put(GpDataBaseContract.Message.SHAREED_CON_NO, msg.getShareConNumber());
                        contentValues.put(GpDataBaseContract.Message.MATCH_CHAT_TYPE, msg.getMatchChatType());

                        dbObj.insert(GpDataBaseContract.Message.TABLE_NAME, null, contentValues);
                    }
            } catch (Exception sqlException) {
                GpLog.e("GP DATABASE SQL", sqlException.getMessage().toString());
                sqlException.printStackTrace();
                return -1;
            }
            index++;
        }*/
       // return index;
        return 0;
    }


  /*  public int addMessage(com.sourcefuse.gameplan.model.MessageI msg) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        try {
            *//*if (!GpUtil.isEmptyString(msg.getMessageID()))
                if (!hasMessage(msg.getMessageID())) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GpDataBaseContract.Message.MESSAGE_ID, msg.getMessageID());
                    contentValues.put(GpDataBaseContract.Message.CONVERSTION_ID, msg.getCnvrId());
                    contentValues.put(GpDataBaseContract.Message.MESSAGE_TYPE, msg.getMessageType());
                    contentValues.put(GpDataBaseContract.Message.IS_SENDER, msg.getIsSender());
                    contentValues.put(GpDataBaseContract.Message.MESSAGE_TEXT, msg.getMessageText());
                    contentValues.put(GpDataBaseContract.Message.MESSAGE_TIME, msg.getMessageTime());
                    contentValues.put(GpDataBaseContract.Message.PLAY_POSITION, msg.getPlayPosition());
                    contentValues.put(GpDataBaseContract.Message.PLAYER_TEAM, msg.getPlayerTeam());
                    contentValues.put(GpDataBaseContract.Message.CHAT_IMAGE, msg.getChatImage());
                    contentValues.put(GpDataBaseContract.Message.MSG_USR_ID, msg.getMsgUserId());
                    contentValues.put(GpDataBaseContract.Message.MSG_USR_NAME, msg.getMsgUserName());
                    contentValues.put(GpDataBaseContract.Message.MSG_USR_IMG, msg.getMsgUserImage());
                    contentValues.put(GpDataBaseContract.Message.SHAREED_LAT, msg.getSharedLat());
                    contentValues.put(GpDataBaseContract.Message.SHAREED_LNG, msg.getSharedLng());
                    contentValues.put(GpDataBaseContract.Message.SHAREED_CON_MAME, msg.getShareConName());
                    contentValues.put(GpDataBaseContract.Message.SHAREED_CON_NO, msg.getShareConNumber());
                    contentValues.put(GpDataBaseContract.Message.MATCH_CHAT_TYPE, msg.getMatchChatType());

                    dbObj.insert(GpDataBaseContract.Message.TABLE_NAME, null, contentValues);
                }*//*
        } catch (Exception sqlException) {
            SPLog.e("GP DATABASE SQL", sqlException.getMessage().toString());
            sqlException.printStackTrace();
            return -1;
        }

        return 1;
    }*/


 /*   public int updateMessage(com.sourcefuse.gameplan.model.MessageI msgBean) {//
        // TODO Auto-generated method stub
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();
      *//*  try {
            ContentValues updateMessageData = new ContentValues();
            if (msgBean.getChatImage() != null)
                updateMessageData.put(GpDataBaseContract.Message.CHAT_IMAGE, msgBean.getChatImage());
            if (msgBean.getMessageTime() != null)
                updateMessageData.put(GpDataBaseContract.Message.MESSAGE_TIME, msgBean.getMessageTime());
            int d = dbObj.update(GpDataBaseContract.Message.TABLE_NAME, updateMessageData, GpDataBaseContract.Message.MESSAGE_ID + "=?", new String[]{String.valueOf(msgBean.getMessageID())});
        } catch (Exception sqlException) {
            SPLog.e("SQLException", sqlException.getMessage().toString());
            sqlException.printStackTrace();
            return -1;
        }*//*
        return 1;
    }

*/
 /*   public int addConversationFromNotification(ConversationI con, com.sourcefuse.gameplan.model.MessageI newMessage) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();
        try {
            if (!hasConversation(con.getConId(), con.getUser_id())) {
                updateConversation(con);
                addMessage(newMessage);
            } else {
                addNewConversation(con);
                addMessage(newMessage);
            }
        } catch (Exception exception) {
            SPLog.e("GP DATABASE SQL", exception.getMessage().toString());
            exception.printStackTrace();
            return -1;
        }

        return 1;
    }*/


    public int noOfRows(String conversationId) {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        Cursor rowsCur = dbObj.rawQuery(MESSAGE_COUNT_QUERY, new String[]{conversationId});
        rowsCur.moveToFirst();
        int count = rowsCur.getInt(0);
        rowsCur.close();

        return count;
    }


    public void deleteDb() {
        if (dbObj == null || !dbObj.isOpen())
            dbObj = null;
    }


    public void dropDatabase() {
        if (dbObj == null || !dbObj.isOpen())
            openDataBase();

        dbObj.execSQL(" DROP TABLE " + GpDataBaseContract.Conversation.TABLE_NAME);

        dbObj.execSQL(" DROP TABLE " + GpDataBaseContract.Message.TABLE_NAME);

        dbObj = null;
    }


}

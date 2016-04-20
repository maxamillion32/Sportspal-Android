package com.tanzil.sportspal.storage;

import android.provider.BaseColumns;

/**
 * Created by mukesh on 25/3/15.
 */
public class GpDataBaseContract {


    private static final String NUMERIC_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE = " DOUBLE";
    private static final String BOOL_TYPE = " BOOL";
    private static final String LONG_TYPE = " LONG";
    private static final String TEXT_TYPE = " TEXT";
    private static final String VARCHAR_TYPE = " VARCHAR";

    private static final String COMMA_SEP = ",";

    public static abstract class Conversation implements BaseColumns {
        public static final String TABLE_NAME = "conversation";

        public static final String CONVERSATION_ID = "conversationId";
        public static final String CONVERSATION_TYPE = "conversationType";
        public static final String CONVERSATION_NAME = "conversationName";
        public static final String CONVERSATION_IMAGE = "conversationImage";
        public static final String CONVERSATION_MGS = "conversationMsg";
        public static final String CONVERSATION_TIME = "conversationTime";
        public static final String CONVERSATION_LAST_USER = "conversationLastUser";
        public static final String CONVERSATION_MATCH_TIME = "conversationMatchTime";
        public static final String CONVERSATION_ACTION = "conversationAction";
        public static final String CONVERSATION_ADMIN = "conversationAdmin";
        public static final String CONVERSATION_USER_ID = "conversationUserId";
        public static final String UNREAD_COUNT = "conunRead";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY ," +
                CONVERSATION_ID + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_TYPE + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_NAME + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_IMAGE + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_MGS + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_TIME + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_LAST_USER + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_MATCH_TIME + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_ACTION + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_ADMIN + VARCHAR_TYPE + COMMA_SEP +
                CONVERSATION_USER_ID + VARCHAR_TYPE +COMMA_SEP +
                UNREAD_COUNT + NUMERIC_TYPE +

                " )";
    }


    public static abstract class Message implements BaseColumns {
        public static final String TABLE_NAME = "message";


        public static final String MESSAGE_ID = "messageID";
        public static final String CONVERSTION_ID = "conversionid";
        public static final String MESSAGE_TYPE = "messageType";
        public static final String IS_SENDER = "isSender";
        public static final String MESSAGE_TEXT = "messageText";
        public static final String MESSAGE_TIME = "messageTime";
        public static final String PLAY_POSITION = "playPosition";
        public static final String PLAYER_TEAM = "playerTeam";
        public static final String CHAT_IMAGE = "chatImage";
        public static final String MSG_USR_ID = "msgUserId";
        public static final String MSG_USR_NAME = "msgUserName";
        public static final String MSG_USR_IMG = "msgUserImage";
        public static final String SHAREED_LAT = "sharedLat";
        public static final String SHAREED_LNG = "sharedLng";
        public static final String SHAREED_CON_MAME = "shareConName";
        public static final String SHAREED_CON_NO = "shareConNumber";
        public static final String MATCH_CHAT_TYPE = "matchChatType";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MESSAGE_ID + VARCHAR_TYPE + COMMA_SEP +
                CONVERSTION_ID + VARCHAR_TYPE + COMMA_SEP +
                MESSAGE_TYPE + VARCHAR_TYPE + COMMA_SEP +
                IS_SENDER + NUMERIC_TYPE + COMMA_SEP +
                MESSAGE_TEXT + VARCHAR_TYPE + COMMA_SEP +
                MESSAGE_TIME + LONG_TYPE + COMMA_SEP +
                PLAY_POSITION + VARCHAR_TYPE + COMMA_SEP +

                PLAYER_TEAM + VARCHAR_TYPE + COMMA_SEP +
                CHAT_IMAGE + VARCHAR_TYPE + COMMA_SEP +
                MSG_USR_ID + VARCHAR_TYPE + COMMA_SEP +
                MSG_USR_NAME + VARCHAR_TYPE + COMMA_SEP +
                MSG_USR_IMG + VARCHAR_TYPE + COMMA_SEP +
                SHAREED_LAT + VARCHAR_TYPE + COMMA_SEP +
                SHAREED_LNG + VARCHAR_TYPE + COMMA_SEP +
                SHAREED_CON_MAME + VARCHAR_TYPE + COMMA_SEP +
                SHAREED_CON_NO + VARCHAR_TYPE + COMMA_SEP +
                MATCH_CHAT_TYPE + VARCHAR_TYPE +

                " )";
    }
}

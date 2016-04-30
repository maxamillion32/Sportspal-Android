package com.tanzil.sportspal.storage;

import com.tanzil.sportspal.model.bean.SpMessage;

import java.util.ArrayList;


/**
 * Created by mukesh on 20/8/15.
 */
public interface MessageI {
    ArrayList<SpMessage> getAllMessage(String conversationId, String lStart, String lEnd, String matchChatType);
    void deleteMessages(String conversationId);
    void deleteAllMessages();
    int setAllMessage(ArrayList<SpMessage> messageBean);


}

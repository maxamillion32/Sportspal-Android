package com.tanzil.sportspal.storage;

import com.tanzil.sportspal.model.bean.SpConversation;

import java.util.ArrayList;

/**
 * Created by mukesh on 20/8/15.
 */
public interface ConverSationI {

    ArrayList<SpConversation> getAllConversations(String user_id);
    void deleteAllConversations();
    void deleteConversations(String whisperId);
    int setConversation(ArrayList<SpConversation> conversation);
    int updateConversation(SpConversation ConversationBean);

}

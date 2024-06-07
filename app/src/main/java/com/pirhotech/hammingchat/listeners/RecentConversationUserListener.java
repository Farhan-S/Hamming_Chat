package com.pirhotech.hammingchat.listeners;

import com.pirhotech.hammingchat.models.User;

public interface RecentConversationUserListener {
    void onUserClicked(User user);
}

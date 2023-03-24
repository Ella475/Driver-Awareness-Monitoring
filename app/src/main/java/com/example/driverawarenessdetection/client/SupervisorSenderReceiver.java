package com.example.driverawarenessdetection.client;

import com.example.driverawarenessdetection.login.data.LoginRepository;

import java.util.ArrayList;
import java.util.List;

public class SupervisorSenderReceiver {
    private final Client client;
    private final String userId;
    private List<String> supervisedUsersIds;
    private List<String> supervisedUsersUsernames;

    public SupervisorSenderReceiver() {
        client = Client.getInstance();
        userId = LoginRepository.getInstance(null).getLoggedInUser().getUserId();
    }

    public void fetchSupervisedUsers() {
        List<List<String>> result = client.getSupervisedUsers(userId);
        if (result.isEmpty()) {
            supervisedUsersIds = new ArrayList<>();
            supervisedUsersUsernames = new ArrayList<>();
            return;
        }
        supervisedUsersIds = result.get(0);
        supervisedUsersUsernames = result.get(1);
    }

    public List<String> getSupervisedUsersIds() {
        return supervisedUsersIds;
    }

    public List<String> getSupervisedUsersUsernames() {
        return supervisedUsersUsernames;
    }

    public void addSupervisedUser(String username) {
        client.setSupervisedUser(userId, username);
    }

    public boolean supervisedUserExists(String supervisedUserId) {
        return client.checkUserExists(supervisedUserId, true);
    }
}

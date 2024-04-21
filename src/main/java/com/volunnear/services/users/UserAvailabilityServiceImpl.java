package com.volunnear.services.users;

import com.volunnear.services.interfaces.UserAvailabilityService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Service
public class UserAvailabilityServiceImpl implements UserAvailabilityService {
    @Override
    public void addOnlineUser(Principal username) {

    }

    @Override
    public void removeOnlineUser(Principal username) {

    }

    @Override
    public boolean isUserOnline(UUID userId) {
        return false;
    }

    @Override
    public void addUserSubscribed(Principal user, String subscribedChannel) {

    }

    @Override
    public void removeUserSubscribed(Principal user, String subscribedChannel) {

    }

    @Override
    public Map<String, Set<String>> getUserSubscribed() {
        return null;
    }
}

package com.volunnear.services.interfaces;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface UserAvailabilityService {

    void addOnlineUser(Principal username);

    void removeOnlineUser(Principal username);

    boolean isUserOnline(UUID userId);

    void addUserSubscribed(Principal user, String subscribedChannel);

    void removeUserSubscribed(Principal user, String subscribedChannel);

    Map<String, Set<String>> getUserSubscribed();
}

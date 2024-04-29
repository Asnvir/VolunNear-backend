package com.volunnear.services.users;

import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.repositories.users.UserRepository;
import com.volunnear.services.interfaces.UserAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Service
@Slf4j
public class UserAvailabilityServiceImpl implements UserAvailabilityService {
    private final Set<UUID> onlineUsers;
    private final Map<UUID, Set<String>> userSubscribed;

    private final UserRepository userRepository;

    private final SimpMessageSendingOperations simpMessageSendingOperations;


    public UserAvailabilityServiceImpl(UserRepository userRepository, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.userRepository = userRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.onlineUsers = new ConcurrentSkipListSet<>();
        this.userSubscribed = new ConcurrentHashMap<>();
    }
    @Override
    public void addOnlineUser(Principal user) {
        if (user == null) return;
        CustomUserDetails userDetails= getUserDetails(user);
        log.info("User {} is online", userDetails.getUsername());
        //TODO: notify that user is online for client rendering
        onlineUsers.add(userDetails.getId());
    }

    @Override
    public void removeOnlineUser(Principal user) {
        if (user == null) return;
        CustomUserDetails userDetails= getUserDetails(user);
        log.info("User {} is offline", userDetails.getUsername());
        onlineUsers.remove(userDetails.getId());
        //TODO: notify that user is online for client rendering
    }

    @Override
    public boolean isUserOnline(UUID userId) {
        return onlineUsers.contains(userId);
    }

    @Override
    public void addUserSubscribed(Principal user, String subscribedChannel) {
        CustomUserDetails userDetails= getUserDetails(user);
        log.info("User {} subscribed to channel {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscribedChannels = userSubscribed.getOrDefault(userDetails.getId(), new HashSet<>());
        subscribedChannels.add(subscribedChannel);
        userSubscribed.put(userDetails.getId(), subscribedChannels);
    }

    @Override
    public void removeUserSubscribed(Principal user, String subscribedChannel) {
        CustomUserDetails userDetails= getUserDetails(user);
        log.info("User {} unsubscribed from channel {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscribedChannels = userSubscribed.getOrDefault(userDetails.getId(), new HashSet<>());
        subscribedChannels.remove(subscribedChannel);
        userSubscribed.put(userDetails.getId(), subscribedChannels);
    }

    @Override
    public boolean isUserSubscribed(UUID userId, String subscribedChannel) {
        return userSubscribed.getOrDefault(userId, new HashSet<>()).contains(subscribedChannel);
    }

    @Override
    public Map<UUID, Set<String>> getUserSubscribed() {
        return null;
    }


    private CustomUserDetails getUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        Object object = user.getPrincipal();
        return (CustomUserDetails) object;
    }
}

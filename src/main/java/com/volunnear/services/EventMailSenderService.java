package com.volunnear.services;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.VolunteerNotificationSubscription;
import com.volunnear.eventListeners.ActivityCreationEvent;
import com.volunnear.repositories.VolunteerNotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMailSenderService {
    private final JavaMailSender javaMailSender;
    private final VolunteerNotificationSubscriptionRepository subscriptionRepository;

    @Async
    @EventListener(ActivityCreationEvent.class)
    public void sendNotificationForSubscribers(ActivityCreationEvent activityCreationEvent) {
        ActivityNotificationDTO notificationDTO = activityCreationEvent.getNotificationDTO();
        List<VolunteerNotificationSubscription> subscriptions = subscriptionRepository.findAllByUserOrganisationId(notificationDTO.getOrganisationResponseDTO().getId());
        Map<String, String> usernameAndEmailMap = subscriptions.stream().collect(Collectors.toMap(
                volunteerNotificationSubscription -> volunteerNotificationSubscription.getUserVolunteer().getUsername(),
                volunteerNotificationSubscription -> volunteerNotificationSubscription.getUserVolunteer().getEmail(),
                (existingEmail, newEmail) -> existingEmail
        ));
        ActivityDTO activityDTO = notificationDTO.getActivityDTO();
        for (Map.Entry<String, String> usernameAndEmailMapEntry : usernameAndEmailMap.entrySet()) {
            String email = usernameAndEmailMapEntry.getValue();
            if (validateEmail(email)) {
                SimpleMailMessage mailMessage = getSimpleMailMessage(notificationDTO, activityDTO);
                mailMessage.setTo(email);
                javaMailSender.send(mailMessage);
            }
        }
    }

    private boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    private static SimpleMailMessage getSimpleMailMessage(ActivityNotificationDTO notificationDTO, ActivityDTO activityDTO) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("New activity from " + notificationDTO.getOrganisationResponseDTO().getNameOfOrganisation() + " !");
        mailMessage.setText("There is info about activity: "
                + "\nTitle: " + activityDTO.getTitle()
                + "\nKind of activity: " + activityDTO.getKindOfActivity()
                + "\nDescription: " + activityDTO.getDescription()
                + "\nCountry and city: " + activityDTO.getCountry() + "/" + activityDTO.getCity()
                + "\nDate of place: " + activityDTO.getDateOfPlace());
        mailMessage.setFrom("volunNearAppTeam@gmail.com");
        return mailMessage;
    }
}

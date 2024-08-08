package com.volunnear.services.notifications;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.entitiy.VolunteerNotificationSubscription;
import com.volunnear.events.ActivityCreationEvent;
import com.volunnear.repositories.VolunteerNotificationSubscriptionRepository;
import com.volunnear.services.interfaces.EventMailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMailSenderServiceImpl implements EventMailSenderService {
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
                try {
                    sendHtmlEmail(email, activityCreationEvent.getStatus(), notificationDTO, activityDTO);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    private void sendHtmlEmail(String toEmail, String status, ActivityNotificationDTO notificationDTO, ActivityDTO activityDTO) throws MessagingException {
        String fromEmail = "volunNearAppTeam@gmail.com";
        String subject = status + " activity from " + notificationDTO.getOrganisationResponseDTO().getNameOfOrganisation() + " !";

        String body = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;'>"
                + "<h2 style='color: #333;'>Activity Information</h2>"
                + "<p><strong>Title:</strong> " + activityDTO.getTitle() + "</p>"
                + "<p><strong>Kind of activity:</strong> " + activityDTO.getKindOfActivity() + "</p>"
                + "<p><strong>Description:</strong> " + activityDTO.getDescription() + "</p>"
                + "<p><strong>Country and city:</strong> " + activityDTO.getCountry() + "/" + activityDTO.getCity() + "</p>"
                + "<p><strong>Date of place:</strong> " + activityDTO.getDateOfPlace() + "</p>"
                + "</div>"
                + "</body></html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}

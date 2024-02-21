package com.volunnear.controller;

import com.volunnear.dto.RegistrationRequest;
import com.volunnear.model.Credentials;
import com.volunnear.model.Volunteer;
import com.volunnear.sertvice.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @PostMapping(value = "/api/registration/volunteer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfVolunteer(@RequestBody RegistrationRequest registrationRequest) {
        String username = registrationRequest.getUsername();
        String password = registrationRequest.getPassword();
        String realName = registrationRequest.getRealName();
        registrationService.registerVolunteer(new Volunteer(realName, new Credentials(username, password)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

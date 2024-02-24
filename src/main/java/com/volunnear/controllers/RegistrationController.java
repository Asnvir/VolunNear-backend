package com.volunnear.controllers;

import com.volunnear.dtos.Credentials;
import com.volunnear.dtos.RegistrationRequestDTO;
import com.volunnear.dtos.VolunteerDTO;
import com.volunnear.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;


    @PostMapping(value = "/api/registration/volunteer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfVolunteer(@RequestBody RegistrationRequestDTO registrationRequestDto) {
        String username = registrationRequestDto.getUsername();
        String password = registrationRequestDto.getPassword();
        String realName = registrationRequestDto.getRealName();
        userService.registerVolunteer(new VolunteerDTO(realName, new Credentials(username, password)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

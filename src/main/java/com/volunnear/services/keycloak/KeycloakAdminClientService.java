package com.volunnear.services.keycloak;

import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAdminClientService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public String createUser(RegistrationOrganisationRequestDTO requestDTO) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(requestDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(requestDTO.getUsername());
        user.setEnabled(true);
        user.setEmail(requestDTO.getEmail());
        user.setRealmRoles(Collections.singletonList("ORGANISATION"));
        user.setCredentials(Collections.singletonList(credentialRepresentation));


        // Установка пароля
        CredentialRepresentation password = new CredentialRepresentation();
        password.setTemporary(false);
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(requestDTO.getPassword());

//        // Настройка атрибутов
//        Map<String, List<String>> attributes = new HashMap<>();
//        attributes.put("organisationName", Collections.singletonList(requestDTO.getNameOfOrganisation()));
//        attributes.put("country", Collections.singletonList(requestDTO.getCountry()));
//        attributes.put("city", Collections.singletonList(requestDTO.getCity()));
//        attributes.put("address", Collections.singletonList(requestDTO.getAddress()));
//        user.setAttributes(attributes);

        String userId = null;
        try (Response response = keycloak.realm(realm).users().create(user);) {
            if(response.getStatus() == 201) {
                System.out.println("Created");
                userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                System.out.println("User created with userId: " + userId);
            } else {
                System.out.println(response.getStatus());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        // Создание пользователя в Keycloak
//        Response response = keycloak.realm(realm).users().create(user);
//        if (response.getStatus() != 200) {
//            throw new RuntimeException("Failed to create user in Keycloak");
//        }


        return userId;
    }

    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
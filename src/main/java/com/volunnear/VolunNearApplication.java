package com.volunnear;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
@SecurityScheme(
        name = "Keycloak",
        openIdConnectUrl ="${keycloak.open-id-connect-url}",
        scheme = "bearer",
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER
)
@OpenAPIDefinition(info = @Info(title = "Endpoints library", version = "1.0", description = "Endpoints library of VolunNearApp API"))
public class VolunNearApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolunNearApplication.class, args);
    }
}

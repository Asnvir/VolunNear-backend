package com.volunnear.config;

//import com.volunnear.security.jwt.JwtTokenFilter; TODO

import com.volunnear.Routes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Stream;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(Routes.REGISTER_ROUTE_SECURITY + "/**",
                                Routes.LOGIN).permitAll()

                        .requestMatchers(Routes.SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("/test/check-status").hasRole("VOLUNTEER") //TODO: only for tests, delete in production

                        .requestMatchers(Routes.VOLUNTEER + "/**",
                                Routes.UPDATE_VOLUNTEER_PROFILE,
                                Routes.POST_FEEDBACK_ABOUT_ORGANISATION,
                                Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANISATION,
                                Routes.DELETE_FEEDBACK_ABOUT_ORGANISATION,
                                Routes.LOCATION + "/**",
                                Routes.NOTIFICATIONS + "/**").hasRole("VOLUNTEER")

                        .requestMatchers(Routes.UPDATE_ORGANISATION_PROFILE,
                                Routes.ADD_ACTIVITY,
                                Routes.GET_MY_ACTIVITIES,
                                Routes.UPDATE_ACTIVITY_INFORMATION,
                                Routes.DELETE_CURRENT_ACTIVITY_BY_ID,
                                Routes.GET_ORGANISATION_PROFILE,
                                Routes.ADD_COMMUNITY_LINK,
                                Routes.ADD_CHAT_LINK_FOR_ACTIVITY).hasRole("ORGANISATION")

                        .requestMatchers("/api/hello",
                                Routes.GET_ALL_ORGANISATIONS,
                                Routes.ACTIVITY_CURRENT_ORGANISATION,
                                Routes.GET_ALL_ACTIVITIES_WITH_ALL_ORGANISATIONS,
                                Routes.GET_FEEDBACKS_OF_ALL_ORGANISATIONS,
                                Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANISATION,
                                Routes.GET_CHAT_LINK_BY_ACTIVITY,
                                Routes.GET_COMMUNITY_LINK_BY_ORGANISATION)
                        .hasAnyRole("VOLUNTEER", "ORGANISATION")

                        .requestMatchers("/ws/**", "stomp").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .anyRequest().authenticated())
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(CsrfConfigurer::disable)
                .oauth2ResourceServer(configurer -> configurer.jwt(
                        jwt -> {
                    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                    jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);

                    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                    KeycloakJwtGrantedAuthoritiesConvertor customJwtGrantedAuthoritiesConverter = new KeycloakJwtGrantedAuthoritiesConvertor();

                    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(token ->
                            Stream
                                    .concat(
                                            jwtGrantedAuthoritiesConverter.convert(token).stream(),
                                            customJwtGrantedAuthoritiesConverter.convert(token).stream()
                                    ).toList());
                }))
                .build();
    }




    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

package com.volunnear.config.security;

import com.volunnear.Routes;
import com.volunnear.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${FRONTEND_HOST}")
    private String frontendHost;

    @Value("${FRONTEND_PORT}")
    private String frontendPort;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        String allowedOrigin = "http://" + frontendHost + ":" + frontendPort;
        config.addAllowedOrigin(allowedOrigin);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(Routes.REGISTER_ROUTE_SECURITY + "/**",
                                Routes.LOGIN).permitAll()

                        .requestMatchers(Routes.SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("/v3/api-docs", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()

                        .requestMatchers(Routes.VOLUNTEER + "/**",
                                Routes.UPDATE_VOLUNTEER_PROFILE,
                                Routes.POST_FEEDBACK_ABOUT_ORGANISATION,
                                Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANISATION,
                                Routes.DELETE_FEEDBACK_ABOUT_ORGANISATION,
                                Routes.GET_ACTIVITIES,
                                Routes.UPLOAD_VOLUNTEER_AVATAR,
                                Routes.GET_ALL_ACTIVITIES_OF_CURRENT_VOLUNTEER,
                                Routes.GET_RECOMMENDATION_BY_PREFERENCES,
                                Routes.SET_VOLUNTEERS_PREFERENCES,
                                Routes.IS_MY_ACTIVITY,
                                Routes.LOCATION + "/**",
                                Routes.NOTIFICATIONS + "/**").hasRole("VOLUNTEER")

                        .requestMatchers(Routes.UPDATE_ORGANISATION_PROFILE,
                                Routes.ADD_ACTIVITY,
                                Routes.UPDATE_ACTIVITY_INFORMATION,
                                Routes.UPLOAD_ORGANISATION_AVATAR,
                                Routes.UPLOAD_ACTIVITY_COVER_IMAGE,
                                Routes.UPLOAD_ACTIVITY_GALLERY_IMAGE,
                                Routes.UPLOAD_ACTIVITY_GALLERY_IMAGES,
                                Routes.DELETE_CURRENT_ACTIVITY_BY_ID,
                                Routes.GET_ORGANISATION_PROFILE,
                                Routes.ADD_COMMUNITY_LINK,
                                Routes.ADD_CHAT_LINK_FOR_ACTIVITY).hasRole("ORGANISATION")

                        .requestMatchers("/api/hello",
                                Routes.GET_ALL_ORGANISATIONS,
                                Routes.ACTIVITY_CURRENT_ORGANISATION,
                                Routes.GET_ALL_ACTIVITIES_NAMES,
                                Routes.GET_FEEDBACKS_OF_ALL_ORGANISATIONS,
                                Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANISATION,
                                Routes.GET_CHAT_LINK_BY_ACTIVITY,
                                Routes.GET_COMMUNITY_LINK_BY_ORGANISATION,
                                Routes.CHANGE_PASSWORD
                                )
                        .hasAnyRole("VOLUNTEER", "ORGANISATION")
                        .requestMatchers("/ws/**", "stomp").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter.class); // Добавляем JwtTokenFilter перед BasicAuthenticationFilter

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

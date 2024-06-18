package com.volunnear.config;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class KeycloakJwtGrantedAuthoritiesConvertor
        implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLES_PREFIX = "ROLE_";

    private final JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = delegate.convert(jwt);
        List<SimpleGrantedAuthority> extractedKeycloakRoles = extractKeycloakRoles(jwt);

        return Stream.of(grantedAuthorities, extractedKeycloakRoles)
                .filter(Objects::nonNull)
                .<GrantedAuthority>flatMap(Collection::stream)
                .toList();
    }

    private List<SimpleGrantedAuthority> extractKeycloakRoles(Jwt jwt) {
        LinkedTreeMap<String, List<String>> realmAccess = jwt.getClaim(REALM_ACCESS);
        if (CollectionUtils.isEmpty(realmAccess)) {
            return List.of();
        }

        List<String> roles = realmAccess.get(ROLES);
        if (CollectionUtils.isEmpty(roles)) {
            return List.of();
        }

        return roles.stream()
                .map(role -> ROLES_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}

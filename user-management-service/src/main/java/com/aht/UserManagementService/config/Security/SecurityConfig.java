package com.aht.UserManagementService.config.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Autowired
    CustomJWTDecoder customJWTDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, "api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/v1/user/registration").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/v1/user/ad/**").hasAuthority("SCOPE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "api/v1/user/ad/**").hasAuthority("SCOPE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "api/v1/user/ad/**").hasAuthority("SCOPE_ADMIN")
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJWTDecoder)));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
}

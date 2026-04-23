package com.backend.housing.infrastructure.config;

import com.backend.housing.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {

                    auth.requestMatchers("/api/auth/register").permitAll();
                    auth.requestMatchers("/api/auth/login").permitAll();
                    auth.requestMatchers("/api/auth/logout").permitAll();

                    auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();

                    auth.requestMatchers("/api/payments/webhook").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/api/notifications/test/expire").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/notifications/test/reminder").permitAll();


                    auth.requestMatchers(HttpMethod.GET, "/api/properties").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/properties/*").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/properties/search").permitAll();

                    auth.requestMatchers(HttpMethod.POST, "/api/rental-requests").authenticated();
                    auth.requestMatchers(HttpMethod.GET, "/api/rental-requests/**").authenticated();
                    auth.requestMatchers(HttpMethod.POST, "/api/rental-requests/**").authenticated();

                    auth.requestMatchers(HttpMethod.GET, "/api/payments/receipt/*").authenticated();

                    auth.requestMatchers(HttpMethod.GET, "/api/notifications").authenticated();
                    auth.requestMatchers(HttpMethod.GET, "/api/notifications/unread").authenticated();
                    auth.requestMatchers(HttpMethod.GET, "/api/notifications/unread/count").authenticated();
                    auth.requestMatchers(HttpMethod.PATCH, "/api/notifications/*/read").authenticated();

                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
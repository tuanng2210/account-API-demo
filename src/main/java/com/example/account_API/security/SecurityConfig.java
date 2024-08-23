package com.example.account_API.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application.
 * 
 * This class configures security settings such as session management, CSRF
 * protection,
 * authorization rules, and basic authentication for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        /**
         * Configures the security filter chain for the application.
         * 
         * @param http The HttpSecurity object to configure security settings.
         * @return A configured SecurityFilterChain object.
         * @throws Exception If an error occurs while configuring security settings.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                // Configures session management to be stateless (no session is created or
                                // used).
                                .sessionManagement(
                                                session -> session
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Disables Cross-Site Request Forgery (CSRF) protection.
                                .csrf(csrf -> csrf.disable())

                                // Configures authorization rules: allow all requests to "/account",
                                // and require authentication for any other request.
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/account").permitAll()
                                                .anyRequest().authenticated())

                                // Enables basic HTTP authentication.
                                .httpBasic(Customizer.withDefaults())

                                // Builds and returns the security filter chain.
                                .build();
        }

        /**
         * Configures an in-memory user details manager with a single user.
         * 
         * This method creates an in-memory user with the username "client",
         * the password "DoNotTell" (not encoded), and the authority "read".
         * 
         * @return An InMemoryUserDetailsManager containing the configured user.
         */
        @Bean
        public InMemoryUserDetailsManager users() {
                return new InMemoryUserDetailsManager(
                                User.withUsername("client")
                                                .password("{noop}DoNotTell")
                                                .authorities("read")
                                                .build());
        }
}

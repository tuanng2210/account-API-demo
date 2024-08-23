package com.example.account_API.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.account_API.security.TokenService;

/**
 * Controller for handling authentication-related requests.
 * 
 * This controller provides endpoints for generating JWT tokens and checking the
 * health of the authorization server.
 */
@RestController
@RequestMapping("/account")
public class AuthController {

    /**
     * TokenService for generating JWT tokens.
     * 
     * This service is injected into the controller to handle token creation.
     */
    @Autowired
    TokenService tokenService;

    /**
     * Endpoint to generate a JWT token for the authenticated user.
     * 
     * This endpoint is accessible via a POST request to "/account/token".
     * 
     * @param authentication The Authentication object containing the user's
     *                       details.
     * @return A JWT token as a string.
     */
    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    /**
     * Endpoint to check the health of the authorization server.
     * 
     * This endpoint is accessible via a GET request to "/account" and simply
     * returns a confirmation message.
     * 
     * @return A string confirming that the authorization server is running.
     */
    @GetMapping
    public String health() {
        return "Authorization Server is running.";
    }

}

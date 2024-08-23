package com.example.account_API.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * Service for generating JWT tokens.
 * 
 * This service is responsible for creating JWT tokens using RSA private and
 * public keys.
 * The tokens include claims such as issuer, issued time, expiration time,
 * subject, and scope.
 */
@Service
public class TokenService {

    /**
     * The RSA private key used to sign the JWT.
     */
    @Value("${rsa.private-key}")
    RSAPrivateKey privateKey;

    /**
     * The RSA public key used to verify the JWT.
     */
    @Value("${rsa.public-key}")
    RSAPublicKey publicKey;

    /**
     * Generates a JWT token for the given authentication object.
     * 
     * @param authentication The authentication object containing user details.
     * @return A signed JWT token as a string.
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();// Current timestamp

        // Generate a space-separated string of authorities (roles) from the
        // authentication object
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Build the JWT claims set with issuer, issued time, expiration time, subject,
        // and scope
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        // Encode and return the JWT token as a string
        return encoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Configures and returns the JwtEncoder used to sign the JWT.
     * 
     * @return A configured JwtEncoder.
     */
    private JwtEncoder encoder() {
        // Create an RSAKey with the public and private keys
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        
        // Create a JWKSource using the RSAKey
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

         // Return a NimbusJwtEncoder configured with the JWKSource
        return new NimbusJwtEncoder(jwks);
    }
}

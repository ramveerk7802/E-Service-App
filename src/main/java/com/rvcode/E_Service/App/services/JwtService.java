package com.rvcode.E_Service.App.services;


import com.rvcode.E_Service.App.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService {

    private final String secretKey = "144d6827f19a17d7d8faff7af472afa705de355a3724e67a71d21f355bf424db6a16df0d527714fff0f3389b5d98e1c6ba09fd56c84c2ddddf6487837ff0d6e47ea97f3fb483828ce78b78186a710b3c782cf3008d3d4ba0edc31611d265a10af8da64b707e44f6f27c2abc8ed93ed2f66940bc0795f69ea9b17bdcfae79a4947f9ba94325f3fd44b72af177a3b116a4b3e6820c826dc628540085aac1b0a31dfab8d8ce3fa7b388debaca5110ad60d2a1a014ab5bbfb027f8595ebd7c57727c42ca21a9de6dbff6020a8664f7781522de9d2cd7b06566070c4d600f85097e158a7655459506159200367267e5639a16848dda5e15a848c0201024cf54199b66";
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth->auth.getAuthority().replace("ROLE_",""))
                .orElse("UNKNOWN");
        claims.put("role",role);
        return createToken(claims, userDetails.getUsername());
    }


    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer("rvcode")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days validity
                .signWith(getSecretKey())
                .compact();
    }



    public String extractUserName(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }




    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuer("rvcode")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*10*1000))
                .and()
                .signWith(getSecretKey())
                .compact();
    }


    private SecretKey getSecretKey() {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }

}

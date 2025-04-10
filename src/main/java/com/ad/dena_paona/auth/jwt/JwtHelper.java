package com.ad.dena_paona.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtHelper {

    @Value("${security.jwt.token.access.expire}")
    private long accessTokenValidity;
    @Value("${security.jwt.token.refresh.expire}")
    private Long refreshTokenValidity;
    private JwtParser jwtParser;
    @Value("${security.jwt.token.secret-key}")
    private String signingKey;
    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(this.signingKey.getBytes());
        this.jwtParser = Jwts.parser().setSigningKey(this.key);
    }
    public String createToken(String userName, List<String> roles, TokenType tokenType, Date tokenCreationTime){
        log.debug("Token create request for username : {}, tokenType : {}, tokenCreateTime : {}", new Object[]{userName, tokenType, tokenCreationTime});
        Claims claims  = Jwts.claims().setSubject(userName);
        Date tokenValidity;

        if(tokenType.equals(TokenType.ACCESS_TOKEN)){
            claims.put("roles", roles);
            log.debug("roles : {}",roles);
            tokenValidity = new Date(tokenCreationTime.getTime() + TimeUnit.MINUTES.toMillis(this.accessTokenValidity));
        }
        else {
            log.debug("roles : {}", roles);
            tokenValidity = new Date(tokenCreationTime.getTime() + TimeUnit.MINUTES.toMillis(this.refreshTokenValidity));
        }

        return Jwts
                .builder()
                .setClaims(claims)
                .setHeaderParam("type", tokenType)
                .setIssuedAt(tokenCreationTime)
                .setExpiration(tokenValidity)
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();

    }
}

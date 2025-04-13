package com.ad.dena_paona.auth.jwt;

import com.ad.dena_paona.config.redis.RedisKey;
import com.ad.dena_paona.utils.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.Key;
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
    private final RedisTemplate<String, String> redisTemplate;

    public JwtHelper(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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

    public boolean insertTokenToBlackList(HttpServletRequest request, String tokenType){
        try {
            if (tokenType.equals(TokenType.ACCESS_TOKEN.toString())){
                String accessToken = this.resolveToken(request);
                String accessTokenKey = RedisKey.accessTokenKeyPrefix + accessToken;
                this.redisTemplate.opsForValue().set(accessTokenKey, accessToken, TimeUnit.MINUTES.toMillis(this.accessTokenValidity), TimeUnit.MILLISECONDS);
                return true;
            }
            else {
                String refreshToken = this.resolveToken(request);
                String refreshTpkenKey = RedisKey.refreshTokenKeyPrefix + refreshToken;
                this.redisTemplate.opsForValue().set(refreshTpkenKey, refreshToken, TimeUnit.MINUTES.toMillis(refreshTokenValidity), TimeUnit.MILLISECONDS);
                return  true;
            }
        }catch (Exception ex){
            log.debug("Error while inserting jwt into blacklist", ex);
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request){
        log.debug("Resolving jwt token ");
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            log.debug("Bearer token found, returning token");
            return bearerToken.substring("Bearer ".length());
        }
        else{
            log.debug("Bearer token NOT found, returning token : null");
            return null;
        }
    }
}

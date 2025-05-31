/*
package com.ad.dena_paona.controller;

import com.ad.dena_paona.auth.jwt.JwtHelper;
import com.ad.dena_paona.utils.*;
import com.ad.dena_paona.entity.User;
import com.ad.dena_paona.exception.UserNotFoundException;
import com.ad.dena_paona.payload.request.AuthRequest;
import com.ad.dena_paona.payload.response.AuthResponse;
import com.ad.dena_paona.payload.response.RestApiResponse;
import com.ad.dena_paona.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/DP/auth")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
})
@Slf4j
public class AuthController {

    @Value("${security.jwt.token.access.expire}")
    private long accessTokenValidity;
    @Value("${security.jwt.token.refresh.expire}")
    private long refreshTokenValidity;

    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthController(MessageSource messageSource, AuthenticationManager authenticationManager, UserRepository userRepository, JwtHelper jwtHelper, RedisTemplate<String, String> redisTemplate) {
        this.messageSource = messageSource;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping("/login")
    @Operation(summary = "Login", description = "Login using username and password")
    public ResponseEntity<RestApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest){
        log.debug("login request /auth/login with userName: {}", authRequest.getUserName());
        RestApiResponse<AuthResponse> restApiResponse = new RestApiResponse<>();
        try {
            String userName = authRequest.getUserName().trim().toLowerCase();
            log.debug("Authentication start...");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, authRequest.getPassword()));
            log.debug("Authintication complete");

            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUserName(userName));
            User user;
            if(optionalUser.isPresent()){
                user = optionalUser.get();
            }
            else {
                throw new UserNotFoundException("User Not Found");
            }

            Date tokenCreateTime = new Date();
            List<String> roles = new ArrayList<>();
            // Add roles or fetch

            String accessToken = jwtHelper.createToken(userName,roles, TokenType.ACCESS_TOKEN, tokenCreateTime);
            String refreshToken = jwtHelper.createToken(userName,roles, TokenType.REFRESH_TOKEN, tokenCreateTime);

            long accTokValInMilli = TimeUnit.MINUTES.toMillis(accessTokenValidity);
            long refTokValInMilli = TimeUnit.MINUTES.toMillis(refreshTokenValidity);
            restApiResponse = Utils.buildSuccessRestResponse(HttpStatus.OK, new AuthResponse(userName, accessToken,refreshToken));
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);

        }
        catch (BadCredentialsException ex){
            log.debug("BadCredentialException", ex);
            restApiResponse = Utils.buildErrorRestResponse(HttpStatus.UNAUTHORIZED, "Incorrect Username or Password");
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);

        }
        catch (UserNotFoundException ex){
            restApiResponse = Utils.buildErrorRestResponse(HttpStatus.NOT_FOUND, "User Not Found");
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
        catch (Exception ex){
            log.debug("Error while login");
            restApiResponse = Utils.buildErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Login");
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
    }


    @RequestMapping(path = "/refresh", method = RequestMethod.POST)
    @Operation(summary = "Refresh",
            security = @SecurityRequirement(name = "bearer-auth"),
            description = "Need to Provide Refresh Token as Bearer Token In Authorization Header")
    public ResponseEntity<RestApiResponse<AuthResponse>> refresh(HttpServletRequest request) {
        log.debug("Inside /auth/refresh of AuthController");
        RestApiResponse<AuthResponse> restApiResponse;
        try {
            UserDetails userDetails = (UserDetails) request.getSession().getAttribute(SessionKey.USER_DETAILS);
            String userName = userDetails.getUsername();
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUserName(userName));
            User user;
            if(optionalUser.isPresent()){
                log.debug("User : {}", optionalUser.get().toString());
                user = optionalUser.get();
                log.debug("Status : {}", user.getStatus().toString());
                if(user.getStatus().equals(Status.INACTIVE)){
                    throw new DisabledException("User is Inactive");
                }
            }
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            List<String> roles = new ArrayList<>(AuthorityUtils.authorityListToSet(authorities));
            Date tokenCreateTime = new Date();
            String accessToken = jwtHelper.createToken(userName, roles, TokenType.ACCESS_TOKEN, tokenCreateTime);
            AuthResponse authResponse = new AuthResponse(userName, accessToken);
            restApiResponse = Utils.buildSuccessRestResponse(HttpStatus.OK, authResponse);
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
        catch (DisabledException ex){
            log.debug("DisabledException found", ex);
            restApiResponse = Utils.buildErrorRestResponse(HttpStatus.NOT_FOUND, "User disabled");
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
        catch (Exception ex){
            log.debug("Error while login : {}", ex);
            restApiResponse = Utils.buildErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to login");
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @Operation(summary = "Logout",
            security = @SecurityRequirement(name = "bearer-auth"),
            description = "Need to Provide Bearer Token In Authorization Header")
    public ResponseEntity<RestApiResponse<String>> logout(HttpServletRequest request) {
        log.debug("Inside /auth/logout for logout of AuthController");
        UserDetails userDetails = (UserDetails) request.getSession().getAttribute(SessionKey.USER_DETAILS);
        String tokenType = (String) request.getSession().getAttribute(SessionKey.TYPE_OF_TOKEN);
        log.debug("username : {} and tokenType from session : {}", userDetails.getUsername(), tokenType);
        RestApiResponse<String> restApiResponse ;
        boolean inserted = jwtHelper.insertTokenToBlackList(request, tokenType);
        if(inserted){
            log.debug("Log out successful");
            restApiResponse = Utils.buildSuccessRestResponse(HttpStatus.OK, ResponseMessages.LOGOUT_SUCCESS);
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
        else {
            log.debug("Logout failed");
            restApiResponse = Utils.buildErrorRestResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessages.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(restApiResponse.getStatus()).body(restApiResponse);
        }
    }

}
*/

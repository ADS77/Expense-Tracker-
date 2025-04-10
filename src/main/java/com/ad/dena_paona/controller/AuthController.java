package com.ad.dena_paona.controller;

import com.ad.dena_paona.auth.jwt.JwtHelper;
import com.ad.dena_paona.auth.jwt.TokenType;
import com.ad.dena_paona.entity.User;
import com.ad.dena_paona.exception.UserNotFoundException;
import com.ad.dena_paona.payload.request.AuthRequest;
import com.ad.dena_paona.payload.response.AuthResponse;
import com.ad.dena_paona.payload.response.RestApiResponse;
import com.ad.dena_paona.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    public AuthController(MessageSource messageSource, AuthenticationManager authenticationManager, UserRepository userRepository, JwtHelper jwtHelper) {
        this.messageSource = messageSource;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
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


        }
    }
}

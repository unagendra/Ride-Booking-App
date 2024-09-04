package com.project.rideBooking.UberApp.controllers;


import com.project.rideBooking.UberApp.dto.*;
import com.project.rideBooking.UberApp.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto) {
        return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
    }

    //Only Users with Admin role can access
    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId, @RequestBody onBoardDriverDto onBoardDriverDto){

        return new ResponseEntity<>(authService.onboardNewDriver(userId,onBoardDriverDto.getVehicleId()), HttpStatus.CREATED);

    }

    /**
     * i/p-LoginRequestDTO
     * o/p-LoginResponseDTo(access Token)
     * @param loginRequestDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<loginResponseDto> login(@RequestBody loginRequestDto loginRequestDto, HttpServletResponse httpServletResponse){

       String tokens[]= authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

       //refresh Token
        Cookie cookie=new Cookie("token", tokens[1]);
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);


       return new ResponseEntity<>(new loginResponseDto(tokens[0]),HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<loginResponseDto> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new loginResponseDto(accessToken));
    }

}

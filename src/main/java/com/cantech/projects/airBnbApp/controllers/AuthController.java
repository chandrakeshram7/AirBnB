package com.cantech.projects.airBnbApp.controllers;

import com.cantech.projects.airBnbApp.dtos.*;
import com.cantech.projects.airBnbApp.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO requestDTO) throws BadRequestException {
        return new ResponseEntity<>(authService.signUp(requestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> signUp(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) throws BadRequestException {
        String[] tokens = authService.login(loginRequestDTO);
        Cookie cookie = new Cookie("refresh-token", tokens[1]);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new ResponseEntity<>(new LoginResponseDTO(tokens[0]), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refresh(HttpServletRequest request){
        Cookie []cookies = request.getCookies();
        Cookie requestCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("refresh-token"))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Invalid Refresh token received via request"));

        String refreshToken = requestCookie.getValue();
        return new ResponseEntity<>(authService.refresh(refreshToken), HttpStatus.OK);
    }
}

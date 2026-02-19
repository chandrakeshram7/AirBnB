package com.cantech.projects.airBnbApp.controllers;

import com.cantech.projects.airBnbApp.dtos.LoginRequestDTO;
import com.cantech.projects.airBnbApp.dtos.LoginResponseDTO;
import com.cantech.projects.airBnbApp.dtos.SignUpRequestDTO;
import com.cantech.projects.airBnbApp.dtos.UserDTO;
import com.cantech.projects.airBnbApp.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

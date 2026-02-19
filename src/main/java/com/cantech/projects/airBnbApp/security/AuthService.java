package com.cantech.projects.airBnbApp.security;

import com.cantech.projects.airBnbApp.dtos.LoginRequestDTO;
import com.cantech.projects.airBnbApp.dtos.SignUpRequestDTO;
import com.cantech.projects.airBnbApp.dtos.UserDTO;
import com.cantech.projects.airBnbApp.entities.User;
import com.cantech.projects.airBnbApp.enums.Role;
import com.cantech.projects.airBnbApp.exceptions.ResourceNotFoundException;
import com.cantech.projects.airBnbApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public UserDTO signUp(SignUpRequestDTO signUpRequestDTO) throws BadRequestException{
        User user = userRepository.findByEmail(signUpRequestDTO.getEmail()).orElse(null);
        if(user != null){
            throw new BadRequestException("User with email "+ signUpRequestDTO.getEmail()+" already exists" );
        }
        user = new User();
        user.setEmail(signUpRequestDTO.getEmail());
        user.setName(signUpRequestDTO.getName());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setRoles(Set.of(Role.HOTEL_MANAGER));
        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getEmail(), user.getName());
    }


    public String[] login(LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword()
        ));
        User user = (User)authentication.getPrincipal();
        if(user == null){
            throw new RuntimeException("Error occurred during authentication");
        }
        String accessToken = jwtService.getAccessToken(user);
        String refreshToken = jwtService.getRefreshToken(user);

        String []tokens = new String[2];
        tokens[0]= accessToken;
        tokens[1] = refreshToken;
        return tokens;
    }
}

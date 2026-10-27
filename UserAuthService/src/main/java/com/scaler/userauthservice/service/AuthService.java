package com.scaler.userauthservice.service;

import com.scaler.userauthservice.model.Enum.Role;
import com.scaler.userauthservice.model.User;
import com.scaler.userauthservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User signUp(String email, String password)
    {
        if(userRepository.existsByEmail(email))
        {
            return null;
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public String login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty() || !passwordEncoder.matches(password,optionalUser.get().getPasswordHash()))
        {
            return null;
        }

        return jwtService.generateToken(optionalUser.get());
    }
}

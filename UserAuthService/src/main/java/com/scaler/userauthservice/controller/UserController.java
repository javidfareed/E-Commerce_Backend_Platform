package com.scaler.userauthservice.controller;

import com.scaler.userauthservice.dto.UserDto;
import com.scaler.userauthservice.model.User;
import com.scaler.userauthservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication)
    {
        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserDto dto = new UserDto();
        dto.setId(optionalUser.get().getId());
        dto.setEmail(optionalUser.get().getEmail());
        dto.setRole(optionalUser.get().getRole());
        return ResponseEntity.ok(dto);
    }

}

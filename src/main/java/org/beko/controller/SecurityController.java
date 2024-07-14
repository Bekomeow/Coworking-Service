package org.beko.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.beko.annotations.Loggable;
import org.beko.dto.AuthRequest;
import org.beko.dto.TokenResponse;
import org.beko.dto.UserDTO;
import org.beko.mapper.UserMapper;
import org.beko.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling authentication and authorization operations.
 */
@Api(value = "Security Controller")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;
    private final UserMapper userMapper;

    @ApiOperation(value = "Register a new user", response = UserDTO.class)
    @PostMapping("/registration")
    public ResponseEntity<UserDTO> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userMapper.toDTO(securityService.register(request)));
    }

    @Loggable
    @ApiOperation(value = "Authenticate user", response = TokenResponse.class)
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(securityService.authorize(request.username(), request.password()));
    }
}
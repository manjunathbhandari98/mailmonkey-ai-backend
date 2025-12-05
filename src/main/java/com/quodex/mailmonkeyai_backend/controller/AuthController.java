package com.quodex.mailmonkeyai_backend.controller;

import com.quodex.mailmonkeyai_backend.dto.request.LoginRequest;
import com.quodex.mailmonkeyai_backend.dto.request.RefreshTokenRequest;
import com.quodex.mailmonkeyai_backend.dto.request.RegisterRequest;
import com.quodex.mailmonkeyai_backend.dto.response.AuthResponse;
import com.quodex.mailmonkeyai_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @PostMapping("/register")
  public AuthResponse register(@RequestBody RegisterRequest req) {
    return service.register(req);
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest req) {
    return service.login(req);
  }

  @PostMapping("/refresh")
  public AuthResponse refresh(@RequestBody RefreshTokenRequest req) {
    return service.refresh(req);
  }
}

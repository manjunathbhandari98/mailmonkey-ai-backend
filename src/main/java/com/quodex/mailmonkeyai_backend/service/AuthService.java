package com.quodex.mailmonkeyai_backend.service;

import com.quodex.mailmonkeyai_backend.Mapper.UserMapper;
import com.quodex.mailmonkeyai_backend.dto.request.LoginRequest;
import com.quodex.mailmonkeyai_backend.dto.request.RefreshTokenRequest;
import com.quodex.mailmonkeyai_backend.dto.request.RegisterRequest;
import com.quodex.mailmonkeyai_backend.dto.response.AuthResponse;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.jwt.JwtService;
import com.quodex.mailmonkeyai_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepo;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwt;

  public AuthResponse register(RegisterRequest req) {
    if (userRepo.existsByEmail(req.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    User user = UserMapper.toRegisterEntity(req);
    user.setPassword(encoder.encode(req.getPassword()));

    userRepo.save(user);
    return UserMapper.toAuthResponse(jwt.generateAccessToken(user),jwt.generateRefreshToken(user) );
  }

  public AuthResponse login(LoginRequest req) {

    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
    );

    User user = userRepo.findByEmail(req.getEmail()).orElseThrow();

    return UserMapper.toAuthResponse(jwt.generateAccessToken(user),jwt.generateRefreshToken(user));
  }

  public AuthResponse refresh(RefreshTokenRequest req) {
    String email = jwt.extractEmail(req.getRefreshToken());
    User user = userRepo.findByEmail(email).orElseThrow();

    if (!jwt.isTokenValid(req.getRefreshToken(), user)) {
      throw new RuntimeException("Invalid refresh token");
    }

    return UserMapper.toAuthResponse(jwt.generateAccessToken(user),jwt.generateRefreshToken(user));
  }
}

package com.quodex.mailmonkeyai_backend.Mapper;

import com.quodex.mailmonkeyai_backend.dto.request.RegisterRequest;
import com.quodex.mailmonkeyai_backend.dto.response.AuthResponse;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.enums.Role;

public class UserMapper {
  public static User toRegisterEntity(RegisterRequest req){
    return User.builder()
      .name(req.getName())
      .email(req.getEmail())
      .role(Role.USER)
      .build();
  }

  public static AuthResponse toAuthResponse(String accessToken, String refreshToken){
    return AuthResponse.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }
}

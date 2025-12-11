package com.quodex.mailmonkeyai_backend.Mapper;

import com.quodex.mailmonkeyai_backend.dto.request.EmailRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailResponse;
import com.quodex.mailmonkeyai_backend.entity.Email;
import com.quodex.mailmonkeyai_backend.entity.User;

import java.time.LocalDateTime;

public class EmailMapper {
  public static Email mapperToEntity(EmailRequest request, User user){
    return Email.builder()
      .subject(request.getSubject())
      .content(request.getContent())
      .tone(request.getTone())
      .type(request.getType())
      .user(user)
      .createdAt(LocalDateTime.now())
      .build();
  }

  public static EmailResponse mapperToResponse(Email email){
    return EmailResponse.builder()
      .id(email.getId())
      .subject(email.getSubject())
      .content(email.getContent())
      .type(email.getType())
      .tone(email.getTone())
      .userId(email.getUser().getId())
      .createdAt(email.getCreatedAt())
      .build();
  }
}

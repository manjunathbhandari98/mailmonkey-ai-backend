package com.quodex.mailmonkeyai_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailResponse {
  private String id;
  private String subject;
  private String content;
  private String type;
  private String tone;
  private String userId;
  private LocalDateTime createdAt;
}

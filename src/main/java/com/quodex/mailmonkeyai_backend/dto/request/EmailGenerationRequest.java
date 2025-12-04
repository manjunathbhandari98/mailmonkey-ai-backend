package com.quodex.mailmonkeyai_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailGenerationRequest {
  private String receiver;
  private String sender;
  private String emailType;
  private String tone;
  private String subject;
  private String content;
}

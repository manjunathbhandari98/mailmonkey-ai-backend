package com.quodex.mailmonkeyai_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
  private String subject;
  private String content;
  private String tone;
  private String type;
}

package com.quodex.mailmonkeyai_backend.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateRequest {
  private String title;
  private String subject;
  private String content;
  private String type;
  private String tone;
  private Integer likes;
}

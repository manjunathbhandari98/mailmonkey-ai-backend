package com.quodex.mailmonkeyai_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailImprovementRequest {
  private String originalEmail;
  private String improvementType;
}

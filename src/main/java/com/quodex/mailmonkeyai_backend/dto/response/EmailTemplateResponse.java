package com.quodex.mailmonkeyai_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplateResponse {
  private String id;
  private String title;
  private String subject;
  private String content;
  private String type;
  private String tone;
  private Integer likes;
  private boolean liked;
  private boolean bookMarked;
}

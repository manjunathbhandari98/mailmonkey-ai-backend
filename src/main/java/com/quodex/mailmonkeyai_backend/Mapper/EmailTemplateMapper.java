package com.quodex.mailmonkeyai_backend.Mapper;

import com.quodex.mailmonkeyai_backend.dto.request.EmailTemplateRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailTemplateResponse;
import com.quodex.mailmonkeyai_backend.entity.EmailTemplate;
import com.quodex.mailmonkeyai_backend.entity.User;

public class EmailTemplateMapper {
  public static EmailTemplateResponse mapToResponse(EmailTemplate t, User user) {

    boolean isLiked = user != null && t.getLikedByUsers().contains(user);
    boolean isBookmarked = user != null && t.getBookmarkedByUsers().contains(user);

    return EmailTemplateResponse.builder()
      .id(t.getId())
      .title(t.getTitle())
      .subject(t.getSubject())
      .content(t.getContent())
      .type(t.getType())
      .tone(t.getTone())
      .likes(t.getLikedByUsers().size())
      .liked(isLiked)
      .bookMarked(isBookmarked)
      .build();
  }

  public static EmailTemplate mapToEntity(EmailTemplateRequest request){
    return EmailTemplate.builder()
      .subject(request.getSubject())
      .content(request.getContent())
      .tone(request.getTone())
      .title(request.getTitle())
      .type(request.getType())
      .build();
  }
}

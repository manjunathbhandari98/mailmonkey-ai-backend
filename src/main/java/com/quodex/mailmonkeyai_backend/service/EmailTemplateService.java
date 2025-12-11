package com.quodex.mailmonkeyai_backend.service;

import com.quodex.mailmonkeyai_backend.dto.request.EmailTemplateRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailTemplateResponse;
import com.quodex.mailmonkeyai_backend.entity.EmailTemplate;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


public interface EmailTemplateService {


   List<EmailTemplateResponse> getAllTemplates(User user);

   EmailTemplateResponse getTemplateById(String id,User user);

   List<EmailTemplateResponse> getTemplateByType(String type,User user);

  List<EmailTemplateResponse> getTemplateByTone(String tone,User user);

   EmailTemplateResponse createTemplate(EmailTemplateRequest template);

   EmailTemplateResponse likeTemplate(String id, User user) ;

   EmailTemplateResponse bookmarkTemplate(String id,User user);


}

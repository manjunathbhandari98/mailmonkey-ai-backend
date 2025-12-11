package com.quodex.mailmonkeyai_backend.service.impl;

import com.quodex.mailmonkeyai_backend.Mapper.EmailTemplateMapper;
import com.quodex.mailmonkeyai_backend.dto.request.EmailTemplateRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailResponse;
import com.quodex.mailmonkeyai_backend.dto.response.EmailTemplateResponse;
import com.quodex.mailmonkeyai_backend.entity.EmailTemplate;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.repository.EmailTemplateRepository;
import com.quodex.mailmonkeyai_backend.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.quodex.mailmonkeyai_backend.Mapper.EmailTemplateMapper.mapToEntity;
import static com.quodex.mailmonkeyai_backend.Mapper.EmailTemplateMapper.mapToResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {
  private final EmailTemplateRepository templateRepo;

  @Override
  public List<EmailTemplateResponse> getAllTemplates(User user) {
    return templateRepo.findAll()
      .stream()
      .map(t -> EmailTemplateMapper.mapToResponse(t, user))
      .toList();
  }

  @Override
  public EmailTemplateResponse getTemplateById(String id, User user) {
    EmailTemplate template = templateRepo.findById(id)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "Template not found"));

    return EmailTemplateMapper.mapToResponse(template, user);
  }

  @Override
  public List<EmailTemplateResponse> getTemplateByType(String type, User user) {
    return templateRepo.findByType(type)
      .stream()
      .map(t -> EmailTemplateMapper.mapToResponse(t, user))
      .toList();
  }

  @Override
  public List<EmailTemplateResponse> getTemplateByTone(String tone, User user) {
    return templateRepo.findByTone(tone)
      .stream()
      .map(t -> EmailTemplateMapper.mapToResponse(t, user))
      .toList();
  }


  @Override
  public EmailTemplateResponse createTemplate(EmailTemplateRequest template) {

    EmailTemplate emailTemplate = mapToEntity(template);

    // Initialize like set (ensure not null)
    emailTemplate.setLikedByUsers(new HashSet<>());

    EmailTemplate savedTemplate = templateRepo.save(emailTemplate);

    // User is null because no one "liked" it at creation time
    return EmailTemplateMapper.mapToResponse(savedTemplate, null);
  }


  @Override
  public EmailTemplateResponse likeTemplate(String id, User user) {
    EmailTemplate template = templateRepo.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    boolean alreadyLiked = template.getLikedByUsers().contains(user);

    if (alreadyLiked) {
      template.getLikedByUsers().remove(user); // unlike
    } else {
      template.getLikedByUsers().add(user); // like
    }

    EmailTemplate saved = templateRepo.save(template);

    return EmailTemplateMapper.mapToResponse(saved, user);
  }

  @Override
  public EmailTemplateResponse bookmarkTemplate(String id, User user) {

    EmailTemplate template = templateRepo.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    boolean alreadyBookmarked = template.getBookmarkedByUsers().contains(user);

    if (alreadyBookmarked) {
      template.getBookmarkedByUsers().remove(user);  // unbookmark
    } else {
      template.getBookmarkedByUsers().add(user);      // bookmark
    }

    EmailTemplate saved = templateRepo.save(template);
    return EmailTemplateMapper.mapToResponse(saved, user);
  }



}

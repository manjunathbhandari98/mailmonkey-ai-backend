package com.quodex.mailmonkeyai_backend.controller;

import com.quodex.mailmonkeyai_backend.dto.request.EmailTemplateRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailTemplateResponse;
import com.quodex.mailmonkeyai_backend.entity.EmailTemplate;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class EmailTemplateController {

  private final EmailTemplateService templateService;

  @GetMapping
  public ResponseEntity<List<EmailTemplateResponse>> getAll(
    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(templateService.getAllTemplates(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmailTemplateResponse> get(
    @PathVariable String id,
    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(templateService.getTemplateById(id, user));
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<List<EmailTemplateResponse>> getByType(
    @PathVariable String type,
    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(templateService.getTemplateByType(type, user));
  }

  @GetMapping("/tone/{tone}")
  public ResponseEntity<List<EmailTemplateResponse>> getByTone(
    @PathVariable String tone,
    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(templateService.getTemplateByTone(tone, user));
  }

  @PostMapping("/{id}/bookmark")
  public ResponseEntity<EmailTemplateResponse> bookmark(
    @PathVariable String id,
    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(templateService.bookmarkTemplate(id, user));
  }

  @PostMapping("/{id}/like")
  public ResponseEntity<EmailTemplateResponse> likeTemplate(
    @PathVariable String id,
    @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(templateService.likeTemplate(id,user));
  }

}

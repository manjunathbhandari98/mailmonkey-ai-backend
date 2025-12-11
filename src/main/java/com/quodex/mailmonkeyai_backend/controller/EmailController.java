package com.quodex.mailmonkeyai_backend.controller;

import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailImprovementRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;
import com.quodex.mailmonkeyai_backend.dto.response.EmailResponse;
import com.quodex.mailmonkeyai_backend.entity.Email;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmailController {
  private final EmailService emailService;

  @PostMapping("/generate")
  public ResponseEntity<EmailGenerationResponse> generateEmail(@RequestBody EmailGenerationRequest request){
    return ResponseEntity.ok(emailService.generateEmail(request));
  }

  @PostMapping("/improve")
  public ResponseEntity<EmailGenerationResponse> improveEmail(@RequestBody EmailImprovementRequest request){
    return ResponseEntity.ok(emailService.improveEmail(request));
  }

  @PostMapping("/save")
  public ResponseEntity<EmailResponse> saveEmail(
    @RequestBody EmailRequest request,
    @AuthenticationPrincipal User user
  ) {
    EmailResponse saved = emailService.saveEmail(request, user);
    return ResponseEntity.ok(saved);
  }


  @GetMapping("/history")
  public ResponseEntity<List<EmailResponse>> getHistory(@AuthenticationPrincipal User user){
    return ResponseEntity.ok(emailService.getEmails(user));
  }

  @DeleteMapping("/delete/{emailId}")
  public ResponseEntity<String> deleteHistory(@PathVariable String emailId, @AuthenticationPrincipal User user ){

    emailService.deleteEmail(emailId,user);
    return ResponseEntity.ok("Email Deleted Successfully");
  }

  @GetMapping("/recent")
  public ResponseEntity<List<EmailResponse>> getRecentEmails(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(emailService.getRecentEmails(user));
  }

}

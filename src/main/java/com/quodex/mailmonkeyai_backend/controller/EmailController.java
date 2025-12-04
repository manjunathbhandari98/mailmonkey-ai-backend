package com.quodex.mailmonkeyai_backend.controller;

import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailImprovementRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;
import com.quodex.mailmonkeyai_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}

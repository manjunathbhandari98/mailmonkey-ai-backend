package com.quodex.mailmonkeyai_backend.service;

import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;

public interface EmailService {
  EmailGenerationResponse generateEmail(EmailGenerationRequest request);
}

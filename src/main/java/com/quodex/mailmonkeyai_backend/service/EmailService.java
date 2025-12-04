package com.quodex.mailmonkeyai_backend.service;

import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailImprovementRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;
import org.jspecify.annotations.Nullable;

public interface EmailService {
  EmailGenerationResponse generateEmail(EmailGenerationRequest request);

  EmailGenerationResponse improveEmail(EmailImprovementRequest request);
}

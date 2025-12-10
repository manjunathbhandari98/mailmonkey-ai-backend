package com.quodex.mailmonkeyai_backend.service;

import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailImprovementRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;
import com.quodex.mailmonkeyai_backend.dto.response.EmailResponse;
import com.quodex.mailmonkeyai_backend.entity.Email;
import com.quodex.mailmonkeyai_backend.entity.User;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface EmailService {
  EmailGenerationResponse generateEmail(EmailGenerationRequest request);

  EmailGenerationResponse improveEmail(EmailImprovementRequest request);

  Email saveEmail(EmailRequest request, User user);

  List<EmailResponse> getEmails(User user);
  void deleteEmail(String emailId, User user);
}

package com.quodex.mailmonkeyai_backend.service.impl;

import com.quodex.mailmonkeyai_backend.Mapper.EmailMapper;
import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailImprovementRequest;
import com.quodex.mailmonkeyai_backend.dto.request.EmailRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;
import com.quodex.mailmonkeyai_backend.dto.response.EmailResponse;
import com.quodex.mailmonkeyai_backend.entity.Email;
import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.repository.EmailRepository;
import com.quodex.mailmonkeyai_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

  private final WebClient webClient;
  private final String apiKey;
  private final EmailRepository emailRepository;

  public EmailServiceImpl(
    WebClient.Builder webClientBuilder,
    @Value("${gemini.api.url}") String baseUrl,
    @Value("${gemini.api.key}") String apiKey,
    EmailRepository emailRepository
  ) {
    this.webClient = webClientBuilder
      .baseUrl(baseUrl)
      .defaultHeader("Content-Type","application/json")
      .build();
    this.apiKey = apiKey;
    this.emailRepository = emailRepository;

  }

  @Override
  public EmailGenerationResponse generateEmail(EmailGenerationRequest request) {
    String prompt = buildPrompt(request);
    String requestBody = String.format("""
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """, prompt);

    try {
      String response = webClient.post()
        .uri("/v1beta/models/gemini-2.5-flash:generateContent")
        .header("x-goog-api-key", apiKey)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();
      String generatedResponse = extractResponse(response);
      return EmailGenerationResponse.builder()
        .generatedEmail(generatedResponse)
        .build();

    } catch (WebClientResponseException.Forbidden ex) {
      // Google API returned 403 - return 400 to Angular instead
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Unable to generate email. Please check your API key or quota limits."
      );
    } catch (WebClientResponseException.Unauthorized ex) {
      // Google API returned 401
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Invalid API credentials for email generation service."
      );
    } catch (WebClientResponseException.TooManyRequests ex) {
      // Google API returned 429
      throw new ResponseStatusException(
        HttpStatus.TOO_MANY_REQUESTS,
        "API rate limit exceeded. Please try again later."
      );
    } catch (WebClientResponseException ex) {
      // Other WebClient errors (400, 500, etc.)
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Failed to generate email: " + ex.getMessage()
      );
    } catch (Exception ex) {
      // Generic errors (JSON parsing, etc.)
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred while generating the email."
      );
    }
  }

  @Override
  public EmailGenerationResponse improveEmail(EmailImprovementRequest request) {
    String prompt = buildImprovePrompt(request);
    String requestBody = String.format("""
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """, prompt);

    try {
      String response = webClient.post()
        .uri("/v1beta/models/gemini-2.5-flash:generateContent")
        .header("x-goog-api-key", apiKey)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();

      String generatedResponse = extractResponse(response);
      return EmailGenerationResponse.builder()
        .generatedEmail(generatedResponse)
        .build();

    } catch (WebClientResponseException.Forbidden ex) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Unable to improve email. Please check your API key or quota limits."
      );
    } catch (WebClientResponseException.Unauthorized ex) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Invalid API credentials for email improvement service."
      );
    } catch (WebClientResponseException.TooManyRequests ex) {
      throw new ResponseStatusException(
        HttpStatus.TOO_MANY_REQUESTS,
        "API rate limit exceeded. Please try again later."
      );
    } catch (WebClientResponseException ex) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Failed to improve email: " + ex.getMessage()
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred while improving the email."
      );
    }
  }


  private String extractResponse(String response) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(response);
      JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
      return textNode.stringValue();
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse response from AI service", e);
    }
  }

  private String buildPrompt(EmailGenerationRequest request) {

    // Tone: default to "Professional" if null or empty
    String tone = (request.getTone() == null || request.getTone().isBlank())
      ? "Professional"
      : request.getTone();

    // Subject: optional — include only if user provided it
    String subject = (request.getSubject() == null || request.getSubject().isBlank())
      ? "No subject provided"
      : request.getSubject();

    // Build prompt clearly
    return """
            Generate an email with the following details:
            - Content: %s
            - Email Type: %s
            - Tone: %s
            - Receiver: %s
            - Sender: %s
            - Subject: %s

            Write the email in a clear, natural, human-like tone.
            """.formatted(
      request.getContent(),
      request.getEmailType(),
      tone,
      request.getReceiver(),
      request.getSender(),
      subject
    );
  }

  private String buildImprovePrompt(EmailImprovementRequest request) {
    return """
        Improve this email based on the selected option: %s.
        Keep meaning intact and return only the improved email: %s
        """.formatted(
      request.getImprovementType(),
      request.getOriginalEmail()
    );
  }

  @Override
  public EmailResponse saveEmail(EmailRequest request, User user) {

    Email email = EmailMapper.mapperToEntity(request,user);
    return EmailMapper.mapperToResponse(emailRepository.save(email));
  }

  @Override
  public void deleteEmail(String emailId, User user) {
    if (user == null)
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged in");

    // If admin → fetch any email
    Email email = emailRepository.findByIdAndUser(emailId, user)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.FORBIDDEN, "You do not have permission to delete this email"));

    emailRepository.delete(email);
  }


  public List<EmailResponse> getEmails(User user) {
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }
    List<Email> emails = emailRepository.findByUser(user);
    return emails.stream()
      .map(email -> EmailResponse.builder()
        .id(email.getId())
        .subject(email.getSubject())
        .content(email.getContent())
        .type(email.getType())
        .tone(email.getTone())
        .userId(email.getUser().getId().toString())
        .build()
      )
      .toList();
  }

  @Override
  public List<EmailResponse> getRecentEmails(User user){
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }
    List<Email> emails = emailRepository.findTop10ByUserOrderByCreatedAtDesc(user);
    return emails.stream()
      .map(email -> EmailResponse.builder()
        .id(email.getId())
        .subject(email.getSubject())
        .content(email.getContent())
        .type(email.getType())
        .tone(email.getTone())
        .userId(email.getUser().getId().toString())
        .build()
      )
      .toList();
  }
}

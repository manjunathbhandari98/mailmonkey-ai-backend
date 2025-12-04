package com.quodex.mailmonkeyai_backend.service.impl;

import com.quodex.mailmonkeyai_backend.dto.request.EmailGenerationRequest;
import com.quodex.mailmonkeyai_backend.dto.response.EmailGenerationResponse;
import com.quodex.mailmonkeyai_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class EmailServiceImpl implements EmailService {

  private final WebClient webClient;
  private final String apiKey;

  public EmailServiceImpl(
    WebClient.Builder webClientBuilder,
    @Value("${gemini.api.url}") String baseUrl,
    @Value("${gemini.api.key}") String apiKey
    ) {
    this.webClient = webClientBuilder
      .baseUrl(baseUrl)
      .defaultHeader("Content-Type","application/json")
      .build();
    this.apiKey = apiKey;
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

    String response = webClient.post()
      .uri("/v1beta/models/gemini-2.5-flash:generateContent")
      .header("x-goog-api-key", apiKey)
      .bodyValue(requestBody)
      .retrieve()
      .bodyToMono(String.class)
      .block();

    String generatedResponse =  extractResponse(response);
    return EmailGenerationResponse.builder()
      .generatedEmail(generatedResponse)
      .build();
  }

  private String extractResponse(String response) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(response);
      JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
      return textNode.stringValue();
    } catch (Exception e) {
      throw new RuntimeException(e);
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

}

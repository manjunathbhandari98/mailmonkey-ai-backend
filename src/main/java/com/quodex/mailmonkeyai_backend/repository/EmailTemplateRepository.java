package com.quodex.mailmonkeyai_backend.repository;

import com.quodex.mailmonkeyai_backend.entity.Email;
import com.quodex.mailmonkeyai_backend.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, String> {
  List<EmailTemplate> findByType(String type);
  List<EmailTemplate> findByTone(String tone);
  List<EmailTemplate> findByTitleContainingIgnoreCase(String title);
}

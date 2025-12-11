package com.quodex.mailmonkeyai_backend.repository;

import com.quodex.mailmonkeyai_backend.dto.response.EmailResponse;
import com.quodex.mailmonkeyai_backend.entity.Email;
import com.quodex.mailmonkeyai_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, String> {
  List<Email> findByUser(User user);
  Optional<Email> findByIdAndUser(String id, User user);
  List<Email> findTop10ByUserOrderByCreatedAtDesc(User user);

}

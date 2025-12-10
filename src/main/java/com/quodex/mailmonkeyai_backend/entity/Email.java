package com.quodex.mailmonkeyai_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String subject;
  @Column(columnDefinition = "TEXT")
  private String content;
  private String type;
  private String tone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  private LocalDateTime createdAt;
}

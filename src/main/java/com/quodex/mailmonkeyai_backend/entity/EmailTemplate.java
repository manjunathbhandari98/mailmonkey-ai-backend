package com.quodex.mailmonkeyai_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "email_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String subject;

  @Column(columnDefinition = "TEXT")
  private String content;

  private String type;
  private String tone;

  private Integer likes = 0;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "template_likes",
    joinColumns = @JoinColumn(name = "template_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> likedByUsers = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "template_bookmarks",
    joinColumns = @JoinColumn(name = "template_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @Builder.Default
  private Set<User> bookmarkedByUsers = new HashSet<>();

}


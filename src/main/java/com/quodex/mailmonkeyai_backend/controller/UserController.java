package com.quodex.mailmonkeyai_backend.controller;

import com.quodex.mailmonkeyai_backend.entity.User;
import com.quodex.mailmonkeyai_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;

  @GetMapping("/me")
  public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user){
    Map<String, Object> response = new HashMap<>();
    response.put("id",user.getId());
    response.put("name",user.getName());
    response.put("email",user.getEmail());
    response.put("role",user.getRole());

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User userDetails) {

    // Email from authenticated user
    String email = userDetails.getEmail();

    // Find user in DB
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new RuntimeException("User not found"));

    // Delete user
    userRepository.delete(user);

    return ResponseEntity.ok("User deleted successfully");
  }

}

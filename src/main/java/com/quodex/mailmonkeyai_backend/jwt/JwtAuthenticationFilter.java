package com.quodex.mailmonkeyai_backend.jwt;
import com.quodex.mailmonkeyai_backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository userRepo;

  @Override
  protected void doFilterInternal(
    HttpServletRequest req,
    HttpServletResponse res,
    FilterChain chain
  ) throws ServletException, IOException {

    String header = req.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    String token = header.substring(7);
    String email = jwtService.extractEmail(token);

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      var user = userRepo.findByEmail(email).orElse(null);

      if (user != null && jwtService.isTokenValid(token, user)) {
        var authToken = new UsernamePasswordAuthenticationToken(
          user, null, user.getAuthorities());

        authToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(req)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    chain.doFilter(req, res);
  }
}

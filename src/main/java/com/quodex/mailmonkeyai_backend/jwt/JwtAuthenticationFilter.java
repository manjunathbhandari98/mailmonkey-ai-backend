package com.quodex.mailmonkeyai_backend.jwt;
import com.quodex.mailmonkeyai_backend.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    String path = req.getServletPath();
    if (path.startsWith("/api/auth")) {
      chain.doFilter(req, res);
      return;
    }

    String header = req.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    try {
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
        } else {
          // Token is invalid (but not expired) - still return 401
          res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          res.setContentType("application/json");
          res.getWriter().write("{\"error\": \"Invalid access token\"}");
          return;
        }
      }
    } catch (ExpiredJwtException ex) {
      // Access token expired → Return 401 (Angular will trigger refresh)
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType("application/json");
      res.getWriter().write("{\"error\": \"Access token expired\"}");
      return;
    } catch (Exception ex) {
      // Other JWT errors → Return 401
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType("application/json");
      res.getWriter().write("{\"error\": \"Invalid token\"}");
      return;
    }

    chain.doFilter(req, res);
  }
}

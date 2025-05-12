package com.lending.book.security;

import com.lending.book.entity.User;
import com.lending.book.enums.Role;
import com.lending.book.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

                if (request.getRequestURI().contains("/api/books")) {
                    if (bookEndPointCheck(user.getRole() == Role.BORROWER || user.getRole() == Role.LENDER || user.getRole() == Role.BOTH, username, request, response, "You don't have permission to view books"))
                        return;
                }

                if (request.getRequestURI().contains("/api/books") && request.getMethod().equals("POST")) {
                    if (bookEndPointCheck(user.getRole() == Role.LENDER || user.getRole() == Role.BOTH, username, request, response, "You don't have permission to add books"))
                        return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static boolean bookEndPointCheck(boolean user, String username, HttpServletRequest request, HttpServletResponse response, String s) throws IOException {
        if (user) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(s);
            return true;
        }
        return false;
    }
}



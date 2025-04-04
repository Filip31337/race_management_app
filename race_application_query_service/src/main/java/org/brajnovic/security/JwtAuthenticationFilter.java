package org.brajnovic.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getClaims(token);
                String subject = claims.getSubject();
                String role = claims.get("role", String.class);
                UsernamePasswordAuthenticationToken authentication;
                if (role != null && !role.isEmpty()) {
                    log.info("(JwtAuthenticationFilter) - role detected: {}", role);
                    authentication = new UsernamePasswordAuthenticationToken(
                            subject,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(role))
                    );
                } else {
                    log.info("(JwtAuthenticationFilter) - no role present");
                    authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.info("(JwtAuthenticationFilter) - token not valid");
            }
        } else {
            log.info("(JwtAuthenticationFilter) - no authentication header present");
        }
        filterChain.doFilter(request, response);
    }
}


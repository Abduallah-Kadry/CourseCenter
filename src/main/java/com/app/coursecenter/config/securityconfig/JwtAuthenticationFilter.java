package com.app.coursecenter.config.securityconfig;

import com.app.coursecenter.config.ApiPathConfig;
import com.app.coursecenter.service.authenticationservice.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ApiPathConfig pathConfig;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   @Lazy UserDetailsService userDetailsService,
                                   ApiPathConfig pathConfig) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.pathConfig = pathConfig;
        log.info("JwtAuthenticationFilter initialized");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();


        // Skip JWT check for public paths
        if (isPublicPath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromRequest(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );


                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (ExpiredJwtException ex) {
            log.error("JWT token has expired", ex);
            handleJwtException(response, "JWT token has expired");
            return;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature", ex);
            handleJwtException(response, "Invalid JWT signature");
            return;
        } catch (JwtException ex) {
            log.error("Invalid JWT token", ex);
            handleJwtException(response, "Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleJwtException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        // Check Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.debug("JWT found in Authorization header");
            return authHeader.substring(7);
        }

        // Fallback: check cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    log.debug("JWT found in cookie");
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isPublicPath(String requestPath) {
        // Check static public paths
        boolean isStaticPublic = Stream.of(pathConfig.getPublicPaths())
                .anyMatch(pattern -> matchesPattern(requestPath, pattern));

        if (isStaticPublic) {
            return true;
        }

        // Check frontend public pages
        boolean isFrontendPublic = Stream.of(pathConfig.getFrontEndPublicPages())
                .anyMatch(path -> {
                    String fullPath = pathConfig.getFrontendBase() + path;
                    return matchesPattern(requestPath, fullPath);
                });

        if (isFrontendPublic) {
            log.debug("Request matches frontend public page");
        }

        return isFrontendPublic;
    }

    private boolean matchesPattern(String requestPath, String pattern) {
        if (pattern.endsWith("/**")) {
            String base = pattern.substring(0, pattern.length() - 3);
            return requestPath.startsWith(base);
        } else if (pattern.endsWith("/*")) {
            String base = pattern.substring(0, pattern.length() - 2);
            return requestPath.startsWith(base + "/") || requestPath.equals(base);
        } else {
            return requestPath.equals(pattern) || requestPath.startsWith(pattern + "/");
        }
    }
}
package org.govt.Authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.govt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserSupplierRepository userSupplier;

    @Autowired
    private UserContractorRepository userContractor;

    @Autowired
    private UserGovtRepository usergovt;

    @Autowired
    private UserProjectManagerRepository userProject;

    @Autowired
    private UserSupervisorRepository userSupervisor;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            // Try to find the user in each repository
            if (userSupplier.findByUsername(username)!=null) {
                userDetails = userSupplier.findByUsername(username);
            } else if (userContractor.findByUsername(username)!=null) {
                userDetails = userContractor.findByUsername(username);
            } else if (userProject.findByUsername(username)!=null) {
                userDetails = userProject.findByUsername(username);
            } else if (usergovt.findByUsername(username)!=null) {
                userDetails = usergovt.findByUsername(username);
            } else if (userSupervisor.findByUsername(username)!=null) {
                userDetails = userSupervisor.findByUsername(username);
            }

            if (userDetails != null && jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}


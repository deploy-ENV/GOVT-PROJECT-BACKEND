package org.govt.Authentication;

import org.govt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

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
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Log all WebSocket commands for debugging
        if (accessor != null) {
            System.out.println("🔵 WebSocket Command: " + accessor.getCommand());
        }

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("🔵 WebSocket CONNECT request received");

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null) {
                System.err.println("❌ No Authorization header found in WebSocket CONNECT");
                return message;
            }

            if (!authHeader.startsWith("Bearer ")) {
                System.err.println("❌ Authorization header does not start with 'Bearer '");
                return message;
            }

            String token = authHeader.substring(7);
            System.out.println("✅ JWT Token extracted: " + token.substring(0, Math.min(20, token.length())) + "...");

            try {
                // Step 1: Extract username from token
                String username = jwtUtil.extractUsername(token);

                if (username == null || username.isEmpty()) {
                    System.err.println("❌ Failed to extract username from JWT token");
                    return message;
                }

                System.out.println("✅ Username extracted from token: " + username);

                // Step 2: Find user in database
                UserDetails userDetails = findUserByUsername(username);

                if (userDetails == null) {
                    System.err.println("❌ User not found in database: " + username);
                    return message;
                }

                System.out.println("✅ User found in database: " + userDetails.getUsername());

                // Step 3: Validate token
                boolean isValid = jwtUtil.validateToken(token, userDetails);

                if (!isValid) {
                    System.err.println("❌ JWT token validation failed for user: " + username);
                    return message;
                }

                System.out.println("✅ JWT token validated successfully for user: " + username);

                // Step 4: Create authentication and set user
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                accessor.setUser(authentication);

                System.out
                        .println("✅✅✅ WebSocket authentication SUCCESS! User ID stored: " + userDetails.getUsername());
                System.out.println("    - Principal name: " + authentication.getName());
                System.out.println("    - Authorities: " + authentication.getAuthorities());

            } catch (Exception e) {
                System.err.println("❌ WebSocket authentication FAILED with exception: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return message;
    }

    private UserDetails findUserByUsername(String username) {
        UserDetails userDetails = null;
        String userType = null;

        // Try to find the user in each repository
        if (userSupplier.findByUsername(username) != null) {
            userDetails = userSupplier.findByUsername(username);
            userType = "Supplier";
        } else if (userContractor.findByUsername(username) != null) {
            userDetails = userContractor.findByUsername(username);
            userType = "Contractor";
        } else if (userProject.findByUsername(username) != null) {
            userDetails = userProject.findByUsername(username);
            userType = "Project Manager";
        } else if (usergovt.findByUsername(username) != null) {
            userDetails = usergovt.findByUsername(username);
            userType = "Government";
        } else if (userSupervisor.findByUsername(username) != null) {
            userDetails = userSupervisor.findByUsername(username);
            userType = "Supervisor";
        }

        if (userDetails != null) {
            System.out.println("✅ User found as: " + userType + " (username: " + username + ")");
        } else {
            System.err.println("❌ User NOT found in any repository: " + username);
        }

        return userDetails;
    }
}

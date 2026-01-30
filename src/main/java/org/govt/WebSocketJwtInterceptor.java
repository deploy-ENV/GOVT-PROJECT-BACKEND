package org.govt;

import org.govt.Authentication.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.govt.repository.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketJwtInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserSupplierRepository userSupplier;

    @Autowired
    private UserContractorRepository userContractor;

    @Autowired
    private UserGovtRepository userGovt;

    @Autowired
    private UserProjectManagerRepository userProject;

    @Autowired
    private UserSupervisorRepository userSupervisor;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                String username = jwtUtil.extractUsername(token);

                if (username != null) {
                    UserDetails userDetails = null;

                    if (userSupplier.findByUsername(username) != null) {
                        userDetails = userSupplier.findByUsername(username);
                    } else if (userContractor.findByUsername(username) != null) {
                        userDetails = userContractor.findByUsername(username);
                    } else if (userProject.findByUsername(username) != null) {
                        userDetails = userProject.findByUsername(username);
                    } else if (userGovt.findByUsername(username) != null) {
                        userDetails = userGovt.findByUsername(username);
                    } else if (userSupervisor.findByUsername(username) != null) {
                        userDetails = userSupervisor.findByUsername(username);
                    }

                    if (userDetails != null && jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        accessor.setUser(authentication);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        return message;
    }
}
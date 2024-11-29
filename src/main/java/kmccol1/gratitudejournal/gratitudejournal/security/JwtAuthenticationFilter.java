//***************************************************************************************
//
//     Filename: JwtAuthenticationFilter.java
//     Author: Kyle McColgan
//     Date: 27 November 2024
//     Description: This file provides the auth token validation implementation.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.security;

//***************************************************************************************

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kmccol1.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import kmccol1.gratitudejournal.gratitudejournal.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

//***************************************************************************************

public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userService)
    {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String jwt = authHeader.substring(7); // Remove "Bearer " prefix

            // Use JwtUtils to get the claims and validate the token
            if (jwtUtils.validateJwtToken(jwt))
            {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                System.out.println("Token validated, user: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            else
            {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token is invalid or expired");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

//***************************************************************************************

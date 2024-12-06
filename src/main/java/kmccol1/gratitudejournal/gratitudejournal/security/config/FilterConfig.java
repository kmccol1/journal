//***************************************************************************************
//
//     Filename: User.java
//     Author: Kyle McColgan
//     Date: 03 December 2024
//     Description: This file holds the auth filter configuration.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.security.config;

import kmccol1.gratitudejournal.gratitudejournal.security.JwtAuthenticationFilter;
import kmccol1.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

//***************************************************************************************

@Configuration
public class FilterConfig
{
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService)
    {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }
}

//***************************************************************************************

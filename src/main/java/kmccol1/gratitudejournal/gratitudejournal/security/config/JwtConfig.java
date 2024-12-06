//***************************************************************************************
//
//     Filename: JwtConfig.java
//     Author: Kyle McColgan
//     Date: 02 December 2024
//     Description: This file contains the token configuration.
//
//***************************************************************************************

package kmccol1.gratitudejournal.gratitudejournal.security.config;

import kmccol1.gratitudejournal.gratitudejournal.security.jwt.JwtUtils;
import kmccol1.gratitudejournal.gratitudejournal.service.UserService;
import org.springframework.context.annotation.Configuration;

//***************************************************************************************

@Configuration
public class JwtConfig
{
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public JwtConfig(JwtUtils jwtUtils, UserService userService)
    {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

}

//***************************************************************************************

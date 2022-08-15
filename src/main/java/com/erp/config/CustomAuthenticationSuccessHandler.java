package com.erp.config;

import com.erp.dto.UserDTO;
import com.erp.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       response.setStatus(HttpServletResponse.SC_OK);
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        HttpSession session = request.getSession();

        UserDTO userDTO = userService.getUserDTOByUserName(authentication.getName());

        session.setAttribute("username", userDTO.getUserName());
        session.setAttribute("department", userDTO.getRole());
        session.setAttribute("empNumber" , userDTO.getCommonStatus());

        if (roles.contains("ROLE_ADMIN"))
            response.sendRedirect("/dash");
        if (roles.contains("ROLE_OFFICER"))
            response.sendRedirect("/person");

    }
}

package com.erp.config;

import com.erp.constant.enums.CommonStatus;
import com.erp.dto.UserDTO;
import com.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserDTO userDTO = userService.getUserDTOByUserName(userName);
        if (userDTO != null && userDTO.getCommonStatus() != CommonStatus.INACTIVE) {

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + userDTO.getRole());
            return new User(userDTO.getUserName(), userDTO.getPassword(), grantedAuthorities);
        }
        throw new UsernameNotFoundException("Username: " + userName + " not found");
    }
}

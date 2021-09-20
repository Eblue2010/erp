package io.eliteblue.erp.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ErpUserDetailsService implements UserDetailsService {

    private final static String USER_NOT_FOUND = "user with username %s not found";

    @Autowired
    private ErpUserService erpUserService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails details = erpUserService.getUserDetails(username);

        if(details == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND, username));
        }

        return details;
    }
}

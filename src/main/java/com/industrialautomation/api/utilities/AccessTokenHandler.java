package com.industrialautomation.api.utilities;

import com.industrialautomation.api.dao.UserRepository;
import com.industrialautomation.api.model.User;
import com.industrialautomation.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
public class AccessTokenHandler {
    @Autowired
    private UserRepository userRepository;

    public User getUserByPrincipal(Principal userPrincipal){
        String email = getEmailByPrincipal(userPrincipal);
        return  userRepository.loadUserByEmail(email);
    }
    public String getEmailByPrincipal(Principal principal){
        if(principal == null)
            return null;
        return principal.getName() == null ? null : principal.getName();

    }


}
package com.industrialautomation.api.service;

import com.industrialautomation.api.dao.UserRepository;
import com.industrialautomation.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.LinkedList;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.loadUserByEmail(username);

        if (user == null)
            throw  new UsernameNotFoundException(username);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
                Arrays.asList(grantedAuthority));


    }


    public Object addUser(String name, String email, String role){
        try{
            User user = new User();
            user.setName(name);
            user.setRole(role);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("abcd"));

            userRepository.save(user);
            return new LinkedList<String>(){{
                add("Successfully");
            }};
        } catch (Exception e){
            return new LinkedList<String>(){{
                add(e.getMessage());
            }};
        }



    }
}

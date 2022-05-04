package com.industrialautomation.api.service;

import com.industrialautomation.api.dao.AuthRepository;
import com.industrialautomation.api.dao.UserRepository;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.Auth;
import com.industrialautomation.api.model.User;
import com.industrialautomation.api.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.Locale;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private EntityManager em;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.loadUserByEmail(username);

        if (user == null)
            throw  new UsernameNotFoundException(username);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserType().getSlug().toUpperCase(Locale.ROOT));

        return new org.springframework.security.core.userdetails.User(username, user.getAuthDetails().getPassword(),
                Arrays.asList(grantedAuthority));

    }


    public Object addUser(String first_name, String last_name, String email, String contact_no,
                          String nic, Long type_id){
        try{

            User validate_user = userRepository.loadUserByEmailOrNic(email,nic);

            if(validate_user!=null)
                return new DefaultResponseDTO(201, ResponseStatus.USER_ALREADY_EXISTS,"Email or NIC already exists.");

            User user = new User();

            user.setFirst_name(first_name);
            user.setLast_name(last_name);
            user.setEmail(email);
            user.setContact_no(contact_no);

            user.setAdded_at(LocalDateTime.now());
            user.setUserType(new UserType(type_id));

            Auth auth = new Auth();
            auth.setPassword(passwordEncoder.encode(nic));
            auth.setUser(user);

            authRepository.save(auth);
            return new DefaultResponseDTO(200, ResponseStatus.OK,"User successfully Added.");
        } catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"User add failed.");
        }

    }
}

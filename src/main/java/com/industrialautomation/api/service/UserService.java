package com.industrialautomation.api.service;

import com.industrialautomation.api.dao.AuthRepository;
import com.industrialautomation.api.dao.UserRepository;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.dto.user.UserDetailsDTO;
import com.industrialautomation.api.dto.user.UserEditDTO;
import com.industrialautomation.api.model.Auth;
import com.industrialautomation.api.model.Machine;
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
import javax.persistence.Query;

import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private EntityManager em;

     private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
            user.setNic(nic);

            user.setAdded_at(LocalDateTime.now());
            user.setUserType(new UserType(type_id));

            Auth auth = new Auth();
            auth.setPassword(encoder.encode(nic));
            auth.setUser(user);

            authRepository.save(auth);
            return new DefaultResponseDTO(200, ResponseStatus.OK,"User successfully Added.");
        } catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"User add failed.");
        }

    }

    public User findById(Long user_id) {
        return userRepository.findByUserId(user_id);
    }

    public Object getUserDetails(String  email) {
        User user = userRepository.loadUserByEmail(email);

        if(user == null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid User");

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched successfully.",new UserDetailsDTO(user));
    }

    public Object forcePasswordChange(String email) {
        User user = userRepository.loadUserByEmail(email);

        if(user == null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid User.");

        if (user.getForce_password_change_flag() ==null)
            return  new DefaultResponseDTO(200,ResponseStatus.FORCE_PASSWORD_CHANGE_PENDING,"Force password change pending.");

        return  new DefaultResponseDTO(200,ResponseStatus.VERIFIED_USER,"A verified user.");
    }

    public Object changePassword(String email, String new_password, String old_password) {
        User user = userRepository.loadUserByEmail(email);

        if(user == null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid User.");

        if (old_password.equals( new_password))
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Password is same as previous.");

        if (!encoder.matches(old_password, user.getAuthDetails().getPassword()))
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Old password doesn't match.");

        Auth auth = user.getAuthDetails();
        auth.setPassword(encoder.encode(new_password));


        authRepository.save(auth);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Password changed successfully.");

    }

    public Object firstChangePassword(String email, String new_password) {
        User user = userRepository.loadUserByEmail(email);

        if(user == null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid User.");

        if(user.getForce_password_change_flag() != null)
            return  new DefaultResponseDTO(201,ResponseStatus.VERIFIED_USER,"User already changed the password.");

        user.setForce_password_change_flag(LocalDateTime.now());

        Auth auth = user.getAuthDetails();
        auth.setPassword(encoder.encode(new_password));

        authRepository.save(auth);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Password changed successfully.");

    }

    public Object getAllUsers(Integer limit, Integer offset) {

        String s_q ="SELECT u FROM User u JOIN FETCH u.authDetails JOIN FETCH u.userType WHERE u.deleted IS NULL ";
        Query query = em.createQuery(s_q);
        if (limit!=null && offset != null) {
            query.setMaxResults(limit);
            query.setFirstResult(offset);
        }
        List<User> users = query.getResultList();
        List<UserDetailsDTO> userDetailsDTOS = new LinkedList<>();
        for( User u: users)
            userDetailsDTOS.add(new UserDetailsDTO(u));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched successfully.",userDetailsDTOS);

    }

    public Object editUserProfile(String email, String first_name,
                                  String last_name, String contact_no,String birthday) {

        User user = userRepository.loadUserByEmail(email);

        if(user == null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid User.");

        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setContact_no(contact_no);
        user.setBirthday(null);

        userRepository.save(user);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"User edit successfully.");
    }


    public Object deleteUser(Long user_id) {
        User user = userRepository.findByUserId(user_id);

        if(user == null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"Invalid User.");

        if(user.getDeleted()!= null)
            return  new DefaultResponseDTO(201,ResponseStatus.INVALID_USER,"User already deleted.");

        user.setDeleted(LocalDateTime.now());
        userRepository.save(user);
        return  new DefaultResponseDTO(200,ResponseStatus.OK,"User successfully deleted.");
    }

    public Object checkUnique(String slug) {

        User user = userRepository.loadUserByEmailNic(slug);

        if (user==null)
            return new DefaultResponseDTO(200,ResponseStatus.OK,"User details is unique.");

        return new DefaultResponseDTO(201,ResponseStatus.ALREADY_EXISTS,"Already exists");
    }
}

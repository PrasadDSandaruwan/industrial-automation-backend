package com.industrialautomation.api.seed;


import com.industrialautomation.api.dao.AuthRepository;
import com.industrialautomation.api.dao.UserRepository;
import com.industrialautomation.api.dao.UserTypeRepository;
import com.industrialautomation.api.model.Auth;
import com.industrialautomation.api.model.User;
import com.industrialautomation.api.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
public class Seed implements CommandLineRunner {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthRepository authRepository;

    @Override
    public void run(String... args) throws Exception {
        addUserTypes();
        addUsers();
    }

    private void addUserTypes(){
        if(userTypeRepository.count()==0){
            String [] type = {"Admin","User"};
            String [] slug = {"ADMIN","USER"};

            for (int i=0;i<type.length;i++){
                UserType userType = new UserType();
                userType.setUser_type_name(type[i]);
                userType.setSlug(slug[i]);
                userTypeRepository.save(userType);
            }
        }
    }

    private void addUsers(){
        if( userRepository.count()==0) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String[] fName = {"Admin", "Admin", "Staff", "Staff", "User", "User", "Manager"};

            String[] lName = {"Rathnayake", "Mudiyanshe", "Gunawardhana", "Gnanathilaka", "Somathilaka", "Karunarathna", "Dasun"};

            String[] email = {"admin1@gmail.com", "admin2@gmail.com", "admin3@gmail.com", "admin4@gmail.com", "user1@gmail.com", "user2@gmail.com", "user4@gmail.com"};

            String[] nic = {"9812234234v", "1291281273v", "1219968826v", "997388162v", "123456765v", "12456765v", "124587V"};

            String[] contacts = {"0771213661", "0917768864", "0779875434", "0917654876", "3456789976", "3456789976", "074587941"};

            String[] password = {"Industry@Admin1", "Industry@Admin2", "Industry@Staff1", "Industry@Staff2", "Industry@User1", "Industry@User2", "Industry@Manager1"};

            Long[] typeID = {1l, 1l, 1l, 1l, 2l, 2l, 2l};
            for (int i = 0; i < fName.length; i++) {
                User user = new User();
                user.setFirst_name(fName[i]);
                user.setLast_name(lName[i]);
                user.setEmail(email[i]);
                user.setNic(nic[i]);
                user.setBirthday(LocalDate.now());
                user.setContact_no(contacts[i]);
                user.setForce_password_change_flag(LocalDateTime.now());

                UserType userType = new UserType();
                userType.setId(typeID[i]);
                user.setUserType(userType);
                user.setAdded_at(LocalDateTime.now());


                Auth auth = new Auth();
                auth.setPassword(encoder.encode(password[i]));
                user.setAuthDetails(auth);
                auth.setUser(user);
                authRepository.save(auth);

            }

        }
    }

}

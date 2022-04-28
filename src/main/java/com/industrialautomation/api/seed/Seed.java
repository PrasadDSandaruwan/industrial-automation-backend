package com.industrialautomation.api.seed;


import com.industrialautomation.api.dao.UserTypeRepository;
import com.industrialautomation.api.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Seed implements CommandLineRunner {

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        addUserTypes();
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
}

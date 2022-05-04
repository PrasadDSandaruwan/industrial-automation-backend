package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.authDetails JOIN FETCH u.userType WHERE u.email= ?1")
    User loadUserByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.authDetails WHERE  u.email=?1 OR u.nic =?2")
    User loadUserByEmailOrNic(String email,String nic);

}

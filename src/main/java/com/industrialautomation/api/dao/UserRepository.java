package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    @Query("SELECT u from  User  u where u.email= ?1")
    User loadUserByEmail(String email);


}

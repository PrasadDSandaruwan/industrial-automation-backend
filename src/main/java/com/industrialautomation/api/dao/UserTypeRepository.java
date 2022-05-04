package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Auth;
import com.industrialautomation.api.model.UserType;
import org.springframework.data.repository.CrudRepository;

public interface UserTypeRepository extends CrudRepository<UserType,Long> {
}

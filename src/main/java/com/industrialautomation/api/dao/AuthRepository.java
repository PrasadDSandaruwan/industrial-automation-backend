package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Auth;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<Auth,Long> {
}

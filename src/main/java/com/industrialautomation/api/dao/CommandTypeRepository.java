package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.CommandType;
import org.springframework.data.repository.CrudRepository;

public interface CommandTypeRepository extends CrudRepository<CommandType,Long> {

    CommandType getCommandTypeById(Long id);

}

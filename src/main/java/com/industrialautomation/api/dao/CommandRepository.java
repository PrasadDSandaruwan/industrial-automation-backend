package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Command;
import org.springframework.data.repository.CrudRepository;

public interface CommandRepository  extends CrudRepository<Command,Long> {
}

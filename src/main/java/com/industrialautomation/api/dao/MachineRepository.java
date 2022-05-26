package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Machine;
import org.springframework.data.repository.CrudRepository;

public interface MachineRepository extends CrudRepository<Machine,Long> {

    Machine getMachineById(long id);
}

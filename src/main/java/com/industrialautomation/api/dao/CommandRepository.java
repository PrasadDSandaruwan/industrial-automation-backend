package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Command;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommandRepository  extends CrudRepository<Command,Long> {

    @Query("SELECT c FROM  Command  c WHERE c.machine.id = ?1 and  c.commandType.id = ?2")
    Command getByMachineIdAndCommandType(Long machine_id, Long command_type_id);
}

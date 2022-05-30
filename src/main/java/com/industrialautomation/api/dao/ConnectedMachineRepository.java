package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.ConnectedMachine;
import com.industrialautomation.api.model.ConnectedMachineKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConnectedMachineRepository extends CrudRepository<ConnectedMachine, ConnectedMachineKey> {


    @Query("SELECT m FROM ConnectedMachine m WHERE m.id=?1 OR m.id=?2  ")
    ConnectedMachine getByDoubleId(ConnectedMachineKey orederd, ConnectedMachineKey reve);

    ConnectedMachine getConnectedMachineById(ConnectedMachineKey key);
}

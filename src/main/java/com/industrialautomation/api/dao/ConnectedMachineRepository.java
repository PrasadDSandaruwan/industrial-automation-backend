package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.ConnectedMachine;
import com.industrialautomation.api.model.ConnectedMachineKey;
import org.springframework.data.repository.CrudRepository;

public interface ConnectedMachineRepository extends CrudRepository<ConnectedMachine, ConnectedMachineKey> {

    ConnectedMachine getConnectedMachineById(ConnectedMachineKey key);
}

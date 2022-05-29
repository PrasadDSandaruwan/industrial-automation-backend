package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Machine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MachineRepository extends CrudRepository<Machine,Long> {

    Machine getMachineById(long id);

    Machine getMachineBySlug(String slug);

    @Query("SELECT m FROM Machine m JOIN FETCH m.machineType JOIN FETCH m.productionLine WHERE m.deleted IS NULL")
    List<Machine> getAllMachines();

    @Query("SELECT m FROM Machine m JOIN FETCH m.machineType JOIN FETCH m.productionLine WHERE m.deleted IS NULL AND m.id=?1")
    Machine getMachineDetails(Long id);


    @Query("SELECT  m FROM  Machine m JOIN FETCH m.productionLine WHERE m.productionLine.id in (SELECT u.productionLine.id FROM  Machine u WHERE u.id=?1) AND m.deleted IS NULL")
    List<Machine> getPossibleMachines(Long id);
}

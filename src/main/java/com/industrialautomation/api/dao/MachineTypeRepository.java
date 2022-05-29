package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.MachineType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MachineTypeRepository  extends CrudRepository<MachineType,Long> {

    MachineType getMachineTypeBySlug(String slug);
    MachineType getMachineTypeById(Long id);

    @Query("SELECT u FROM MachineType u")
    List<MachineType> getAll();
}

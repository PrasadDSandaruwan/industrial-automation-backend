package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.MachineType;
import org.springframework.data.repository.CrudRepository;

public interface MachineTypeRepository  extends CrudRepository<MachineType,Long> {
}

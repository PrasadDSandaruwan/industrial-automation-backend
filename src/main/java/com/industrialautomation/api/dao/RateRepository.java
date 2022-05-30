package com.industrialautomation.api.dao;


import com.industrialautomation.api.model.Rates;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RateRepository  extends CrudRepository<Rates,Long> {


    @Query("SELECT  m FROM  Rates  m WHERE m.machine.id =?1 AND m.is_temp = ?2")
    List<Rates> getRatesByMachineAndIs_temp(Long machine_id,Integer is_temp);

}

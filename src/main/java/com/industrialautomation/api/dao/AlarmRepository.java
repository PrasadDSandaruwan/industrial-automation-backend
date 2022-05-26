package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.Alarm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlarmRepository extends CrudRepository<Alarm,Long> {

    Alarm getAlarmBySlug(String slug);

    Alarm getAlarmById(Long id);

    List<Alarm> getAlarmsByDeletedIsNull();

    @Query("SELECT a FROM Alarm a JOIN FETCH a.machine WHERE a.deleted IS NULL ")
    List<Alarm> getAlarmsByDeletedIsNullWithMachine();

    @Query("SELECT  a FROM  Alarm  a JOIN FETCH a.machine WHERE a.id=?1")
    Alarm getAlarmsByIdWithMachine(Long id);


}

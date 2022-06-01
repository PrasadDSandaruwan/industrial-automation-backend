package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dao.RateRepository;
import com.industrialautomation.api.dto.admin.MachineDetailsDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.dto.user.ChangeRateDTO;
import com.industrialautomation.api.model.Machine;
import com.industrialautomation.api.model.Rates;
import com.industrialautomation.api.utilities.HandleChangeOfRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RateService {


    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private MachineRepository machineRepository;


    @Autowired
    private EntityManager em;

    @Autowired
    private HandleChangeOfRate handleChangeOfRate;

    public Object getRates(Long machine_id, Integer is_temp) {

        Machine machine = machineRepository.getMachineDetails(machine_id);

        if(machine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine exists.");

        if( (is_temp!= 0) && (is_temp!=1))
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid value for temperature test.");

        String s = "SELECT  m FROM  Rates  m WHERE m.machine.id =:machine_id AND m.is_temp =:is_temp ORDER BY m.id DESC ";


        Query query = em.createQuery(s);
        query.setParameter("machine_id",machine_id);
        query.setParameter("is_temp",is_temp);

        query.setMaxResults(50);

        List<Rates> rates = query.getResultList();

        List<String> labels = new LinkedList<>();
        List<Float> data = new LinkedList<>();



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        for(int i = rates.size() -1 ; i>-1;i--){
            Rates rate = rates.get(i);
            labels.add( rate.getRate_at().format(formatter));
            data.add(rate.getRate());
        }

        Map<String,Object> results = new HashMap<String,Object>(){{
            put("labels",labels);
            put("data",data);
        }};


        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched successfully.",results);
    }

    @Transactional
    public Object addRates(Long machine_id, Integer is_temp, double rate) {

        Machine machine = machineRepository.getMachineDetails(machine_id);
        if(machine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine exists.");

        if( (is_temp!= 0) && (is_temp!=1))
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid value for temperature test.");

//        List<Machine> machines = machineRepository.getAllMachines();
//
//        List<Rates> rates = new LinkedList<>();


        Float f_rate = (float) rate;
//        for(Machine i : machines){
//            if (machine_id==i.getId()){
//
//                if(is_temp==1){
//                    machine.setCurrent_temp( f_rate);
//                }
//                else {
//                    machine.setCurrent_rate( f_rate);
//                }
//
//                machineRepository.save(machine);
//                Rates r = new Rates(null, LocalDateTime.now(), f_rate,is_temp,machine);
//                rates.add(r);
//            }else {
//                Rates r;
//                if(is_temp==1){
//                    if(i.getCurrent_temp() != null){
//                        r = new Rates(null,LocalDateTime.now(),i.getCurrent_temp(),is_temp,i);
//                        rates.add(r);
//                    }
//
//                }else {
//                    if (i.getCurrent_rate()!= null){
//                        r = new Rates(null,LocalDateTime.now(),i.getCurrent_rate(),is_temp,i);
//                        rates.add(r);
//                    }
//
//                }
//
//            }
//
//        }
//
//        rateRepository.saveAll(rates);
//
//        List<Machine> updated =(List<Machine>) handleChangeOfRate.handleChangeOfRate(machine,f_rate,is_temp);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Updated.", handleChangeOfRate.newHandleChaneOfRate(machine_id,f_rate,is_temp));
    }
}

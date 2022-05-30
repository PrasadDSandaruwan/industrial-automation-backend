package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.ConnectedMachineRepository;
import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dto.admin.MachineDetailsDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.ConnectedMachine;
import com.industrialautomation.api.model.ConnectedMachineKey;
import com.industrialautomation.api.model.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ConnectedMachineService {

    @Autowired
    private ConnectedMachineRepository connectedMachineRepository;

    @Autowired
    private MachineRepository machineRepository;


    @Transactional
    public Object editConnectedMachine(Long machine_id, Long connected_machine_id, double rate,double temp) {

        Machine machine = machineRepository.getMachineDetails(machine_id);
        Machine connected_machine = machineRepository.getMachineDetails(connected_machine_id);

        if(machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine exists.");

        if(connected_machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid connected machine id.");

        if(connected_machine.getProductionLine().getId()!= machine.getProductionLine().getId())
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Both machines not in same production Line.");

        if(machine_id==connected_machine_id)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Cannot have same id.");

        machine.setInt_tempe((float) temp);

        ConnectedMachineKey connectedMachineKey = new ConnectedMachineKey();
        connectedMachineKey.setConnected_machine_id(connected_machine_id);
        connectedMachineKey.setMachine_id(machine_id);

        ConnectedMachineKey rev = new ConnectedMachineKey();
        rev.setConnected_machine_id(machine_id);
        rev.setMachine_id(connected_machine_id);

        ConnectedMachine connectedMachine = connectedMachineRepository.getByDoubleId(connectedMachineKey, rev);
        Float f_rate = (float) rate;

        if(connectedMachine == null){
            connectedMachine = new ConnectedMachine();
            connectedMachine.setId(connectedMachineKey);

            connectedMachine.setRate(f_rate);
            connectedMachine.setMachine(machine);
            connectedMachine.setConnectedMachine(connected_machine);
            connectedMachine.setCurrent_rate(f_rate);

            float current_rate;
            if(machine.getInt_rate() ==null){
                current_rate=f_rate;
            }else {
                current_rate = machine.getInt_rate()+f_rate;
            }

            machine.setInt_rate(current_rate);
        }else {

            connectedMachine.setRate(f_rate);
            connectedMachine.setCurrent_rate(f_rate);
            connectedMachine.setMachine(machine);
            connectedMachine.setConnectedMachine(connected_machine);

            float cur_rate=  machine.getInt_rate();
            cur_rate = cur_rate - connectedMachine.getRate() + f_rate;
            machine.setInt_rate(cur_rate);
        }

        machineRepository.save(machine);
        connectedMachineRepository.save(connectedMachine);
        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");

    }

    public Object getPossibleMachines(Long id) {

        List<Machine> machines = machineRepository.getPossibleMachines(id);

        List<MachineDetailsDTO> machineDetailsDTOS = new LinkedList<>();

        for(Machine m: machines)
            machineDetailsDTOS.add(new MachineDetailsDTO(m));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched successfully.",machineDetailsDTOS);
    }

    public Object getRate(Long machine_id, Long connected_machine_id) {

        ConnectedMachineKey connectedMachineKey = new ConnectedMachineKey();
        connectedMachineKey.setConnected_machine_id(connected_machine_id);
        connectedMachineKey.setMachine_id(machine_id);

        ConnectedMachineKey rev = new ConnectedMachineKey();
        rev.setConnected_machine_id(machine_id);
        rev.setMachine_id(connected_machine_id);

        ConnectedMachine connectedMachine = connectedMachineRepository.getByDoubleId(connectedMachineKey,rev);

        Machine machine = machineRepository.getMachineDetails(machine_id);
        if(machine==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Machine details Not exists");

        Map<String,Float> results = new HashMap<>();

        if(connectedMachine == null) {
            if(machine.getInt_tempe()==null){
                results.put("temp",0F);
                results.put("rate",0F);
            }else {
                results.put("temp",machine.getInt_tempe());
                results.put("rate",0F);
            }

            return new DefaultResponseDTO(200, ResponseStatus.OK, "Data Fetched successfully.",results);
        }else {

            if(machine.getInt_tempe()==null){
                results.put("temp",0F);
                results.put("rate",connectedMachine.getRate());
            }else {
                results.put("temp",machine.getInt_tempe());
                results.put("rate",connectedMachine.getRate());
            }

        }

        return new DefaultResponseDTO(200,ResponseStatus.OK,"data fetched successfully.",results);

    }
}

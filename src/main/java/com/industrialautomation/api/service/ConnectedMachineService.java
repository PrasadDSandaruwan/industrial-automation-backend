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

import java.util.LinkedList;
import java.util.List;

@Service
public class ConnectedMachineService {

    @Autowired
    private ConnectedMachineRepository connectedMachineRepository;

    @Autowired
    private MachineRepository machineRepository;


    public Object addConnectedMachine(Long machine_id, Long connected_machine_id, double rate) {

        Machine machine =machineRepository.getMachineDetails(machine_id);
        Machine connected_machine = machineRepository.getMachineDetails(connected_machine_id);

        if(machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine exists.");

        if(connected_machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid connected machine id.");

        if(connected_machine.getProductionLine().getId()!= machine.getProductionLine().getId())
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Both machines not in same production Line.");



        ConnectedMachineKey connectedMachineKey = new ConnectedMachineKey();
        connectedMachineKey.setConnected_machine_id(connected_machine_id);
        connectedMachineKey.setMachine_id(machine_id);


        ConnectedMachine connectedMachine = connectedMachineRepository.getConnectedMachineById(connectedMachineKey);

        if(connectedMachine != null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Details already exists");

        connectedMachine = new ConnectedMachine();
        connectedMachine.setId(connectedMachineKey);
        Float f_rate = (float) rate;
        connectedMachine.setRate(f_rate);
        connectedMachine.setMachine(machine);
        connectedMachine.setConnectedMachine(connected_machine);

        connectedMachine.setCurrent_rate(f_rate);

        connectedMachineRepository.save(connectedMachine);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");

    }

    public Object editConnectedMachine(Long machine_id, Long connected_machine_id, double rate) {

        Machine machine =machineRepository.getMachineDetails(machine_id);
        Machine connected_machine = machineRepository.getMachineDetails(connected_machine_id);

        if(machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine exists.");

        if(connected_machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid connected machine id.");

        if(connected_machine.getProductionLine().getId()!= machine.getProductionLine().getId())
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Both machines not in same production Line.");



        ConnectedMachineKey connectedMachineKey = new ConnectedMachineKey();
        connectedMachineKey.setConnected_machine_id(connected_machine_id);
        connectedMachineKey.setMachine_id(machine_id);


        ConnectedMachine connectedMachine = connectedMachineRepository.getConnectedMachineById(connectedMachineKey);

        if(connectedMachine == null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Machine details Not exists");

        Float f_rate = (float) rate;
        connectedMachine.setRate(f_rate);

        connectedMachine.setCurrent_rate(f_rate);

        connectedMachine.setMachine(machine);
        connectedMachine.setConnectedMachine(connected_machine);

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

        ConnectedMachine connectedMachine = connectedMachineRepository.getConnectedMachineById(connectedMachineKey);

        if(connectedMachine == null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Machine details Not exists");

        return new DefaultResponseDTO(200,ResponseStatus.OK,"data fetched successfully.",connectedMachine.getRate());

    }
}

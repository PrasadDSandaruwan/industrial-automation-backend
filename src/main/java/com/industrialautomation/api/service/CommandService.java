package com.industrialautomation.api.service;

import com.industrialautomation.api.dao.CommandRepository;
import com.industrialautomation.api.dao.CommandTypeRepository;
import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.Command;
import com.industrialautomation.api.model.CommandType;
import com.industrialautomation.api.model.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandTypeRepository commandTypeRepository;

    @Autowired
    private MachineRepository machineRepository;


    public Object addCommand(String command, Long command_type_id, Long machine_id) {
        CommandType commandType = commandTypeRepository.getCommandTypeById(command_type_id);
        Command check = commandRepository.getByMachineIdAndCommandType(machine_id,command_type_id);

        if(check!=null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Command type for given machine already exists.");

        if(commandType==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such command type.");

        Machine machine = machineRepository.getMachineById(machine_id);

        if(machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine.");

        Command c = new Command();

        c.setCommand(command);
        c.setCommandType(commandType);
        c.setMachine(machine);

        commandRepository.save(c);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully added.");
    }

    public Object editCommand(String command, Long command_type_id, Long machine_id, Long id) {

        CommandType commandType = commandTypeRepository.getCommandTypeById(command_type_id);

        if(commandType==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such command type.");

        Machine machine = machineRepository.getMachineById(machine_id);

        if(machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such machine.");

        Command c = new Command();

        c.setCommand(command);
        c.setCommandType(commandType);
        c.setMachine(machine);

        commandRepository.save(c);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully added.");
    }
}

package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.MachineTypeRepository;
import com.industrialautomation.api.dto.admin.MachineTypeDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.MachineType;
import com.industrialautomation.api.model.ProductionLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MachineTypeService {

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    public Object addProductionLine(String machine_type_name, String slug) {
        MachineType machineType = machineTypeRepository.getMachineTypeBySlug(slug);

        if(machineType!=null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");

        machineType = new MachineType();
        machineType.setMachine_type_name(machine_type_name);
        machineType.setSlug(slug);
        machineTypeRepository.save(machineType);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");
    }

    public Object getAll() {

        List<MachineType> machineTypes = machineTypeRepository.getAll();

        List<MachineTypeDTO> machineTypeDTOS = new LinkedList<>();

        for(MachineType m: machineTypes)
            machineTypeDTOS.add(new MachineTypeDTO(m));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data fetched Successfully.",machineTypeDTOS);
    }
}

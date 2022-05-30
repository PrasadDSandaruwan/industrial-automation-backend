package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.MachineRepository;
import com.industrialautomation.api.dao.MachineTypeRepository;
import com.industrialautomation.api.dao.ProductionLineRepository;
import com.industrialautomation.api.dto.admin.MachineDetailsDTO;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.Machine;
import com.industrialautomation.api.model.MachineType;
import com.industrialautomation.api.model.ProductionLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private ProductionLineRepository productionLineRepository;

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private EntityManager em;


    public Object addMachine(String name, String slug, String license_number, Integer is_automated, Long production_line_id, Long machine_type_id ) {

        Machine machine = machineRepository.getMachineBySlug(slug);
        if(machine!=null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");

        ProductionLine productionLine = productionLineRepository.getProductionLineById(production_line_id);
        if(productionLine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such production line");

        MachineType machineType = machineTypeRepository.getMachineTypeById(machine_type_id);
        if(machineType == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such Machine Type.");


        if(is_automated!=1 && is_automated!=0)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Automated can be zero or one");


        machine = new Machine(null,name,slug,license_number, LocalDateTime.now(),is_automated,null,0F,0F, 0F, 0F,productionLine,machineType);
        machineRepository.save(machine);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");

    }

    public Object edit(String name, String slug, String license_number, Integer is_automated, Long production_line_id, Long machine_type_id , Long id) {
        Machine machine = machineRepository.getMachineBySlug(slug);
        if(machine!=null){
            if(machine.getId()!=id)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");
        }
        machine = machineRepository.getMachineById(id);

        if(machine==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"No Such Machine Id.");

        ProductionLine productionLine = productionLineRepository.getProductionLineById(production_line_id);
        if(productionLine == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such production line");

        MachineType machineType = machineTypeRepository.getMachineTypeById(machine_type_id);
        if(machineType == null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"No such Machine Type.");


        if(is_automated!=1 && is_automated !=0)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Automated can be zero or one");


        machine.setName(name);
        machine.setSlug(slug);
        machine.setLicense_number(license_number);
        machine.setIs_automated(is_automated);
        machine.setProductionLine(productionLine);
        machine.setMachineType(machineType);
        machineRepository.save(machine);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");


    }

    public Object deleteMachine(Long id) {

        Machine machine = machineRepository.getMachineById(id);

        if(machine== null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid Machine Id.");

        machine.setDeleted(LocalDateTime.now());
        machineRepository.save(machine);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Deleted.");


    }

    public Object getAll() {
        List<Machine> machines = machineRepository.getAllMachines();
        List<MachineDetailsDTO> machineDetailsDTOS = new LinkedList<>();
        for (Machine m: machines)
            machineDetailsDTOS.add( new MachineDetailsDTO(m));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data Fetched Successfully.",machineDetailsDTOS);

    }

    public Object getMachineDetails(Long id) {

        Machine machine = machineRepository.getMachineDetails(id);
        if(machine==null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Invalid Machine Id.");
        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data Fetched Successfully.",new MachineDetailsDTO(machine));
    }

    public Object getAllByType(String type) {

        if((!type.equals("INGRE")) && (!type.equals("PROD")) && (!type.equals("PACK")) )
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"No such machine type");


        String q = "SELECT  m FROM Machine m JOIN FETCH m.machineType JOIN FETCH m.productionLine WHERE m.deleted IS NULL AND  ";

        if(type.equals("INGRE"))
            q+= " m.machineType.slug='ingredient' ";
        if(type.equals("PROD"))
            q+= " m.machineType.slug='production' ";
        if(type.equals("PACK"))
            q+= " m.machineType.slug='packing' ";
        Query query = em.createQuery(q);

        List<Machine> machines = query.getResultList();

        List<MachineDetailsDTO> machineDetailsDTOS = new LinkedList<>();
        for (Machine m: machines)
            machineDetailsDTOS.add( new MachineDetailsDTO(m));

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Data Fetched Successfully.",machineDetailsDTOS);

    }
}

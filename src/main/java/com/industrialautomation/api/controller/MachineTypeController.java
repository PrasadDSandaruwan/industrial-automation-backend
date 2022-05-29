package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.MachineService;
import com.industrialautomation.api.service.MachineTypeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachineTypeController {

    @Autowired
    private MachineTypeService machineTypeService;


    @PostMapping("/v1/machine-type/add")
    public Object addMachineType(@RequestBody JsonNode jsonNode){
        try {
            String machine_type_name = (jsonNode.hasNonNull("machine_type_name")) ? jsonNode.get("machine_type_name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;

            if(machine_type_name==null || slug==null )
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return machineTypeService.addProductionLine(machine_type_name,slug);

        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @GetMapping("/v1/machine-type/all")
    public Object getAll(){
        try {
            return machineTypeService.getAll();
        } catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }
}

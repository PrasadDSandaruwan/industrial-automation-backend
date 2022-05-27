package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MachineController {

    @Autowired
    private MachineService machineService;


    @PostMapping("/v1/machine/add")
    public Object addMachine(@RequestBody JsonNode jsonNode){

        try {
            String name = (jsonNode.hasNonNull("name")) ? jsonNode.get("name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;
            String license_number = (jsonNode.hasNonNull("license_number")) ? jsonNode.get("license_number").asText(): null;
            Integer is_automated = (jsonNode.hasNonNull("is_automated")) ? jsonNode.get("is_automated").asInt(): null;
            Long production_line_id = (jsonNode.hasNonNull("production_line_id")) ? jsonNode.get("production_line_id").asLong(): null;
            Long machine_type_id = (jsonNode.hasNonNull("machine_type_id")) ? jsonNode.get("machine_type_id").asLong(): null;

            if (name ==null || slug==null ||license_number==null || is_automated==null || production_line_id ==null || machine_type_id==null)
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return machineService.addMachine(name, slug, license_number,is_automated,production_line_id,machine_type_id);

        }

        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }


    @PostMapping("/v1/machine/edit/{id}")
    public Object  editMachine(@RequestBody JsonNode jsonNode, @PathVariable Long id){
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Machine Id missing");

        try {
            String name = (jsonNode.hasNonNull("name")) ? jsonNode.get("name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;
            String license_number = (jsonNode.hasNonNull("license_number")) ? jsonNode.get("license_number").asText(): null;
            Integer is_automated = (jsonNode.hasNonNull("is_automated")) ? jsonNode.get("is_automated").asInt(): null;
            Long production_line_id = (jsonNode.hasNonNull("production_line_id")) ? jsonNode.get("production_line_id").asLong(): null;
            Long machine_type_id = (jsonNode.hasNonNull("machine_type_id")) ? jsonNode.get("machine_type_id").asLong(): null;

            if (name ==null || slug==null ||license_number==null || is_automated==null || production_line_id ==null || machine_type_id==null)
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return machineService.edit(name, slug, license_number,is_automated,production_line_id,machine_type_id,id);
        }

        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }


    @DeleteMapping("/v1/machine/delete/{id}")
    public Object deleteMachine(@PathVariable Long id){
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Machine Id missing");

        try {
            return machineService.deleteMachine(id);
        }

        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }


    @GetMapping("/v1/machine/all")
    public Object getAll(){
        try {
            return machineService.getAll();
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @GetMapping("/v1/machine/details/{id}")
    public Object getMachineDetails(@PathVariable Long id){
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Machine Id missing");
        try {
            return machineService.getMachineDetails(id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }
}

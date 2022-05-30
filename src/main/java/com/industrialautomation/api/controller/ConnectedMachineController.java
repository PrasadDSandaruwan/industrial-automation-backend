package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.ConnectedMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConnectedMachineController {

    @Autowired
    private ConnectedMachineService connectedMachineService;


    @PostMapping("/v1/connected-machine/edit")
    public Object editConnectedMachine(@RequestBody JsonNode jsonNode){
        try {
            Long machine_id = (jsonNode.hasNonNull("machine_id")) ? jsonNode.get("machine_id").asLong(): null;
            Long connected_machine_id = (jsonNode.hasNonNull("connected_machine_id")) ? jsonNode.get("connected_machine_id").asLong(): null;
            Double rate = (jsonNode.hasNonNull("rate")) ? jsonNode.get("rate").asDouble(): null;
            Double temp = (jsonNode.hasNonNull("temp")) ? jsonNode.get("temp").asDouble(): null;


            if(machine_id==null || connected_machine_id==null || rate==null|| temp==null)
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return connectedMachineService.editConnectedMachine(machine_id,connected_machine_id,rate,temp);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }


    @GetMapping("/v1/connected-machine/possible/{id}")
    public Object getPossibleConnection(@PathVariable Long id){

        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Notification Id missing");

        try {
            return connectedMachineService.getPossibleMachines(id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }

    }

    @PostMapping("/v1/connected-machine/rate")
    public Object getRate(@RequestBody JsonNode jsonNode){
        try {
            Long machine_id = (jsonNode.hasNonNull("machine_id")) ? jsonNode.get("machine_id").asLong(): null;
            Long connected_machine_id = (jsonNode.hasNonNull("connected_machine_id")) ? jsonNode.get("connected_machine_id").asLong(): null;
            if(machine_id==null || connected_machine_id==null)
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");
            return connectedMachineService.getRate(machine_id,connected_machine_id);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

}

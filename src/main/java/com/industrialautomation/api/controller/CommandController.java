package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandController {

    @Autowired
    private CommandService commandService;

    @PostMapping("/v1/command/add")
    public Object addCommand(@RequestBody JsonNode jsonNode) {

        try {
            String command = (jsonNode.hasNonNull("command")) ? jsonNode.get("command").asText() : null;
            Long command_type_id = (jsonNode.hasNonNull("command_type_id")) ? jsonNode.get("command_type_id").asLong() : null;
            Long machine_id = (jsonNode.hasNonNull("machine_id")) ? jsonNode.get("machine_id").asLong() : null;

            if (command == null || command_type_id == null || machine_id == null)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "Some inputs are missing.");
            return commandService.addCommand(command, command_type_id, machine_id);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED, "Operation failed.");
        }

    }

    @PostMapping("/v1/command/edit/{id}")
    public Object editCommand(@RequestBody JsonNode jsonNode, @PathVariable Long id) {
        if(id==null)
            return new DefaultResponseDTO(201,ResponseStatus.INVALID_INPUTS,"Command Id missing");

        try {
            String command = (jsonNode.hasNonNull("command")) ? jsonNode.get("command").asText() : null;
            Long command_type_id = (jsonNode.hasNonNull("command_type_id")) ? jsonNode.get("command_type_id").asLong() : null;
            Long machine_id = (jsonNode.hasNonNull("machine_id")) ? jsonNode.get("machine_id").asLong() : null;

            if (command == null || command_type_id == null || machine_id == null)
                return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS, "Some inputs are missing.");
            return commandService.editCommand(command, command_type_id, machine_id,id);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED, "Operation failed.");
        }

    }
}

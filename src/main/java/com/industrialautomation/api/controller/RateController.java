package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateController {

    @Autowired
    private RateService rateService;

    @PostMapping("/v1/rates/get-rates")
    public Object getRates(@RequestBody JsonNode jsonNode){
        try {

            Long machine_id = (jsonNode.hasNonNull("machine_id")) ? jsonNode.get("machine_id").asLong(): null;
            Integer is_temp = (jsonNode.hasNonNull("is_temp")) ? jsonNode.get("is_temp").asInt(): null;

            if(machine_id==null || is_temp==null)
                return new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");
            return rateService.getRates(machine_id,is_temp);
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }

    @PostMapping("/v1/rates/add")
    public Object addRates(@RequestBody JsonNode jsonNode){
        try {

            Long machine_id = (jsonNode.hasNonNull("machine_id")) ? jsonNode.get("machine_id").asLong(): null;
            Integer is_temp = (jsonNode.hasNonNull("is_temp")) ? jsonNode.get("is_temp").asInt(): null;
            Double rate = (jsonNode.hasNonNull("rate")) ? jsonNode.get("rate").asDouble(): null;

            if(machine_id==null || is_temp==null || rate==null)
                return new DefaultResponseDTO(201,ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");
            return rateService.addRates(machine_id,is_temp,rate );
        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }
}

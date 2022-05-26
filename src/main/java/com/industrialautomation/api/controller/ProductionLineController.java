package com.industrialautomation.api.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.service.ProductionLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductionLineController {

    @Autowired
    private ProductionLineService productionLineService;

    @PostMapping("/v1/production-line/add")
    public Object addProductionLine(@RequestBody JsonNode jsonNode){
        try {
            String production_line_name = (jsonNode.hasNonNull("production_line_name")) ? jsonNode.get("production_line_name").asText(): null;
            String slug = (jsonNode.hasNonNull("slug")) ? jsonNode.get("slug").asText(): null;

            if(production_line_name==null || slug==null )
                return new DefaultResponseDTO(201, ResponseStatus.MISSING_INPUTS,"Some inputs are missing.");

            return productionLineService.addProductionLine(production_line_name,slug);

        }
        catch (Exception e){
            e.printStackTrace();
            return new DefaultResponseDTO(201, ResponseStatus.FAILED,"Operation failed.");
        }
    }
}

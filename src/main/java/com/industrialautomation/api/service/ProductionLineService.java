package com.industrialautomation.api.service;


import com.industrialautomation.api.dao.ProductionLineRepository;
import com.industrialautomation.api.dto.response.DefaultResponseDTO;
import com.industrialautomation.api.dto.response.ResponseStatus;
import com.industrialautomation.api.model.ProductionLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionLineService {

    @Autowired
    private ProductionLineRepository productionLineRepository;
    public Object addProductionLine(String production_line_name, String slug) {

        ProductionLine productionLine = productionLineRepository.getProductionLineBySlug(slug);

        if(productionLine!=null)
            return new DefaultResponseDTO(201, ResponseStatus.INVALID_INPUTS,"Slug is not unique");

        productionLine = new ProductionLine();
        productionLine.setLine_name(production_line_name);
        productionLine.setSlug(slug);
        productionLineRepository.save(productionLine);

        return new DefaultResponseDTO(200,ResponseStatus.OK,"Successfully Added.");
    }
}

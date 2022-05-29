package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.ProductionLine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductionLineRepository extends CrudRepository<ProductionLine,Long> {

    ProductionLine getProductionLineBySlug(String slug);

    ProductionLine getProductionLineById(Long id);

    @Query("SELECT u FROM ProductionLine u")
    List<ProductionLine> getAll();
}

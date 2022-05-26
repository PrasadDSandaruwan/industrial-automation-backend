package com.industrialautomation.api.dao;

import com.industrialautomation.api.model.ProductionLine;
import org.springframework.data.repository.CrudRepository;

public interface ProductionLineRepository extends CrudRepository<ProductionLine,Long> {
}

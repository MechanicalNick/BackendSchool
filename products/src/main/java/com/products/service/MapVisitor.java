package com.products.service;

import com.products.ShopUnitMapper;
import com.products.data.ShopUnit;
import com.products.data.ShopUnitImport;
import com.products.data.ShopUnitStatisticUnit;
import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;
import org.springframework.stereotype.Service;

@Service
public class MapVisitor implements MapEntityVisitor {
    private final ShopUnitMapper mapper;

    public MapVisitor(ShopUnitMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public void visitCategoryEntry(ShopUnitImport item, CategoryEntity entity) {
        mapper.updateCategoryEntityFromDto(item, entity);
    }

    @Override
    public void visitOfferEntry(ShopUnitImport item, OfferEntity entity) {
        mapper.updateOfferEntityFromDto(item, entity);
    }

    @Override
    public void visitCategoryDto(CategoryEntity entity, ShopUnit item) {
        mapper.updateDtoFromCategoryEntity(entity, item);
        item.setDate(entity.getZonedDateTime());
    }

    @Override
    public void visitOfferDto(OfferEntity entity, ShopUnit item) {
        mapper.updateDtoFromOfferEntity(entity, item);
        item.setDate(entity.getZonedDateTime());
    }

    @Override
    public ShopUnitStatisticUnit visitOfferStatistics(OfferEntity entity, ShopUnitStatisticUnit shopUnitStatisticUnit) {
        mapper.updateStatisticsFromOfferEntity(entity, shopUnitStatisticUnit);
        shopUnitStatisticUnit.setDate(entity.getZonedDateTime());
        return shopUnitStatisticUnit;
    }
}

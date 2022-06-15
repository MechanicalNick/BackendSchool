package com.products;

import com.products.data.ShopUnit;
import com.products.data.ShopUnitImport;
import com.products.data.ShopUnitStatisticUnit;
import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShopUnitMapper {
    void updateCategoryEntityFromDto(ShopUnitImport dto, @MappingTarget CategoryEntity entity);
    void updateOfferEntityFromDto(ShopUnitImport dto, @MappingTarget OfferEntity entity);
    void updateDtoFromCategoryEntity(CategoryEntity entity, @MappingTarget ShopUnit dto);
    void updateDtoFromOfferEntity(OfferEntity entity, @MappingTarget ShopUnit dto);
    void updateStatisticsFromOfferEntity(OfferEntity entity, @MappingTarget ShopUnitStatisticUnit dto);
}

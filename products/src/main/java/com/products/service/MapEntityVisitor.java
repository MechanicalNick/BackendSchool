package com.products.service;

import com.products.data.ShopUnit;
import com.products.data.ShopUnitImport;
import com.products.data.ShopUnitStatisticUnit;
import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;

public interface MapEntityVisitor {
    void visitCategoryEntry(ShopUnitImport item, CategoryEntity entity);
    void visitOfferEntry(ShopUnitImport item, OfferEntity entity);
    void visitCategoryDto(CategoryEntity entity, ShopUnit item);
    void visitOfferDto(OfferEntity entity, ShopUnit item);
    ShopUnitStatisticUnit visitOfferStatistics(OfferEntity entity, ShopUnitStatisticUnit shopUnitStatisticUnit);
}

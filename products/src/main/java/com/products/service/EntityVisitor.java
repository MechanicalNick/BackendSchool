package com.products.service;

import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;

import java.util.Collection;

public interface EntityVisitor {
    void visitCategory(CategoryEntity entity);
    void visitOffer(OfferEntity entity);
    void visitAllCategories(Collection<CategoryEntity> entities);
    void visitAllOffers(Collection<OfferEntity> entities);
}

package com.products.service;

import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;
import com.products.repository.CategoryRepository;
import com.products.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SaveVisitor implements EntityVisitor {
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;

    public SaveVisitor(CategoryRepository categoryRepository, OfferRepository offerRepository) {
        this.categoryRepository = categoryRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    public void visitCategory(CategoryEntity entity) {
        categoryRepository.save(entity);
    }

    @Override
    public void visitOffer(OfferEntity entity) {
        offerRepository.save(entity);
    }

    @Override
    public void visitAllCategories(Collection<CategoryEntity> entities) {
        categoryRepository.saveAll(entities);
    }

    @Override
    public void visitAllOffers(Collection<OfferEntity> entities) {
        offerRepository.saveAll(entities);
    }
}

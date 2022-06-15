package com.products.service;

import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;
import com.products.repository.CategoryRepository;
import com.products.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class DeleteVisitor implements EntityVisitor {
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;

    public DeleteVisitor(CategoryRepository categoryRepository, OfferRepository offerRepository){
        this.categoryRepository = categoryRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    public void visitCategory(CategoryEntity entity) {
        var childCategories = categoryRepository.findChildCategoryRecursive(entity.getId());
        childCategories.add(entity);
        var idSet = childCategories.stream()
                .map(x -> x.getId())
                .collect(Collectors.toCollection(HashSet::new));
        var offers = offerRepository.findChildOffer(idSet.stream().toList());
        categoryRepository.deleteAll(childCategories);
        offerRepository.deleteAll(offers);
    }

    @Override
    public void visitOffer(OfferEntity entity) {
        offerRepository.delete(entity);
    }

    @Override
    public void visitAllCategories(Collection<CategoryEntity> entities) {
        categoryRepository.deleteAll(entities);
    }

    @Override
    public void visitAllOffers(Collection<OfferEntity> entities) {
        offerRepository.deleteAll(entities);
    }
}

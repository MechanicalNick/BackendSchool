package com.products.helper;

import com.products.data.ShopUnit;
import com.products.data.UnitType;
import com.products.entity.ShopUnitEntity;
import com.products.repository.CategoryRepository;
import com.products.repository.OfferRepository;
import com.products.service.MapVisitor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TreeBuilder {
    private final MapVisitor mapVisitor;
    private final CategoryRepository categoryRepository;
    private final OfferRepository offerRepository;

    public TreeBuilder(MapVisitor mapVisitor, CategoryRepository categoryRepository, OfferRepository offerRepository){
        this.mapVisitor = mapVisitor;
        this.categoryRepository = categoryRepository;
        this.offerRepository = offerRepository;
    }

    public ShopUnit build(ShopUnitEntity entity){
        var root= toData(entity);
        createTree(root);
        if(root.getType() == UnitType.CATEGORY)
            calculatePrice(root);
        return root;
    }

    private ShopUnit toData(ShopUnitEntity entity){
        var data = new ShopUnit();
        entity.accept(data, mapVisitor);
        return data;
    }

    private void createTree(ShopUnit root){
        if(root.getType() == UnitType.OFFER)
            return;

        var childCategory = categoryRepository.findChildCategoryRecursive(root.getId());
        var allCategory = Stream.concat(childCategory.stream().map(ShopUnitEntity::getId),
                Stream.of(root.getId())).toList();
        var childOffer = offerRepository.findChildOffer(allCategory);

        var allChildEntities = Stream.of(childCategory, childOffer)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        var map = new HashMap<UUID, ShopUnit>();
        map.put(root.getId(), root);
        for (var entity: allChildEntities) {
            var data = toData(entity);
            map.put(data.getId(), data);
        }

        for (var data: map.values()) {
            if(map.containsKey(data.getParentId())){
                var parent =  map.get(data.getParentId());
                if(parent.getChildren() == null)
                    parent.setChildren(new ArrayList<>());
                parent.getChildren().add(data);
            }
        }
    }

    private PriceResult calculatePrice(@NotNull ShopUnit root){
        var priceResult = new PriceResult(0,0);
        Long price = null;
        if(root.getChildren() != null) {
            for (var children : root.getChildren()) {
                if(children.getType() == UnitType.CATEGORY){
                    var result = calculatePrice(children);
                    priceResult.price += result.price;
                    priceResult.count += result.count;
                }
                else {
                    priceResult.count += 1;
                    priceResult.price += getValueOrZero(children.getPrice());
                }
            }
            if(priceResult.count > 0)
                price = (long) Math.floor(priceResult.price / priceResult.count);
        }
        root.setPrice(price);
        return new PriceResult(priceResult.price, priceResult.count);
    }

    private static long getValueOrZero(Long value){
        return value == null ? 0 : value;
    }

    final class PriceResult {
        private long price;
        private long count;

        public PriceResult(long price, long count) {
            this.price = price;
            this.count = count;
        }
    }
}

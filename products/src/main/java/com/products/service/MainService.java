package com.products.service;

import com.products.data.*;
import com.products.entity.CategoryEntity;
import com.products.entity.OfferEntity;
import com.products.entity.ShopUnitEntity;
import com.products.helper.TreeBuilder;
import com.products.repository.CategoryRepository;
import com.products.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MainService {
    public static final int DAYS_OF_SALES = 1;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final MapVisitor mapVisitor;
    private final SaveVisitor saveVisitor;
    private final TreeBuilder treeBuilder;
    private final DeleteVisitor deleteVisitor;

    public MainService(OfferRepository offerRepository, CategoryRepository categoryRepository,
                       MapVisitor mapVisitor, SaveVisitor saveVisitor, TreeBuilder treeBuilder,
                       DeleteVisitor deleteVisitor){
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.mapVisitor = mapVisitor;
        this.saveVisitor = saveVisitor;
        this.treeBuilder = treeBuilder;
        this.deleteVisitor = deleteVisitor;
    }

    public List<ShopUnitEntity> getAll() {
        return Stream.concat(offerRepository.findAll().stream(),
                        categoryRepository.findAll().stream())
                .toList();
    }

    public Optional<? extends ShopUnitEntity> find(UUID id) {
        var offer = offerRepository.findById(id);
        if(!offer.isEmpty())
            return Optional.of(offer.get());
        return categoryRepository.findById(id);
    }

    public ShopUnit createSubtree(ShopUnitEntity entity){
        return treeBuilder.build(entity);
    }

    public void updateDate(List<ShopUnitEntity> entities, ZonedDateTime date) {
        var hs = new HashSet<ShopUnitEntity>();
        hs.addAll(entities);
        var groups = entities.stream()
                .filter(x -> x.getParentId() != null)
                .collect(Collectors.groupingBy(ShopUnitEntity::getParentId));
        for (var group: groups.entrySet()){
            var categories = categoryRepository.findParentCategoryRecursive(group.getKey());
            hs.addAll(categories);
        }
        for (var entity: hs) {
            if(entity.getZonedDateTime() != date) {
                entity.setZonedDateTime(date);
                entity.accept(saveVisitor);
            }
        }
    }

    public ShopUnitStatisticResponse findAll(ZonedDateTime zonedDateTime) {
        var response = new ShopUnitStatisticResponse();
        var offers = offerRepository.findAll(zonedDateTime.minusDays(DAYS_OF_SALES), zonedDateTime);
        var statistics = offers.stream()
                .map(x -> mapVisitor.visitOfferStatistics(x, new ShopUnitStatisticUnit()))
                .toList();
        response.setItems(statistics);
        return response;
    }

    public boolean imports(ShopUnitImportRequest importRequest) {
        var dateTime = getZonedDateTime(importRequest.getUpdateDate());
        if(dateTime.isEmpty())
            return false;

        var zonedDateTime = dateTime.get();

        var idMap = new HashMap<UUID, ShopUnitImport>();
        for (var item: importRequest.getItems())
            idMap.put(item.getId(), item);

        var entities = new ArrayList<ShopUnitEntity>();
        for (var item: importRequest.getItems()) {
            var entity = getShopUnitEntity(item);
            var isTypeChanged = entity.getType() != null && entity.getType() != item.getType();
            if(isTypeChanged)
                return false;
            if(!checkParentType(item, idMap))
                return false;
            if(!checkCategoryPrice(item))
                return false;
            entities.add(entity);
            entity.accept(mapVisitor, item);
        }

        updateDate(entities, zonedDateTime);
        save(entities);
        return true;
    }

    public void delete(ShopUnitEntity entity) {
        entity.accept(deleteVisitor);
    }

    private Optional<ZonedDateTime> getZonedDateTime(String value) {
        try {
            return Optional.of(ZonedDateTime.parse(value));
        }
        catch (DateTimeParseException exception){
            return Optional.empty();
        }
    }

    private void save(List<ShopUnitEntity> entities) {
        var categories = entities.stream()
                .filter(x -> x instanceof CategoryEntity)
                .map(x -> (CategoryEntity) x)
                .toList();

        saveVisitor.visitAllCategories(categories);

        var offers =  entities.stream()
                .filter(x -> x instanceof OfferEntity)
                .map(p -> (OfferEntity) p)
                .toList();
        saveVisitor.visitAllOffers(offers);
    }

    private ShopUnitEntity getShopUnitEntity(ShopUnitImport item) {
        var entity = find(item.getId());
        ShopUnitEntity value;
        if(entity.isEmpty()) {
            value = item.getType() == UnitType.CATEGORY ? new CategoryEntity() : new OfferEntity();
        }
        else {
            value = entity.get();
        }
        return value;
    }

    private boolean checkCategoryPrice(ShopUnitImport item){
        return item.getType() != UnitType.CATEGORY || item.getPrice() == null;
    }

    private boolean checkParentType(ShopUnitImport item, HashMap<UUID, ShopUnitImport> idMap) {
        if(item.getParentId() == null)
            return  true;

        if(idMap.containsKey(item.getParentId())){
            var parent = idMap.get(item.getParentId());
            if (parent.getType() != UnitType.CATEGORY)
                return false;
        }
        var repoItem = find(item.getParentId());
        if(!repoItem.isEmpty()){
            if(repoItem.get().getType() != UnitType.CATEGORY)
                return false;
        }
        return true;
    }
}

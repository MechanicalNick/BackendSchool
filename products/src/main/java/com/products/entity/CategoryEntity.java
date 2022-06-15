package com.products.entity;


import com.products.data.ShopUnit;
import com.products.data.ShopUnitImport;
import com.products.service.EntityVisitor;
import com.products.service.MapEntityVisitor;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "category", schema = "public")
public class CategoryEntity extends ShopUnitEntity {
    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visitCategory(this);
    }

    @Override
    public void accept(MapEntityVisitor visitor, ShopUnitImport item) {
        visitor.visitCategoryEntry(item, this);
    }

    @Override
    public void accept(ShopUnit item, MapEntityVisitor visitor) { visitor.visitCategoryDto(this, item); }
}

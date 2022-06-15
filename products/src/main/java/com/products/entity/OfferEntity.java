package com.products.entity;

import com.products.data.ShopUnit;
import com.products.data.ShopUnitImport;
import com.products.service.EntityVisitor;
import com.products.service.MapEntityVisitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "offer", schema = "public")
public class OfferEntity extends ShopUnitEntity {
    @Column(name = "price")
    private long price;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visitOffer(this);
    }

    @Override
    public void accept(MapEntityVisitor visitor, ShopUnitImport item) {
        visitor.visitOfferEntry(item, this);
    }

    @Override
    public void accept(ShopUnit item, MapEntityVisitor visitor) {
        visitor.visitOfferDto(this, item);
    }
}

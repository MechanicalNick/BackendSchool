package com.products.entity;

import com.products.data.ShopUnit;
import com.products.data.ShopUnitImport;
import com.products.service.EntityVisitor;
import com.products.data.UnitType;
import com.products.service.MapEntityVisitor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;


@MappedSuperclass
public abstract class ShopUnitEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    @Column(name = "parentId")
    private UUID parentId;

    @NotNull
    @Column(name = "type")
    private UnitType type;

    @NotNull
    @Column(name = "date")
    private ZonedDateTime localDateTime;

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public ZonedDateTime getZonedDateTime() {
        return localDateTime;
    }

    public void setZonedDateTime(ZonedDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public abstract void accept(EntityVisitor visitor);

    public abstract void accept(MapEntityVisitor visitor, ShopUnitImport item);

    public abstract void accept(ShopUnit item, MapEntityVisitor visitor);
}

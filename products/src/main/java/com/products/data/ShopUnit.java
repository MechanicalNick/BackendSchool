package com.products.data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class ShopUnit {
    private UnitType type;
    private String name;
    private UUID id;
    private Long price;
    private UUID parentId;
    private ZonedDateTime date;
    private ArrayList<ShopUnit> children;

    public UnitType getType() {
        return type;
    }
    public void setType(UnitType type) {
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Long getPrice() {
        return price;
    }
    public void setPrice(Long price) {
        this.price = price;
    }
    public UUID getParentId() {
        return parentId;
    }
    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
    public ZonedDateTime getDate() {
        return date;
    }
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    public ArrayList<ShopUnit> getChildren() {
        return children;
    }
    public void setChildren(ArrayList<ShopUnit> children) {
        this.children = children;
    }
}

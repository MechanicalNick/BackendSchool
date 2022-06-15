package com.products.data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ShopUnitImportRequest {
    @Valid
    private ShopUnitImport[] items;
    @NotNull
    private String updateDate;

    public ShopUnitImport[] getItems() {
        return items;
    }

    public void setItems(ShopUnitImport[] items) {
        this.items = items;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}

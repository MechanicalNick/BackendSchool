package com.products.controller;

import com.products.data.ShopUnitImportRequest;
import com.products.service.MainService;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.products.data.Error;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
public class ProductsController {
    private final MainService service;

    public ProductsController(MainService service) {
        this.service = service;
    }
    @PostMapping(value ="/imports")
    ResponseEntity imports(@Valid @RequestBody @NotNull ShopUnitImportRequest importRequest) {
        var result = service.imports(importRequest);
        if(!result)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error());
        return ok(null);
    }

    @DeleteMapping(value ="/delete/{id}")
    ResponseEntity delete(@Valid @PathVariable("id") UUID id) {
        var item = service.find(id);
        if(item.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        service.delete(item.get());
        return ok(null);
    }

    @GetMapping(value ="/nodes/{id}")
    ResponseEntity nodes(@Valid @PathVariable(name = "id") UUID id) {
        var item = service.find(id);
        if(item.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        var entity = item.get();
        return ResponseEntity.status(HttpStatus.OK).body(service.createSubtree(entity));
    }

    @GetMapping(value ="/sales")
    ResponseEntity sales(@RequestParam("date")
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                         ZonedDateTime date)  {
        var items = service.findAll(date);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping(value ="/node/{id}/statistic")
    ResponseEntity statistic(@Valid @NotBlank @PathVariable("id") String id,
                             @RequestParam(name = "dateStart") String dateStart,
                             @RequestParam(name = "dateEnd") String dateEnd) {
        return ResponseEntity.status(HttpStatus.OK).body(dateEnd);
    }

    @GetMapping(value = "/getall")
    ResponseEntity getAll(){
       return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }

    @GetMapping(value = "/test")
    ResponseEntity test(){
        return ResponseEntity.status(HttpStatus.OK).body("async_test");
    }
}

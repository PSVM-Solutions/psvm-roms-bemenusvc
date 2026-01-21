package com.restaurant.menu_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.menu_service.dto.MenuItemRequest;
import com.restaurant.menu_service.dto.MenuItemsResponse;
import com.restaurant.menu_service.enums.CategoryEnum;
import com.restaurant.menu_service.services.MenuService;


@RequestMapping("/menu")
@RestController
public class MenuServiceController {

    @Autowired
    private MenuService menuService;
    
    @PostMapping("/addItem")
    public ResponseEntity<?> postMethodName(@RequestBody MenuItemRequest req) {
        boolean isSaved= menuService.addItem(req);
        if(isSaved)
            return ResponseEntity.ok("Ok, Saved Item: "+ req);
        else
            return ResponseEntity.ok("Not Saved!");
    }

    @GetMapping("/getAll")
    public List<MenuItemsResponse> getMethodName() {
        return menuService.getAll();
    }
    
    @GetMapping("getItem/{category}")
    public List<MenuItemsResponse> getMethodName(@PathVariable CategoryEnum category) {
        return menuService.getItemByCategory(category);
    }
    
    @GetMapping()
    public List<MenuItemsResponse> getMethodName(@RequestParam(required = false) Double price,
                                @RequestParam(required = false) Boolean available,
                                @RequestParam(required = false) Boolean veg,
                                @RequestParam(defaultValue = "asc") String sort) {
        return menuService.getFilteredItems(price, available, veg, sort);
    }
}

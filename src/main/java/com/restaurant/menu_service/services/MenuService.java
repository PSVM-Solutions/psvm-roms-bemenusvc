package com.restaurant.menu_service.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restaurant.menu_service.dto.MenuItemRequest;
import com.restaurant.menu_service.dto.MenuItemsResponse;
import com.restaurant.menu_service.entity.MenuItems;
import com.restaurant.menu_service.enums.CategoryEnum;
import com.restaurant.menu_service.mapper.MenuItemRequestToMenuItemsMapper;
import com.restaurant.menu_service.mapper.MenuItemToResponseMapper;
import com.restaurant.menu_service.repository.MenuItemsRepo;
import com.restaurant.menu_service.services.Interface.IMenuService;

@Service
public class MenuService implements IMenuService {

    @Autowired
    private MenuItemsRepo menuItemsRepo;

    @Override
    public List<MenuItemsResponse> getAll() {
        List<MenuItems> items= menuItemsRepo.findAll();
        return MenuItemToResponseMapper.toResponseList(items);
    }

    @Override
    public boolean addItem(MenuItemRequest req) {
        try {
            
            MenuItems item =  MenuItemRequestToMenuItemsMapper.toResponse(req);

            MenuItems savedItem = menuItemsRepo.save(item);

            // Confirm by checking if saved item has a valid ID (Mongo assigns it)
            return savedItem != null && savedItem.getId() != null;
        } catch (Exception e) {
            // Optional: log the error
            System.err.println("Error saving item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<MenuItemsResponse> getItemByCategory(CategoryEnum type) {
        List<MenuItems> fetched= menuItemsRepo.findByCategoryType(type);
        return MenuItemToResponseMapper.toResponseList(fetched);
    }

    public List<MenuItemsResponse> getFilteredItems(Double price, Boolean available, Boolean veg, String sort){
        List<MenuItems> items = menuItemsRepo.findAll();

        if (price != null) {
            items = items.stream()
                    .filter(item -> item.getPrice() <= price)
                    .collect(Collectors.toList());
        }

        if (available != null) {
            items = items.stream()
                    .filter(item -> item.isAvailable() == available)
                    .collect(Collectors.toList());
        }

        if (veg != null) {
            items = items.stream()
                    .filter(item -> item.isVeg() == veg)
                    .collect(Collectors.toList());
        }

        if (sort.equalsIgnoreCase("desc")) {
            items.sort(Comparator.comparingDouble(MenuItems::getPrice).reversed());
        } else {
            items.sort(Comparator.comparingDouble(MenuItems::getPrice));
        }


        List<MenuItemsResponse> item = MenuItemToResponseMapper.toResponseList(items);

        return item;
    }
}

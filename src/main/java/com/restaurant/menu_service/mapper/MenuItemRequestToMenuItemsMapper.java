package com.restaurant.menu_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.restaurant.menu_service.dto.MenuItemRequest;
import com.restaurant.menu_service.entity.MenuItems;

public class MenuItemRequestToMenuItemsMapper {
public static MenuItems toResponse(MenuItemRequest item) {
        MenuItems response = new MenuItems();
        response.setName(item.getName());
        response.setPrice(item.getPrice());
        response.setCategoryType(item.getCategoryType());
        response.setDescription(item.getDescription());
        response.setAvailable(item.isAvailable());
        response.setVeg(item.isVeg());
        return response;
    }

    public static List<MenuItems> toResponseList(List<MenuItemRequest> items) {
        return items.stream()
                    .map(MenuItemRequestToMenuItemsMapper::toResponse)
                    .collect(Collectors.toList());
    }
}

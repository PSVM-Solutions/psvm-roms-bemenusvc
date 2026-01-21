package com.restaurant.menu_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.restaurant.menu_service.dto.MenuItemsResponse;
import com.restaurant.menu_service.entity.MenuItems;

public class MenuItemToResponseMapper {

    public static MenuItemsResponse toResponse(MenuItems item) {
        MenuItemsResponse response = new MenuItemsResponse();
        response.setName(item.getName());
        response.setPrice(item.getPrice());
        response.setCategoryType(item.getCategoryType());
        response.setDescription(item.getDescription());
        response.setAvailable(item.isAvailable());
        response.setVeg(item.isVeg()); // convert enum to string
        return response;
    }

    public static List<MenuItemsResponse> toResponseList(List<MenuItems> items) {
        return items.stream()
                    .map(MenuItemToResponseMapper::toResponse)
                    .collect(Collectors.toList());
    }
}

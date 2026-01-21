package com.restaurant.menu_service.services.Interface;

import java.util.List;

import com.restaurant.menu_service.dto.MenuItemRequest;
import com.restaurant.menu_service.dto.MenuItemsResponse;
import com.restaurant.menu_service.enums.CategoryEnum;

public interface IMenuService {

    public List<MenuItemsResponse> getAll();

    public boolean addItem(MenuItemRequest req);

    public List<MenuItemsResponse> getItemByCategory(CategoryEnum type);

    public List<MenuItemsResponse> getFilteredItems(Double price, Boolean available, Boolean veg, String sort);

}

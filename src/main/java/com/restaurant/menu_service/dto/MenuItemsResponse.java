package com.restaurant.menu_service.dto;

import com.restaurant.menu_service.enums.CategoryEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemsResponse {

    private String name;

    private CategoryEnum categoryType;

    private String description;

    private double price;

    private boolean available;

    private boolean isVeg;

}

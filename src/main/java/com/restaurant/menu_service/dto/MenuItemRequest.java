package com.restaurant.menu_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.menu_service.enums.CategoryEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {

    private String name;

    private CategoryEnum categoryType;

    private String description;

    private double price;

    private boolean available;

    @JsonProperty("Veg")
    private boolean Veg;


}
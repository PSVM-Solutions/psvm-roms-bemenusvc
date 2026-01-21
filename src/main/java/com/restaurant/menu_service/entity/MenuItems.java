package com.restaurant.menu_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.restaurant.menu_service.enums.CategoryEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menu_items")
public class MenuItems {

    @Id
    private String id;

    private String name;

    private String description;

    private double price;

    private boolean available;

    private CategoryEnum categoryType;

    private boolean Veg;
}
package com.restaurant.menu_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.menu_service.entity.MenuItems;
import com.restaurant.menu_service.enums.CategoryEnum;

@Repository
public interface MenuItemsRepo extends MongoRepository<MenuItems ,String>  {

    List<MenuItems> findByCategoryType(CategoryEnum type);

}

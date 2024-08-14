package com.labhesh.MeroMeal.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labhesh.MeroMeal.models.Category;
import com.labhesh.MeroMeal.models.Item;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.models.SubCategory;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {

    @Query("SELECT i FROM item i WHERE i.shop = ?1 AND i.isDeleted = false")
    List<Item> findAllItems(Shop shop);

    @Query("SELECT i FROM item i WHERE i.shop = ?1 AND i.category = ?2 AND i.isDeleted = false")
    List<Item> findAllItems(Shop shop, Category category);

    @Query("SELECT i FROM item i WHERE i.shop = ?1 AND i.category = ?2 AND i.subCategory = ?3 AND i.isDeleted = false")
    List<Item> findAllItems(Shop shop, Category category, SubCategory subCategory);

    // item id and isInStock
    @Query("SELECT i FROM item i WHERE i.id = ?1 AND i.isInStock = ?2 AND i.isDeleted = false")
    Item findItem(UUID id, boolean isInStock);

    // all items with isInStock
    @Query("SELECT i FROM item i WHERE i.isInStock = ?1 AND i.isDeleted = false")
    List<Item> findByIsInStock(boolean isInStock);

    @Query("SELECT i FROM item i WHERE i.name = ?1 AND i.shop = ?2 AND i.isDeleted = false")
    Item existByNameAndShop(String name, Shop shop);
    
}


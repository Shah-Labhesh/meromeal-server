package com.labhesh.MeroMeal.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labhesh.MeroMeal.models.Category;
import com.labhesh.MeroMeal.models.Shop;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM category c WHERE c.id = ?1 AND c.isDeleted = false")
    Optional<Category> findCategory(UUID id);

    // name and shop
    @Query("SELECT c FROM category c WHERE c.name = ?1 AND c.shop = ?2 AND c.isDeleted = false")
    Optional<Category> findCategory(String name, Shop shop);

    @Query("SELECT c FROM category c WHERE c.id = ?1 AND c.shop = ?2 AND c.isDeleted = false")
    Optional<Category> findCategory(UUID id, Shop shop);

    @Query("SELECT c FROM category c WHERE c.shop = ?1 AND c.isDeleted = false")
    List<Category> findAllCategories(Shop shop);

    // shop id
    @Query("SELECT c FROM category c WHERE c.shop.id = ?1 AND c.isDeleted = false")
    List<Category> findAllCategories(UUID shop);

    @Query("SELECT c FROM category c WHERE c.isDeleted = false")
    List<Category> findAllCategories();

    @Query("SELECT c FROM category c WHERE c.name = ?1 AND c.shop = ?2 AND c.isDeleted = false")
    Category existByNameAndShop(String name, Shop shop);
    


    
}

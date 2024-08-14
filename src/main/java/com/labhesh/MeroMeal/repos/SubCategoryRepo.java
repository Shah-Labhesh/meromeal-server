package com.labhesh.MeroMeal.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labhesh.MeroMeal.models.Category;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.models.SubCategory;

public interface SubCategoryRepo extends JpaRepository<SubCategory, UUID> {

    @Query("SELECT s FROM sub_category s WHERE s.id = ?1 AND s.isDeleted = false")
    Optional<SubCategory> findSubCategory(UUID id);

    @Query("SELECT s FROM sub_category s WHERE s.id = ?1 AND s.shop = ?2 AND s.isDeleted = false")
    Optional<SubCategory> findSubCategory(UUID id, Shop shop);

    @Query("SELECT s FROM sub_category s WHERE s.category = ?1 AND s.isDeleted = false")
    Optional<SubCategory> findByCategory(Category category);

    @Query("SELECT s FROM sub_category s WHERE s.shop = ?1 AND s.isDeleted = false")
    List<SubCategory> findAllSubCategories(Shop shop);

    // find by shop and category
    @Query("SELECT s FROM sub_category s WHERE s.shop = ?1 AND s.category = ?2 AND s.isDeleted = false")
    List<SubCategory> findAllSubCategories(Shop shop, Category category);

    @Query("SELECT s FROM sub_category s WHERE s.isDeleted = false")
    List<SubCategory> findAllSubCategories();

    @Query("SELECT s FROM sub_category s WHERE s.name = ?1 AND s.shop = ?2 AND s.isDeleted = false")
    SubCategory existByNameAndShop(String name, Shop shop);

    @Query("SELECT s FROM sub_category s WHERE s.name = ?1 AND s.category.id = ?2 AND s.shop = ?3 AND s.isDeleted = false")
    SubCategory existByNameAndCategoryAndShop(String name, UUID id, Shop shop);

    
}

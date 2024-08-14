package com.labhesh.MeroMeal.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.labhesh.MeroMeal.enums.ShopCategory;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.models.Users;

@Repository
public interface ShopRepo extends JpaRepository<Shop, UUID> {

        @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Shop s WHERE s.name = ?1")
        boolean existsByName(String name);

        @Query("SELECT s FROM Shop s WHERE s.owner = ?1 AND s.owner.isDeleted = false")
        Optional<Shop> findByUser(Users user);

        // find by id
        @Query("SELECT s FROM Shop s WHERE s.id = ?1 AND s.owner.isDeleted = false")
        Optional<Shop> findShop(UUID id);

        @Query("SELECT s FROM Shop s WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < 50 AND s.isPopular = :isPopular AND :category MEMBER OF s.shopCategories AND s.isApproved = true AND s.owner.isDeleted = false")
        Page<Shop> filterShop(Pageable pageable, @Param("isPopular") boolean isPopular,
                        @Param("latitude") double latitude,
                        @Param("longitude") double longitude, @Param("category") List<ShopCategory> category);

        @Query("SELECT s FROM Shop s WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < 50 AND :category MEMBER OF s.shopCategories AND s.isApproved = true AND s.owner.isDeleted = false")
        Page<Shop> filterShop(Pageable pageable, @Param("latitude") double latitude,
                        @Param("longitude") double longitude, @Param("category") List<ShopCategory> category);

        @Query("SELECT s FROM Shop s WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < 50 AND s.isPopular = :isPopular AND s.isApproved = true AND s.owner.isDeleted = false")
        Page<Shop> filterShop(Pageable pageable, @Param("isPopular") boolean isPopular,
                        @Param("latitude") double latitude,
                        @Param("longitude") double longitude);

        @Query("SELECT s FROM Shop s WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < 50 AND s.isApproved = true AND s.owner.isDeleted = false")
        Page<Shop> filterShop(Pageable pageable, @Param("latitude") double latitude,
                        @Param("longitude") double longitude);

}

package com.labhesh.MeroMeal.repos;

import com.labhesh.MeroMeal.enums.UserRole;
import com.labhesh.MeroMeal.models.Users;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Users, UUID> {

    @Query("SELECT u FROM Users u WHERE u.email = ?1 AND u.isDeleted = false")
    Optional<Users> findByEmail(String username);

    @Query("SELECT u FROM Users u WHERE u.id = :id AND u.isDeleted = false")
    Optional<Users> userById(UUID id);

    @Query("SELECT u FROM Users u WHERE u.id = :id AND u.isInTrash = :trash AND u.isDeleted = false")
    Optional<Users> userById(UUID id, boolean trash);

    // trash user
    @Query("SELECT u FROM Users u WHERE u.isInTrash = true AND u.isDeleted = false")
    List<Users> trashUsers();

    @Query("SELECT u FROM Users u WHERE u.role = com.labhesh.MeroMeal.enums.UserRole.USER")
    List<Users> findOnlyUsers();

    @Query("SELECT u FROM Users u WHERE u.role = :role AND u.name = :name AND u.isDeleted = false")
    List<Users> filterByRoleAndName(Pageable pageable, UserRole role, String name);

    @Query("SELECT u FROM Users u WHERE u.role = :role AND u.isDeleted = false")
    List<Users> filterByRoleAndName(Pageable pageable, UserRole role);

    @Query("SELECT u FROM Users u WHERE u.name = :name AND u.isDeleted = false")
    List<Users> filterByRoleAndName(Pageable pageable, String name);

    @Query("SELECT u FROM Users u WHERE u.isDeleted = false")
    List<Users> filterByRoleAndName(Pageable pageable);
}

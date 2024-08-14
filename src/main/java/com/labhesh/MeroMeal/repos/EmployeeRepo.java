package com.labhesh.MeroMeal.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labhesh.MeroMeal.models.Employee;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.models.Users;
public interface EmployeeRepo extends JpaRepository<Employee, UUID> {

    @Query("SELECT e FROM employee e WHERE e.id = ?1 AND e.user.isDeleted = false")
    Optional<Employee> findEmployee(UUID id);

    @Query("SELECT e FROM employee e WHERE e.user = ?1 AND e.user.isDeleted = false")
    Optional<Employee> findEmployee(Users user);

    @Query("SELECT e FROM employee e WHERE e.id = ?1 AND e.shop = ?2 AND e.user.isDeleted = false")
    Optional<Employee> findEmployee(UUID id, Shop shop);

    @Query("SELECT e FROM employee e WHERE e.shop = ?1 AND e.user.isDeleted = false")
    List<Employee> findAllEmployees(Shop shop);
    
}

package com.labhesh.MeroMeal.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labhesh.MeroMeal.models.DeliveryHours;

@Repository
public interface DeliveryHoursRepo extends JpaRepository<DeliveryHours, UUID> {
    DeliveryHours findByDay(String day);
}

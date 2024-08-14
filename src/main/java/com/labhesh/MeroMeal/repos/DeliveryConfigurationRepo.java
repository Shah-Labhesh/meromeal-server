package com.labhesh.MeroMeal.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labhesh.MeroMeal.models.DeliveryConfiguration;
import com.labhesh.MeroMeal.models.Shop;

@Repository
public interface DeliveryConfigurationRepo extends JpaRepository<DeliveryConfiguration, UUID>  {

    Optional<DeliveryConfiguration> findByShop(Shop shop);
    
}

package com.labhesh.MeroMeal.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labhesh.MeroMeal.models.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, UUID> {

    
}

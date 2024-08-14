package com.labhesh.MeroMeal.models;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "delivery_configuration")
public class DeliveryConfiguration {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Builder.Default
    private double minOrder = 100.0;
    @Builder.Default
    private double riderCommission = 60.0;
    @Builder.Default
    private double baseDeliveryCharge = 50.0;
    @Builder.Default
    private double deliveryChargePerDistance = 10.0;
    @Builder.Default
    private double maxDeliveryDistance = 10.0;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Shop shop;

}

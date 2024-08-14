package com.labhesh.MeroMeal.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDeliveryConfigurationDto {


    @Pattern(regexp = "^[0-9]+$", message = "Min order price must be a number")
    private String minOrder;
    @Pattern(regexp = "^[0-9]+$", message = "Rider commission must be a number")
    private String riderCommission;
    @Pattern(regexp = "^[0-9]+$", message = "Base delivery charge must be a number")
    private String baseDeliveryCharge;
    @Pattern(regexp = "^[0-9]+$", message = "Delivery charge per distance must be a number")
    private String deliveryChargePerDistance;
    @Pattern(regexp = "^[0-9]+$", message = "Max Delivery distance must be a number")
    private String maxDeliveryDistance;

}

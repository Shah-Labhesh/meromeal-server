package com.labhesh.MeroMeal.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.labhesh.MeroMeal.enums.ShopAvailability;
import com.labhesh.MeroMeal.enums.ShopCategory;
import com.labhesh.MeroMeal.validation.EnumListValidator;
import com.labhesh.MeroMeal.validation.EnumValidator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShopDto {

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name should contain only alphabets")
    private String name;

    // +977-9812345678
    @Pattern(regexp = "^\\+977-[0-9]{10}$", message = "Phone number must be valid")
    private String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Description should contain only alphabets and numbers")
    private String description;

    // latitude and longitude
    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}$", message = "Latitude must be valid")
    private String latitude;

    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}$", message = "Longitude must be valid")
    private String longitude;

    @EnumListValidator(enumClass = ShopCategory.class)
    private List<String> shopCategory;

    
    private String address;

    @Size(min = 1, max = 7, message = "Opening hours must not be empty and should have at most 7 elements")
    private List<AddHoursDto> openingHours;

    // delivery hours
    @Size(min = 1, max = 7, message = "Delivery hours must not be empty and should have at most 7 elements")
    private List<AddHoursDto> deliveryHours;

    // pan number
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Pan number must be valid")
    private String panNumber;

    // vat number
    @Pattern(regexp = "^[0-9]{10}$", message = "Vat number must be valid")
    private String vatNumber;

    @Pattern(regexp = "^[0-9]{10}$", message = "License number must be valid")
    private String licenseNumber;

    @NotNull(message = "Display image must not be empty")
    private MultipartFile displayImage;

    @NotNull(message = "Cover image must not be empty")
    private MultipartFile coverImage;

    @NotNull(message = "License Document images must not be empty")
    private MultipartFile licenseDocumentImages;

    @EnumValidator(enumClass = ShopAvailability.class, message = "Shop availability must be \"DELIVERY\", \"PICKUP\" or \"BOTH\"")
    private String shopAvailability;
    
}

package com.labhesh.MeroMeal.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.labhesh.MeroMeal.enums.ShopAvailability;
import com.labhesh.MeroMeal.enums.ShopCategory;
import com.labhesh.MeroMeal.validation.EnumListValidator;
import com.labhesh.MeroMeal.validation.EnumValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterShopDto {

    @NotBlank(message = "Name must not be empty")
    // can also contain special characters with spaces
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name should contain only alphabets")
    private String name;

    @NotBlank(message = "Phone number must not be empty")
    // +977-9812345678
    @Pattern(regexp = "^\\+977-[0-9]{10}$", message = "Phone number must be valid")
    private String phoneNumber;

    @NotBlank(message = "Description must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Description should contain only alphabets and numbers")
    private String description;

    // latitude and longitude
    @NotBlank(message = "Latitude must not be empty")
    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}$", message = "Latitude must be valid")
    private String latitude;

    @NotBlank(message = "Longitude must not be empty")
    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}$", message = "Longitude must be valid")
    private String longitude;

    @NotNull(message = "Shop category must not be empty")
    @EnumListValidator(enumClass = ShopCategory.class)
    private List<String> shopCategory;

    @NotBlank(message = "Address must not be empty")
    private String address;

    @NotNull(message = "Opening hours must not be empty")
    @Size(min = 1, max = 7, message = "Opening hours must not be empty and should have at most 7 elements")
    private List<AddHoursDto> openingHours;

    // delivery hours
    @NotNull(message = "Delivery hours must not be empty")
    @Size(min = 1, max = 7, message = "Delivery hours must not be empty and should have at most 7 elements")
    private List<AddHoursDto> deliveryHours;

    // pan number
    @NotBlank(message = "Pan number must not be empty")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Pan number must be valid")
    private String panNumber;

    // vat number
    @NotBlank(message = "Vat number must not be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Vat number must be valid")
    private String vatNumber;

    @NotBlank(message = "License number must not be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "License number must be valid")
    private String licenseNumber;

    @NotNull(message = "Display image must not be empty")
    private MultipartFile displayImage;

    @NotNull(message = "Cover image must not be empty")
    private MultipartFile coverImage;

    @NotNull(message = "License Document images must not be empty")
    private MultipartFile licenseDocumentImages;

    @NotBlank(message = "Shop availability must not be empty")
    @EnumValidator(enumClass = ShopAvailability.class, message = "Shop availability must be \"DELIVERY\", \"PICKUP\" or \"BOTH\"")
    private String shopAvailability;
}

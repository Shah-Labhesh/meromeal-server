package com.labhesh.MeroMeal.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAddressDto {

    @NotBlank(message = "Street must not be empty")
    private String street;
    
    @NotBlank(message = "City must not be empty")
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", message = "City must contain only alphabets")
    @Size(min = 3, max = 50, message = "City must be at least 3 characters long and at most 50 characters long")
    private String city;

    @NotBlank(message = "State must not be empty")
    private String state;

    @NotBlank(message = "Zip must not be empty")
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Zip must be valid")
    private String zip;

    @NotBlank(message = "Country must not be empty")
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", message = "Country must contain only alphabets")
    @Size(min = 3, max = 50, message = "Country must be at least 3 characters long and at most 50 characters long")
    private String country;

    @NotBlank(message = "Latitude must not be empty")
    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}$", message = "Latitude must be valid")
    private String latitude;

    @NotBlank(message = "Longitude must not be empty")
    @Pattern(regexp = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}$", message = "Longitude must be valid")
    private String longitude;
    
}

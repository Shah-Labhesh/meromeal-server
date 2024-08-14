package com.labhesh.MeroMeal.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressDto {

    
    private String street;
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", message = "City must contain only alphabets")
    @Size(min = 3, max = 50, message = "City must be at least 3 characters long and at most 50 characters long")
    private String city;
    private String state;
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Zip must be valid")
    private String zip;
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", message = "Country must contain only alphabets")
    @Size(min = 3, max = 50, message = "Country must be at least 3 characters long and at most 50 characters long")
    private String country;

    // lat and long
    @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$", message = "Latitude must be valid")
    private double latitude;
    @Pattern(regexp = "^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$", message = "Longitude must be valid")
    private double longitude;
}

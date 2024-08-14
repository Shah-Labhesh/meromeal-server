package com.labhesh.MeroMeal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddHoursDto {

    @NotBlank(message = "Open time must not be empty")
    @Pattern(regexp = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$", message = "Open time must be in 12-hour format")
    private String open;
    @NotBlank(message = "Close time must not be empty")
    @Pattern(regexp = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$", message = "Close time must be in 12-hour format")
    private String close;
    @NotBlank(message = "Day must not be empty")
    @Pattern(regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)$", message = "Day must be valid")
    private String day;
    
}

package com.labhesh.MeroMeal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHoursDto {

    @NotBlank(message = "Id is required")
    @Pattern(regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$", message = "Id must be a valid UUID")
    private String id;
    @Pattern(regexp = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$", message = "Open time must be in 12-hour format")
    private String open;
    @Pattern(regexp = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$", message = "Close time must be in 12-hour format")
    private String close;
    @Pattern(regexp = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)$", message = "Day must be valid")
    private String day;
}

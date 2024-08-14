package com.labhesh.MeroMeal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {

    @Pattern(regexp = "^(1|2|3|4|5)$", message = "Rating must be between 1 and 5")
    private int rating;
    @NotBlank(message = "Comment must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Comment should contain only alphabets and numbers")
    @Size(min = 1, max = 100, message = "Comment must be at least 1 character long and at most 100 characters long")
    private String comment;
    
}

package com.labhesh.MeroMeal.dtos;

import org.springframework.web.multipart.MultipartFile;

import com.labhesh.MeroMeal.validation.StrongPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {


    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;
    @Email(message = "Email must be valid")
    private String email;
    @Pattern(regexp = "^(male|female)$", message = "Gender must be male or female")
    private String gender;
    private MultipartFile avatar;
    @NotBlank(message = "Password must not be empty")
    @StrongPassword(entity = "Password")
    private String password;

}

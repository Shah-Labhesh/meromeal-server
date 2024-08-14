package com.labhesh.MeroMeal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.MeroMeal.dtos.ChangePasswordDto;
import com.labhesh.MeroMeal.dtos.UpdateUserDto;
import com.labhesh.MeroMeal.enums.UserRole;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "auth")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @GetMapping("/current-user")
    @Operation(summary = "Get current user", description = "ALL")
    public ResponseEntity<?> getCurrentUser() throws BadRequestException {
        return userService.getCurrentUser();
    }

    @PatchMapping("/current-user")
    public ResponseEntity<?> updateUser(@ModelAttribute @RequestBody @Valid UpdateUserDto dto) throws BadRequestException, InternalServerException {
        return userService.updateUser(dto);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDto dto ) throws BadRequestException, InternalServerException {
        return userService.changePassword(dto);
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> allUsers(@RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) UserRole role, @RequestParam(required = false) String name) throws InternalServerException {
        return userService.allusers(page, size, role, name);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> softDelete(@PathVariable String user_id) throws BadRequestException, InternalServerException {
        return userService.deleteSoft(user_id);
    }

    @PostMapping("/recover/{user_id}")
    public ResponseEntity<?> recoverUser(@PathVariable String user_id) throws InternalServerException, BadRequestException {
        return userService.recoveruser(user_id);
    }

    @GetMapping("/trash")
    public ResponseEntity<?> trashUsers() throws InternalServerException{
        return userService.trashUsers();
    }
    
    @DeleteMapping("/trash/{user_id}")
    public ResponseEntity<?> removeFromTrash(@PathVariable String user_id) throws BadRequestException, InternalServerException{
        return userService.deletePermanently(user_id);
    }


}

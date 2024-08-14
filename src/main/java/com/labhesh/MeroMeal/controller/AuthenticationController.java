package com.labhesh.MeroMeal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.MeroMeal.dtos.InitiateDto;
import com.labhesh.MeroMeal.dtos.LoginDto;
import com.labhesh.MeroMeal.dtos.RegisterDto;
import com.labhesh.MeroMeal.dtos.ResetPasswordDto;
import com.labhesh.MeroMeal.dtos.UploadAvatarDto;
import com.labhesh.MeroMeal.dtos.VerifyDto;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) throws BadRequestException, InternalServerException {
        return authenticationService.register(registerDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.login(dto);
    }

    // initiate email verification
    @PostMapping("/initiate-email-verification")
    @Operation(summary = "Initiate email verification")
    public ResponseEntity<?> initiateEmailVerification(@RequestBody @Valid InitiateDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.initiateEmailVerification(dto);
    }

    // resend email verification
    @PostMapping("/resend-email-verification")
    @Operation(summary = "Resend email verification")
    public ResponseEntity<?> resendEmailVerification(@RequestBody @Valid InitiateDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.resendEmailVerification(dto);
    }

    // verify email
    @PostMapping("/verify-email")
    @Operation(summary = "Verify email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerifyDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.verifyEmail(dto);
    }

    // initiate password reset
    @PostMapping("/initiate-password-reset")
    @Operation(summary = "Initiate password reset")
    public ResponseEntity<?> initiatePasswordReset(@RequestBody @Valid InitiateDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.initiatePasswordReset(dto);
    }

    // resend password reset
    @PostMapping("/resend-password-reset")
    @Operation(summary = "Resend password reset")
    public ResponseEntity<?> resendPasswordReset(@RequestBody @Valid InitiateDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.resendPasswordReset(dto);
    }

    // verify password reset
    @PostMapping("/verify-password-reset")
    @Operation(summary = "Verify password reset")
    public ResponseEntity<?> verifyPasswordReset(@RequestBody @Valid VerifyDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.verifyPasswordReset(dto);
    }

    // reset password
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.resetPassword(dto);
    }

    // upload profile picture
    @PostMapping("/upload-profile-picture/{userId}")
    @Operation(summary = "Upload profile picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable String userId,@ModelAttribute @RequestBody @Valid UploadAvatarDto dto) throws BadRequestException, InternalServerException {
        return authenticationService.uploadAvatar(userId, dto);
    }

    
    
}

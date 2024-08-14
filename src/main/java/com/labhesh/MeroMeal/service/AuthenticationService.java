package com.labhesh.MeroMeal.service;

import com.labhesh.MeroMeal.config.JwtTokenUtil;
import com.labhesh.MeroMeal.config.PasswordEncoder;
import com.labhesh.MeroMeal.dtos.InitiateDto;
import com.labhesh.MeroMeal.dtos.LoginDto;
import com.labhesh.MeroMeal.dtos.RegisterDto;
import com.labhesh.MeroMeal.dtos.ResetPasswordDto;
import com.labhesh.MeroMeal.dtos.UploadAvatarDto;
import com.labhesh.MeroMeal.dtos.VerifyDto;
import com.labhesh.MeroMeal.enums.OtpType;
import com.labhesh.MeroMeal.enums.UserRole;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.models.Files;
import com.labhesh.MeroMeal.models.Otp;
import com.labhesh.MeroMeal.models.Users;
import com.labhesh.MeroMeal.repos.OtpRepo;
import com.labhesh.MeroMeal.repos.UserRepo;
import com.labhesh.MeroMeal.utils.EmailSender;
import com.labhesh.MeroMeal.utils.ImageService;
import com.labhesh.MeroMeal.utils.OtpUtils;
import com.labhesh.MeroMeal.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@EnableAsync
public class AuthenticationService {

    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailSender mailService;
    private final ImageService imageService;

    public UserDetails loadUserByUsername(String username) {
        Users user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User don't exist"));
        if (!user.isVerified()) {
            throw new UsernameNotFoundException("User is not verified");
        }
        if (user.isInTrash()) {
            throw new UsernameNotFoundException("User is in trash");
        }
        if (user.isDeleted()) {
            throw new UsernameNotFoundException("User is deleted");
        }
        return user;
    }

    public ResponseEntity<?> login(LoginDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credential");
        }
        if (!user.isVerified()) {
            throw new BadRequestException("User is not verified");
        }
        if (user.isInTrash()) {
            throw new BadRequestException("User is in trash");
        }
        String token = jwtTokenUtil.generateToken(user);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("message", "User logged in successfully");
        map.put("user", user);
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> register(RegisterDto dto) throws BadRequestException, InternalServerException {
        try {
            Users existUser = userRepo.findByEmail(dto.getEmail()).orElse(null);
            if (existUser != null) {
                throw new BadRequestException("Email address already in use");
            }
            Users user = Users.builder()
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .password(passwordEncoder.encodePassword(dto.getPassword()))
                    .gender(dto.getGender())
                    .role(getUserRole(dto.getRole()))
                    .build();
            userRepo.save(user);
            return ResponseEntity.created(null).body(new SuccessResponse("User registered successfully", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException("Email address already in use");
        } catch (Exception ex) {
            throw new InternalServerException(ex.getMessage());
        }
    }

    public ResponseEntity<?> initiateEmailVerification(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        Otp otp = Otp.builder()
                .email(dto.getEmail())
                .otp(OtpUtils.generateOtp())
                .otptype(OtpType.EMAIL_VERIFICATION)
                .user(user)
                .build();
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.EMAIL_VERIFICATION);
        return ResponseEntity.ok(new SuccessResponse("Email verification Otp Sent To Your Email.", null, null));
    }

    // resend email verification
    public ResponseEntity<?> resendEmailVerification(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        Otp otp = otpRepo.findByEmailAndOtpType(dto.getEmail(), OtpType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new BadRequestException("OTP not found"));
        otp.setOtp(OtpUtils.generateOtp());
        otp.setExpired(false);
        otp.setExpiredDate(otp.getCreatedDate().plusMinutes(10));
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.EMAIL_VERIFICATION);
        return ResponseEntity.ok(new SuccessResponse("Email verification Otp Sent To Your Email.", null, null));
    }

    // Verify email
    public ResponseEntity<?> verifyEmail(VerifyDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (user.isVerified()) {
            throw new BadRequestException("User is already verified");
        }
        System.out.println(dto.getOtp());
        Otp otp = otpRepo.findByEmailAndOtpAndType(dto.getEmail(), dto.getOtp(), OtpType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
        if (otp.isExpired()) {
            throw new BadRequestException("OTP is expired");
        }
        user.setVerified(true);
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("User verified successfully", null, null));
    }

    // initiate password reset
    public ResponseEntity<?> initiatePasswordReset(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        Otp otp = Otp.builder()
                .email(dto.getEmail())
                .otp(OtpUtils.generateOtp())
                .otptype(OtpType.PASSWORD_RESET)
                .user(user)
                .build();
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.PASSWORD_RESET);
        return ResponseEntity.ok(new SuccessResponse("Password reset Otp Sent To Your Email.", null, null));
    }

    // resend password reset
    public ResponseEntity<?> resendPasswordReset(InitiateDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("User don't exist"));
        Otp otp = otpRepo.findByEmailAndOtpType(dto.getEmail(), OtpType.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("OTP not found"));
        otp.setOtp(OtpUtils.generateOtp());
        otp.setExpired(false);
        otp.setExpiredDate(otp.getCreatedDate().plusMinutes(10));
        otpRepo.save(otp);
        // send email verification link
        mailService.sendOtpEmail(user.getEmail(), user.getName(), otp.getOtp(), OtpType.PASSWORD_RESET);
        return ResponseEntity.ok(new SuccessResponse("Password reset Otp Sent To Your Email.", null, null));
    }

    // verify password reset
    public ResponseEntity<?> verifyPasswordReset(VerifyDto dto) throws BadRequestException {
        userRepo.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("User don't exist"));
        Otp otp = otpRepo.findByEmailAndOtpAndType(dto.getEmail(), dto.getOtp(), OtpType.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
        if (otp.isExpired()) {
            throw new BadRequestException("OTP is expired");
        }
        otp.setExpired(true);
        otpRepo.save(otp);
        return ResponseEntity.ok(new SuccessResponse("Reset password verified", null, null));
    }



    // reset password
    public ResponseEntity<?> resetPassword(ResetPasswordDto dto) throws BadRequestException {
        Users user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        otpRepo.findByEmailAndOtpAndType(dto.getEmail(), dto.getOtp(), OtpType.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
        user.setPassword(passwordEncoder.encodePassword(dto.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("Password reset successfully", null, null));
    }

    // upload avatar 
    public ResponseEntity<?> uploadAvatar( String id, UploadAvatarDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = userRepo.findById(UUID.fromString(id)).orElseThrow(() -> new BadRequestException("User not found"));
        Files file = imageService.saveImage(dto.getAvatar());
        user.setAvatar(file);
        userRepo.save(user);
        return ResponseEntity.ok(new SuccessResponse("Avatar uploaded successfully", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    private UserRole getUserRole(String role) {
        return switch (role) {
            case "ADMIN" -> UserRole.ADMIN;
            default -> UserRole.USER;
        };
    }

}

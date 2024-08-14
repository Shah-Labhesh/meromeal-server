package com.labhesh.MeroMeal.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.labhesh.MeroMeal.dtos.ChangePasswordDto;
import com.labhesh.MeroMeal.dtos.UpdateUserDto;
import com.labhesh.MeroMeal.enums.UserRole;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.models.Users;
import com.labhesh.MeroMeal.repos.UserRepo;
import com.labhesh.MeroMeal.utils.ImageService;
import com.labhesh.MeroMeal.utils.SuccessResponse;

import jakarta.validation.Valid;

import com.labhesh.MeroMeal.config.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ImageService imageService;
    private final PasswordEncoder encoder;

    // current user
    public Users currentUser() throws BadRequestException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User don't exist"));
        if (!user.isVerified()) {
            throw new BadRequestException("User is not verified");
        }
        if (user.isInTrash()) {
            throw new BadRequestException("User is in trash");
        }
        if (user.isDeleted()) {
            throw new BadRequestException("User is deleted");
        }
        return user;
    }

    public ResponseEntity<?> getCurrentUser() throws BadRequestException {
        return ResponseEntity.ok(currentUser());
    }

    public ResponseEntity<?> updateUser(UpdateUserDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = currentUser();
            if (encoder.matches(dto.getPassword(), user.getPassword())) {
                throw new BadRequestException("Password is incorrect");
            }
            user.setName(dto.getName() != null ? dto.getName() : user.getName());
            user.setEmail(dto.getEmail() != null ? dto.getEmail() : user.getEmail());
            user.setGender(dto.getGender() != null ? dto.getGender() : user.getGender());
            user.setAvatar(dto.getAvatar() != null ? imageService.saveImage(dto.getAvatar()) : user.getAvatar());
            user.setUpdatedDate(LocalDateTime.now());
            HashMap<String, Object> response = new HashMap<>();
            response.put("message", "User updated successfully");
            if (dto.getEmail() != null) {
                response.put("emailUpdate", true);
            } else {
                response.put("emailUpdate", false);
            }
            response.put("user", userRepo.save(user));
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }

    }

    public ResponseEntity<?> allusers(int page, int size, UserRole role, String name) throws InternalServerException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            if (role != null && !name.isBlank()) {
                return ResponseEntity.ok(userRepo.filterByRoleAndName(pageable, role, name));
            } else if (role != null) {
                return ResponseEntity.ok(userRepo.filterByRoleAndName(pageable, role));
            } else if (!name.isBlank()) {
                return ResponseEntity.ok(userRepo.filterByRoleAndName(pageable, name));
            } else {
                return ResponseEntity.ok(userRepo.filterByRoleAndName(pageable));
            }
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteSoft(String user_id) throws BadRequestException, InternalServerException {
        try {
            Users user = userRepo.userById(UUID.fromString(user_id))
                    .orElseThrow(() -> new BadRequestException("User not found"));
            if (user.isInTrash()) {
                throw new BadRequestException("User is already in trash");
            }
            user.setInTrash(true);
            userRepo.save(user);
            return ResponseEntity.ok(new SuccessResponse("User moved to trash", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> recoveruser(String user_id) throws InternalServerException, BadRequestException {
        try {
            Users user = userRepo.userById(UUID.fromString(user_id))
                    .orElseThrow(() -> new BadRequestException("User not found"));
            if (!user.isInTrash()) {
                throw new BadRequestException("User is not in trash");
            }
            user.setInTrash(false);
            userRepo.save(user);
            return ResponseEntity.ok(new SuccessResponse("User recovered successfully", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> trashUsers() throws InternalServerException {
        try {
            return ResponseEntity.ok(userRepo.trashUsers());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> deletePermanently(String user_id) throws BadRequestException, InternalServerException {
        try {
            Users user = userRepo.userById(UUID.fromString(user_id), true)
                    .orElseThrow(() -> new BadRequestException("User not in trash"));
            user.setDeleted(true);
            user.setInTrash(false);
            user.setDeletedDate(LocalDateTime.now());
            return ResponseEntity.ok(new SuccessResponse("User deleted permanently", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<?> changePassword(@Valid ChangePasswordDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = currentUser();
            if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
                throw new BadRequestException("Old password is incorrect");
            }
            if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                throw new BadRequestException("New & confirm Passwords do not match");
            }
            user.setPassword(encoder.encodePassword(dto.getNewPassword()));
            user.setUpdatedDate(LocalDateTime.now());
            userRepo.save(user);
            return ResponseEntity.ok(new SuccessResponse("Password changed successfully", user, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

}

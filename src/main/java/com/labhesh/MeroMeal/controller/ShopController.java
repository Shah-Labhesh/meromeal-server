package com.labhesh.MeroMeal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.MeroMeal.dtos.RatingDto;
import com.labhesh.MeroMeal.dtos.RegisterShopDto;
import com.labhesh.MeroMeal.dtos.UpdateDeliveryConfigurationDto;
import com.labhesh.MeroMeal.dtos.UpdateShopDto;
import com.labhesh.MeroMeal.enums.ShopCategory;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.ForbiddenException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.service.ShopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
@Tag(name = "Shop")
public class ShopController {

    private final ShopService shopService;

    @SecurityRequirement(name = "auth")
    @PostMapping
    @Operation(summary = "Add a new shop", description = "SELLER")
    public ResponseEntity<?> newShop(@ModelAttribute @RequestBody @Valid RegisterShopDto dto)
            throws InternalServerException, BadRequestException {
        return shopService.addShop(dto);
    }

    // my-shop
    @SecurityRequirement(name = "auth")
    @GetMapping("/my-shop")
    @Operation(summary = "Get my shop", description = "SELLER")
    public ResponseEntity<?> getMyShop() throws InternalServerException, BadRequestException {
        return shopService.myShop();
    }

    // dashboard
    @SecurityRequirement(name = "auth")
    @GetMapping("/dashboard")
    @Operation(summary = "Get shop dashboard", description = "SELLER")
    public ResponseEntity<?> getShopDashboard() throws InternalServerException, BadRequestException {
        return shopService.dashboard();
    }

    // shop by id
    @GetMapping("/{id}")
    @Operation(summary = "Get shop by id")
    public ResponseEntity<?> getShopById(@PathVariable String id) throws InternalServerException, BadRequestException {
        return shopService.shopById(id);
    }

    @SecurityRequirement(name = "auth")
    @PatchMapping("/{id}")
    @Operation(summary = "Update shop by id", description = "SELLER")
    public ResponseEntity<?> updateShopById(@PathVariable String id,
            @ModelAttribute @RequestBody @Valid UpdateShopDto dto)
            throws InternalServerException, BadRequestException, ForbiddenException {
        return shopService.shopById(id, dto);
    }

    @SecurityRequirement(name = "auth")
    @PatchMapping("/delivery-config")
    @Operation(summary = "update delivery config", description = "SELLER")
    public ResponseEntity<?> updateDeliveryConfig(@RequestBody @Valid UpdateDeliveryConfigurationDto dto)
            throws InternalServerException, BadRequestException {
        return shopService.updateConfiguration(dto);
    }

    @SecurityRequirement(name = "auth")
    // add review
    @PostMapping("/review/{id}")
    @Operation(summary = "Add review", description = "USER")
    public ResponseEntity<?> addReview(@PathVariable String id, @RequestBody @Valid RatingDto dto)
            throws InternalServerException, BadRequestException {
        return ResponseEntity.ok("Not implemented");
    }

    // get reviews
    @GetMapping("/review/{id}")
    @Operation(summary = "Get reviews")
    public ResponseEntity<?> getReviews(@PathVariable String id) throws InternalServerException, BadRequestException {
        return ResponseEntity.ok("Not implemented");
    }

    // make shop popular
    @SecurityRequirement(name = "auth")
    @PostMapping("/popular/{id}")
    @Operation(summary = "Make shop popular", description = "ADMIN")
    public ResponseEntity<?> makePopular(@PathVariable String id, @RequestParam boolean value)
            throws InternalServerException, BadRequestException {
        return shopService.makePopular(id, value);
    }

    // reply to review
    @SecurityRequirement(name = "auth")
    @PostMapping("/review/reply/{id}")
    @Operation(summary = "Reply to review", description = "SELLER")
    public ResponseEntity<?> replyToReview(@PathVariable String id, @RequestBody @Valid RatingDto dto)
            throws InternalServerException, BadRequestException {
        return ResponseEntity.ok("Not implemented");
    }

    // filter shops
    @GetMapping("/filter/{latitude}/{longitude}")
    @Operation(summary = "Filter shops")
    public ResponseEntity<?> filterShops(@PathVariable double latitude, @PathVariable double longitude,
    @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) List<ShopCategory> category, @RequestParam(required = false) boolean popular)
            throws InternalServerException, BadRequestException {
        return shopService.filter(page, size, latitude, longitude, popular, category);
    }

}

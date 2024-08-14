package com.labhesh.MeroMeal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.labhesh.MeroMeal.dtos.RegisterShopDto;
import com.labhesh.MeroMeal.dtos.UpdateDeliveryConfigurationDto;
import com.labhesh.MeroMeal.dtos.UpdateShopDto;
import com.labhesh.MeroMeal.enums.ShopAvailability;
import com.labhesh.MeroMeal.enums.ShopCategory;
import com.labhesh.MeroMeal.enums.UserRole;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.ForbiddenException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.models.Address;
import com.labhesh.MeroMeal.models.DeliveryConfiguration;
import com.labhesh.MeroMeal.models.DeliveryHours;
import com.labhesh.MeroMeal.models.Files;
import com.labhesh.MeroMeal.models.OpeningHours;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.models.Users;
import com.labhesh.MeroMeal.repos.AddressRepo;
import com.labhesh.MeroMeal.repos.DeliveryConfigurationRepo;
import com.labhesh.MeroMeal.repos.DeliveryHoursRepo;
import com.labhesh.MeroMeal.repos.OpeningHoursRepo;
import com.labhesh.MeroMeal.repos.ShopRepo;
import com.labhesh.MeroMeal.repos.UserRepo;
import com.labhesh.MeroMeal.utils.ImageService;
import com.labhesh.MeroMeal.utils.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepo shopRepo;
    private final OpeningHoursRepo openingHoursRepo;
    private final DeliveryHoursRepo deliveryHoursRepo;
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;
    private final ImageService imageService;
    private final DeliveryConfigurationRepo deliveryConfigurationRepo;

    // add shop
    public ResponseEntity<?> addShop(RegisterShopDto dto) throws InternalServerException, BadRequestException {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepo.findByEmail(name).orElseThrow(() -> new BadRequestException("User not found"));
            // check userRole
            if (user.getRole() != UserRole.SELLER) {
                throw new BadRequestException("User is not a shop owner");
            }
            Shop existShop = shopRepo.findByUser(user).orElse(null);
            if (existShop != null) {
                throw new BadRequestException("Shop already exists");
            }

            if (shopRepo.existsByName(dto.getName())) {
                throw new BadRequestException("Shop with name " + dto.getName() + " already exists");
            }
            if (dto.getShopCategory().isEmpty()) {
                throw new BadRequestException("Shop category must not be empty");
            }
            if (dto.getOpeningHours().isEmpty()) {
                throw new BadRequestException("Opening hours must not be empty");
            }
            if (dto.getDeliveryHours().isEmpty()) {
                throw new BadRequestException("Delivery hours must not be empty");
            }
            List<OpeningHours> openingHours = new ArrayList<>();
            dto.getOpeningHours().forEach(oh -> {
                OpeningHours openingHour = OpeningHours.builder()
                        .day(oh.getDay())
                        .open(oh.getOpen())
                        .close(oh.getClose())
                        .build();
                openingHours.add(openingHour);
            });
            openingHoursRepo.saveAll(openingHours);
            List<DeliveryHours> deliveryHours = new ArrayList<>();
            dto.getDeliveryHours().forEach(dh -> {
                DeliveryHours deliveryHour = DeliveryHours.builder()
                        .day(dh.getDay())
                        .open(dh.getOpen())
                        .close(dh.getClose())
                        .build();
                deliveryHours.add(deliveryHour);
            });
            deliveryHoursRepo.saveAll(deliveryHours);

            Shop shop = Shop.builder()
                    .name(dto.getName())
                    .phoneNumber(dto.getPhoneNumber())
                    .description(dto.getDescription())
                    .latitude(Double.parseDouble(dto.getLatitude()))
                    .longitude(Double.parseDouble(dto.getLongitude()))
                    .shopCategories(dto.getShopCategory())
                    .address(dto.getAddress())
                    .coverImage(imageService.saveImage(dto.getCoverImage()))
                    .displayImage(imageService.saveImage(dto.getDisplayImage()))
                    .documents(imageService.saveImage(dto.getLicenseDocumentImages()))
                    .openingHours(openingHours)
                    .owner(user)
                    .deliveryHours(deliveryHours)
                    .build();
            shopRepo.save(shop);
            deliveryConfigurationRepo.save(DeliveryConfiguration.builder().shop(shop).build());
            return ResponseEntity.ok(new SuccessResponse("Shop registered successfully", shop, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            // TODO: handle exception
            throw new InternalServerException(e.getMessage());
        }
    }

    // my shop
    public ResponseEntity<?> myShop() throws BadRequestException, InternalServerException {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepo.findByEmail(name).orElseThrow(() -> new BadRequestException("User not found"));
            Shop shop = shopRepo.findByUser(user).orElseThrow(() -> new BadRequestException("Shop not found"));
            return ResponseEntity.ok(shop);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            // TODO: handle exception
            throw new InternalServerException(e.getMessage());
        }
    }

    // dashboard
    public ResponseEntity<?> dashboard() throws BadRequestException, InternalServerException {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepo.findByEmail(name).orElseThrow(() -> new BadRequestException("User not found"));
            Shop shop = shopRepo.findByUser(user).orElseThrow(() -> new BadRequestException("Shop not found"));
            // get total orders
            // get total sales
            // today's orders
            // today's sales
            // user reviews
            // Recent orders
            return ResponseEntity.ok(shop);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // shop by id
    public ResponseEntity<?> shopById(String id) throws BadRequestException, InternalServerException {
        try {
            Shop shop = shopRepo.findById(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Shop not found"));
            return ResponseEntity.ok(shop);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // update shop by id
    public ResponseEntity<?> shopById(String id, UpdateShopDto dto)
            throws BadRequestException, InternalServerException, ForbiddenException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
            Shop shop = shopRepo.findById(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Shop not found"));
            if (!shop.getOwner().equals(user)) {
                throw new ForbiddenException("You are not the owner of this shop");
            }
            shop.setName(dto.getName() != null ? dto.getName() : shop.getName());
            shop.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : shop.getPhoneNumber());
            shop.setDescription(dto.getDescription() != null ? dto.getDescription() : shop.getDescription());
            shop.setLatitude(dto.getLatitude() != null ? Double.parseDouble(dto.getLatitude()) : shop.getLatitude());
            shop.setLongitude(
                    dto.getLongitude() != null ? Double.parseDouble(dto.getLongitude()) : shop.getLongitude());
            shop.setAddress(dto.getAddress() != null ? dto.getAddress() : shop.getAddress());
            shop.setPanNumber(dto.getPanNumber() != null ? dto.getPanNumber() : shop.getPanNumber());
            shop.setVatNumber(dto.getVatNumber() != null ? dto.getVatNumber() : shop.getVatNumber());
            shop.setLicenseNumber(dto.getLicenseNumber() != null ? dto.getLicenseNumber() : shop.getLicenseNumber());
            shop.setDisplayImage(dto.getDisplayImage() != null ? imageService.saveImage(dto.getDisplayImage())
                    : shop.getDisplayImage());
            shop.setCoverImage(dto.getCoverImage() != null ? imageService.saveImage(dto.getCoverImage())
                    : shop.getCoverImage());
            shop.setShopAvailability(
                    dto.getShopAvailability() != null ? ShopAvailability.valueOf(dto.getShopAvailability())
                            : shop.getShopAvailability());
            shop.setDocuments(dto.getLicenseDocumentImages() != null ? imageService.saveImage(dto.getLicenseDocumentImages())
                    : shop.getDocuments()); 
            if (dto.getShopCategory() != null) {
                List<String> oldCategories = shop.getShopCategories();
                for (String category : dto.getShopCategory()) {
                    if (!oldCategories.contains(category)) {
                        oldCategories.add(category);
                    }
                }
                shop.setShopCategories(oldCategories);
            }
            // document

            // shop.setCoverImage(imageService.saveImage(dto.getCoverImage()));
            // shop.setDisplayImage(imageService.saveImage(dto.getDisplayImage()));
            // List<Files> documents = new ArrayList<>();
            // for (MultipartFile file : dto.getDocumentImages()) {
            // documents.add(imageService.saveImage(file));
            // }
            // shop.setDocuments(documents);
            shopRepo.save(shop);
            return ResponseEntity.ok(new SuccessResponse("Shop updated successfully", shop, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ForbiddenException e) {
            throw new ForbiddenException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // update delivery configuration
    public ResponseEntity<?> updateConfiguration(UpdateDeliveryConfigurationDto dto)
            throws BadRequestException, InternalServerException {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
            Shop shop = shopRepo.findByUser(user).orElseThrow(() -> new BadRequestException("Shop not found"));
            DeliveryConfiguration configuration = deliveryConfigurationRepo.findByShop(shop)
                    .orElseThrow(() -> new BadRequestException("Delivery configuration not found"));
            configuration.setBaseDeliveryCharge(
                    dto.getBaseDeliveryCharge() != null ? Double.parseDouble(dto.getBaseDeliveryCharge())
                            : configuration.getBaseDeliveryCharge());
            configuration.setDeliveryChargePerDistance(
                    dto.getDeliveryChargePerDistance() != null ? Double.parseDouble(dto.getDeliveryChargePerDistance())
                            : configuration.getDeliveryChargePerDistance());
            configuration.setMaxDeliveryDistance(
                    dto.getMaxDeliveryDistance() != null ? Double.parseDouble(dto.getMaxDeliveryDistance())
                            : configuration.getMaxDeliveryDistance());
            configuration.setMinOrder(
                    dto.getMinOrder() != null ? Double.parseDouble(dto.getMinOrder()) : configuration.getMinOrder());
            configuration
                    .setRiderCommission(dto.getRiderCommission() != null ? Double.parseDouble(dto.getRiderCommission())
                            : configuration.getRiderCommission());
            deliveryConfigurationRepo.save(configuration);
            return ResponseEntity.ok(new SuccessResponse("Delivery configuration updated successfully", shop, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // add review

    // get reviews

    // make shop popular
    public ResponseEntity<?> makePopular(String shopId, boolean value)
            throws BadRequestException, InternalServerException {
        try {
            Shop shop = shopRepo.findById(UUID.fromString(shopId))
                    .orElseThrow(() -> new BadRequestException("Shop not found"));
            shop.setPopular(value);
            shopRepo.save(shop);
            return ResponseEntity.ok(new SuccessResponse("Shop updated successfully", shop, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // reply to review

    // filter without login
    public ResponseEntity<?> filter(int pageSize, int pageNumber, double lat, double lon, boolean popular,
            List<ShopCategory> category) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (popular && category != null) {
            return ResponseEntity.ok(shopRepo.filterShop(pageable, popular, lat, lon, category));
        } else if (popular) {
            return ResponseEntity.ok(shopRepo.filterShop(pageable, popular, lat, lon));
        } else if (category != null) {
            return ResponseEntity.ok(shopRepo.filterShop(pageable, lat, lon, category));
        } else {
            return ResponseEntity.ok(shopRepo.filterShop(pageable, lat, lon));
        }
    }

    // filter with login

    // approve shop by id
    public ResponseEntity<?> approveShop(String id, boolean approve)
            throws BadRequestException, InternalServerException {
        try {
            Shop shop = shopRepo.findById(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Shop not found"));
            shop.setApproved(approve);
            shopRepo.save(shop);
            return ResponseEntity.ok(new SuccessResponse("Shop approved successfully", shop, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

}

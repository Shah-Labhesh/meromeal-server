package com.labhesh.MeroMeal.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.labhesh.MeroMeal.dtos.AddCategoryDto;
import com.labhesh.MeroMeal.dtos.UpdateCategoryDto;
import com.labhesh.MeroMeal.enums.UserRole;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.repos.CategoryRepo;
import com.labhesh.MeroMeal.repos.EmployeeRepo;
import com.labhesh.MeroMeal.repos.ShopRepo;
import com.labhesh.MeroMeal.repos.SubCategoryRepo;
import com.labhesh.MeroMeal.utils.ImageService;
import com.labhesh.MeroMeal.utils.SuccessResponse;
import com.labhesh.MeroMeal.models.Category;
import com.labhesh.MeroMeal.models.Employee;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.models.SubCategory;
import com.labhesh.MeroMeal.models.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final UserService userService;
    private final ShopRepo shopRepo;
    private final ImageService imageService;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final EmployeeRepo employeeRepo;

    // add category
    public ResponseEntity<?> addCategory(AddCategoryDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            Shop shop = null;
            if (user.getRole() == UserRole.SELLER) {
                shop = shopRepo.findByUser(user)
                        .orElseThrow(() -> new BadRequestException("Shop don't exist"));
            } else {
                shop = employeeRepo.findEmployee(user).get().getShop();
            }
            if (categoryRepo.findCategory(dto.getName(), shop).isPresent()) {
                throw new BadRequestException("Category already exist");
            }
            categoryRepo.save(Category.builder()
                    .name(dto.getName())
                    .shop(shop)
                    .image(imageService.saveImage(dto.getImage()))
                    .createdBy(user.getRole() == UserRole.SELLER ? shop.getId() : employee.getId())
                    .build());

            return ResponseEntity.ok(new SuccessResponse("Category added successfully",
                    categoryRepo.findCategory(dto.getName(), shop).get(), null));

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // my categories
    public ResponseEntity<?> myCategories() throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Shop shop = shopRepo.findByUser(user)
                    .orElseThrow(() -> new BadRequestException("Shop don't exist"));
            return ResponseEntity.ok(categoryRepo.findAllCategories(shop));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // get shop categories by shop id
    public ResponseEntity<?> getShopCategories(String id) throws BadRequestException, InternalServerException {
        try {
            shopRepo.findShop(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Shop don't exist"));
            return ResponseEntity.ok(categoryRepo.findAllCategories(UUID.fromString(id)));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // update category
    public ResponseEntity<?> updateCategory(String id, UpdateCategoryDto dto)
            throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            Shop shop = null;
            if (user.getRole() == UserRole.SELLER) {
                shop = shopRepo.findByUser(user)
                        .orElse(
                                null);
            } else {
                shop = employeeRepo.findEmployee(user).get().getShop();
            }
            if (shop == null) {
                throw new BadRequestException("Shop don't exist");
            }
            if (categoryRepo.findCategory(UUID.fromString(id), shop).isEmpty()) {
                throw new BadRequestException("Category don't exist");
            }
            if (categoryRepo.findCategory(dto.getName(), shop).isPresent()) {
                throw new BadRequestException("Category already exist");
            }
            Category category = categoryRepo.findCategory(UUID.fromString(id), shop)
                    .orElseThrow(() -> new BadRequestException("Category don't exist"));
            category.setName(dto.getName() != null ? dto.getName() : category.getName());
            category.setImage(dto.getImage() != null ? imageService.saveImage(dto.getImage()) : category.getImage());
            category.setUpdatedDate(LocalDateTime.now());
            category.setUpdatedBy(user.getRole() == UserRole.SELLER ? shop.getId() : employee.getId());
            return ResponseEntity.ok(new SuccessResponse("Category updated successfully",
                    categoryRepo.save(category), null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // delete category
    public ResponseEntity<?> deleteCategory(String id) throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            Shop shop = null;
            if (user.getRole() == UserRole.SELLER) {
                shop = shopRepo.findByUser(user)
                        .orElse(null);
            } else {
                shop = employeeRepo.findEmployee(user).get().getShop();
            }
            if (shop == null) {
                throw new BadRequestException("Shop don't exist");
            }
            if (categoryRepo.findCategory(UUID.fromString(id), shop).isEmpty()) {
                throw new BadRequestException("Category don't exist");
            }
            if (subCategoryRepo.findByCategory(categoryRepo.findCategory(UUID.fromString(id), shop).get())
                    .isPresent()) {
                throw new BadRequestException("Category has sub categories");
            }
            Category category = categoryRepo.findCategory(UUID.fromString(id), shop)
                    .orElseThrow(() -> new BadRequestException("Category don't exist"));
            category.setDeleted(true);
            category.setDeletedBy(user.getRole() == UserRole.SELLER ? shop.getId() : employee.getId());
            category.setCreatedDate(LocalDateTime.now());
            categoryRepo.save(category);
            return ResponseEntity.ok(new SuccessResponse("Category deleted successfully", null, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // add sub category
    public ResponseEntity<?> addSubCategory(String categoryId, AddCategoryDto dto) throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            Shop shop = null;
            if (user.getRole() == UserRole.SELLER) {
                shop = shopRepo.findByUser(user)
                        .orElse(null);
            } else {
                shop = employeeRepo.findEmployee(user).get().getShop();
            }
            if (shop == null) {
                throw new BadRequestException("Shop don't exist");
            }
            if (categoryRepo.findCategory(UUID.fromString(categoryId), shop).isEmpty()) {
                throw new BadRequestException("Category don't exist");
            }
            if (subCategoryRepo.existByNameAndShop(dto.getName(), shop) != null) {
                throw new BadRequestException("Sub category already exist");
            }
            subCategoryRepo.save(SubCategory.builder()
                    .name(dto.getName())
                    .category(categoryRepo.findCategory(UUID.fromString(categoryId), shop).get())
                    .shop(shop)
                    .createdBy(user.getRole() == UserRole.SELLER ? shop.getId() : employee.getId())
                    .build());
            return ResponseEntity.ok(new SuccessResponse("Sub category added successfully",
                    subCategoryRepo.existByNameAndShop(dto.getName(), shop), null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // my sub categories
    public ResponseEntity<?> mySubCategories() throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            if (user.getRole() == UserRole.SELLER) {
                Shop shop = shopRepo.findByUser(user)
                        .orElseThrow(() -> new BadRequestException("Shop don't exist"));
                return ResponseEntity.ok(subCategoryRepo.findAllSubCategories(shop));
            } else {
                Shop shop = employee.getShop();
                return ResponseEntity.ok(subCategoryRepo.findAllSubCategories(shop));
            }
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // get sub categories by category id
    public ResponseEntity<?> getSubCategories(String categoryId) throws BadRequestException, InternalServerException {
        try {
            Category category = categoryRepo.findCategory(UUID.fromString(categoryId))
                    .orElseThrow(() -> new BadRequestException("Category don't exist"));
            return ResponseEntity.ok(subCategoryRepo.findByCategory(category));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // get shop sub categories by shop id and category id
    public ResponseEntity<?> getShopSubCategories(String shopId, String categoryId)
            throws BadRequestException, InternalServerException {
        try {
            Shop shop = shopRepo.findShop(UUID.fromString(shopId))
                    .orElseThrow(() -> new BadRequestException("Shop don't exist"));
            Category category = categoryRepo.findCategory(UUID.fromString(categoryId))
                    .orElseThrow(() -> new BadRequestException("Category don't exist"));
            return ResponseEntity
                    .ok(subCategoryRepo.findAllSubCategories(shop, category));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // update sub category
    public ResponseEntity<?> updateSubCategory(String categoryId, String subCategoryId , UpdateCategoryDto dto)
            throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            Shop shop = null;
            if (user.getRole() == UserRole.SELLER) {
                shop = shopRepo.findByUser(user)
                        .orElse(null);
            } else {
                shop = employeeRepo.findEmployee(user).get().getShop();
            }
            if (shop == null) {
                throw new BadRequestException("Shop don't exist");
            }
            if (subCategoryRepo.findSubCategory(UUID.fromString(subCategoryId)).isEmpty()) {
                throw new BadRequestException("Sub category don't exist");
            }
            if (categoryRepo.findCategory(UUID.fromString(categoryId), shop).isEmpty()) {
                throw new BadRequestException("Category don't exist");
            }
            if (subCategoryRepo.existByNameAndCategoryAndShop(dto.getName(),
                    UUID.fromString(categoryId), shop) != null) {
                throw new BadRequestException("Sub category already exist");
            }
            SubCategory subCategory = subCategoryRepo.findSubCategory(UUID.fromString(subCategoryId))
                    .orElseThrow(() -> new BadRequestException("Sub category don't exist"));
            subCategory.setName(dto.getName() != null ? dto.getName() : subCategory.getName());
            subCategory.setCategory(categoryId != null
                    ? categoryRepo.findCategory(UUID.fromString(categoryId), shop).get()
                    : subCategory.getCategory());
            subCategory.setImage(dto.getImage() != null ? imageService.saveImage(dto.getImage()) : subCategory.getImage());
            subCategory.setUpdatedDate(LocalDateTime.now());
            subCategory.setUpdatedBy(user.getRole() == UserRole.SELLER ? shop.getId() : employee.getId());
            return ResponseEntity.ok(new SuccessResponse("Sub category updated successfully",
                    subCategoryRepo.save(subCategory), null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    // delete sub category
    public ResponseEntity<?> deleteSubCategory(String id) throws BadRequestException, InternalServerException {
        try {
            Users user = userService.currentUser();
            Employee employee = employeeRepo.findEmployee(user)
                    .orElse(null);
            Shop shop = null;
            if (user.getRole() == UserRole.SELLER) {
                shop = shopRepo.findByUser(user)
                        .orElse(null);
            } else {
                shop = employeeRepo.findEmployee(user).get().getShop();
            }
            if (shop == null) {
                throw new BadRequestException("Shop don't exist");
            }
            if (subCategoryRepo.findSubCategory(UUID.fromString(id)).isEmpty()) {
                throw new BadRequestException("Sub category don't exist");
            }
            if (subCategoryRepo.findSubCategory(UUID.fromString(id)).get().getShop() != shop) {
                throw new BadRequestException("Sub category don't exist");
            }
            SubCategory subCategory = subCategoryRepo.findSubCategory(UUID.fromString(id))
                    .orElseThrow(() -> new BadRequestException("Sub category don't exist"));
            subCategory.setDeleted(true);
            subCategory.setDeletedBy(user.getRole() == UserRole.SELLER ? shop.getId() : employee.getId());
            subCategory.setDeletedDate(LocalDateTime.now());
            subCategoryRepo.save(subCategory);
            return ResponseEntity.ok(new SuccessResponse("Sub category deleted successfully", null, null));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }



}

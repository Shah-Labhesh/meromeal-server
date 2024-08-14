package com.labhesh.MeroMeal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labhesh.MeroMeal.dtos.AddCategoryDto;
import com.labhesh.MeroMeal.dtos.UpdateCategoryDto;
import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/category")
@RestController
@Tag(name = "Category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @SecurityRequirement(name = "auth")
    @Operation(summary = "Add a new category", description = "SELLER, EMPLOYEE")
    @PostMapping
    public ResponseEntity<?> createCategory(@ModelAttribute @RequestBody @Valid AddCategoryDto dto) throws BadRequestException, InternalServerException {
        return categoryService.addCategory(dto);
    }

    // my categories
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Get all categories", description = "SELLER, EMPLOYEE")
    @GetMapping("/my-categories")
    public ResponseEntity<?> myCategories() throws BadRequestException, InternalServerException {
        return categoryService.myCategories();
    }

    // all categories
    @Operation(summary = "Get shop categories")
    @GetMapping("/{shop_id}")
    public ResponseEntity<?> allCategories(@PathVariable String shop_id) throws BadRequestException, InternalServerException {
        return categoryService.getShopCategories(shop_id);
    }

    // update category
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Update category", description = "SELLER, EMPLOYEE")
    @PatchMapping("/{category_id}")
    public ResponseEntity<?> updateCategory(@PathVariable String category_id, @ModelAttribute @RequestBody @Valid UpdateCategoryDto dto) throws BadRequestException, InternalServerException {
        return categoryService.updateCategory(category_id, dto);
    }

    // delete category
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Delete category", description = "SELLER, EMPLOYEE")
    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String category_id) throws BadRequestException, InternalServerException {
        return categoryService.deleteCategory(category_id);
    }

    // add sub category
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Add a new sub category", description = "SELLER, EMPLOYEE")
    @PostMapping("/{category_id}/sub-category")
    public ResponseEntity<?> addSubCategory(@PathVariable String category_id, @ModelAttribute @RequestBody @Valid AddCategoryDto dto) throws BadRequestException, InternalServerException {
        return categoryService.addSubCategory(category_id, dto);
    }

    // get my sub categories
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Get my sub categories", description = "SELLER, EMPLOYEE")
    @GetMapping("/my-sub-categories")
    public ResponseEntity<?> mySubCategories() throws BadRequestException, InternalServerException {
        return categoryService.mySubCategories();
    }

    // get all sub categories
    @Operation(summary = "Get all sub categories")
    @GetMapping("/{category_id}/sub-categories")
    public ResponseEntity<?> allSubCategories(@PathVariable String category_id) throws BadRequestException, InternalServerException {
        return categoryService.getSubCategories(category_id);
    }

    // get by shop id and category id
    @Operation(summary = "Get sub categories by shop id and category id")
    @GetMapping("/{shop_id}/{category_id}/sub-categories")
    public ResponseEntity<?> allSubCategories(@PathVariable String shop_id, @PathVariable String category_id) throws BadRequestException, InternalServerException {
        return categoryService.getShopSubCategories(shop_id, category_id);
    }

    // update sub category
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Update sub category", description = "SELLER, EMPLOYEE")
    @PatchMapping("/{category_id}/sub-category/{sub_category_id}")
    public ResponseEntity<?> updateSubCategory(@PathVariable String category_id, @PathVariable String sub_category_id, @ModelAttribute @RequestBody @Valid UpdateCategoryDto dto) throws BadRequestException, InternalServerException {
        return categoryService.updateSubCategory(category_id, sub_category_id, dto);
    }

    // delete sub category
    @SecurityRequirement(name = "auth")
    @Operation(summary = "Delete sub category", description = "SELLER, EMPLOYEE")
    @DeleteMapping("/sub-category/{sub_category_id}")
    public ResponseEntity<?> deleteSubCategory(@PathVariable String sub_category_id) throws BadRequestException, InternalServerException {
        return categoryService.deleteSubCategory(sub_category_id);
    }


    
}

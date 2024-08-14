package com.labhesh.MeroMeal.service;

import org.springframework.stereotype.Service;

import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.models.Shop;
import com.labhesh.MeroMeal.repos.EmployeeRepo;
import com.labhesh.MeroMeal.repos.ShopRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final UserService userService;
    private final ShopRepo shopRepo;
    private final EmployeeRepo employeeRepo;

    // my associated shop
    public Shop myShop() throws BadRequestException {
        return employeeRepo.findEmployee(userService.currentUser()).get().getShop();
    }

}

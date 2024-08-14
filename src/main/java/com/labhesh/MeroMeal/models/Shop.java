package com.labhesh.MeroMeal.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.labhesh.MeroMeal.enums.ShopAvailability;
import com.labhesh.MeroMeal.enums.ShopCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String phoneNumber;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Files displayImage;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Files coverImage;
    @Builder.Default
    private double latitude = 0.0;
    @Builder.Default
    private double longitude = 0.0;
    @Builder.Default
    private boolean isActive = false;

    @ElementCollection(targetClass = ShopCategory.class)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<String> shopCategories = new ArrayList<>();

    private String address;
    @Builder.Default
    private boolean isApproved = false;
    private String panNumber;
    private String vatNumber;
    private String licenseNumber;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)  
    private Files documents;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private List<OpeningHours> openingHours = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private List<DeliveryHours> deliveryHours = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShopAvailability shopAvailability;

    @Builder.Default
    private boolean isPopular = false;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Users owner;
    
    // createdDate, updatedDate, deletedDate
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;
    
}

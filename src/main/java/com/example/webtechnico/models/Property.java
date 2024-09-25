package com.example.webtechnico.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Property {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    
    //@Column(unique = true, nullable = false)
    private Long propertyId;
    
    private String address;
    
    private int yearOfConstruction;
    
    private PropertyType propertyType;
    
    @ManyToOne
    @JoinColumn(name = "propertyOwner_id")
    private PropertyOwner propertyOwner;
    
    private Boolean isActive;

//    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PropertyRepair> propertyRepairs;

//    @Override
//    public String toString() {
//        return "Property{" + "id=" + id + ", propertyId=" + propertyId + ", address=" + address + ", yearOfConstruction=" + yearOfConstruction + ", propertyType=" + propertyType + ", propertyOwner=" + propertyOwner.getId() + ", propertyRepairs=" + propertyRepairs + ", isActive=" + isActive + '}';
//    }
    
    
}

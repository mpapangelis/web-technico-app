package com.example.webtechnico.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "propertyOwner")
public class PropertyOwner  {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "vat", unique = true, nullable = false)
    private Long vat;

    //@Column(name = "first_name", nullable = false)
    private String firstName;

    //@Column(name = "last_name", nullable = false)
    private String lastName;

    //@Column(name = "address", nullable = false)
    private String address;

    //@Column(name = "phone_number", unique = true, nullable = false)
    private Long phoneNumber;

    //@Column(name = "email", unique = true, nullable = false)
    private String email;

    //@Column(name = "user_name", nullable = false)
    private String userName;

    //@Column(name = "password", nullable = false)
    private String password;
    
    private Boolean isActive;

    @OneToMany(mappedBy = "propertyOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Property> properties;
    
}

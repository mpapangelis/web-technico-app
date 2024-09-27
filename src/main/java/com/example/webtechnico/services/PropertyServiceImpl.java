package com.example.webtechnico.services;


import com.example.webtechnico.exceptions.*;
import com.example.webtechnico.models.Property;
import com.example.webtechnico.models.PropertyOwner;
import com.example.webtechnico.models.PropertyType;
import com.example.webtechnico.repositories.PropertyOwnerRepository;
import com.example.webtechnico.repositories.PropertyRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Named("PropertyService")
@RequestScoped
public class PropertyServiceImpl implements PropertyService{

    @Inject
    @Named("PropertyRepoDb")
    private PropertyRepository propertyRepository;
    @Inject
    @Named("PropertyOwnerRepoDb")
    private PropertyOwnerRepository propertyOwnerRepository;
    
    
    @Override
    public Optional<Property> findByPropertyIdNumber(Long propertyIdNumber) {
        return propertyRepository.findByPropertyIdNumber(propertyIdNumber);
    }//TODO

    @Override
    public List<Property> findByOwnerVatNumber(Long vatNumber) {
        List<Property> properties = propertyRepository.findByOwnerVatNumber(vatNumber);
        if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found for owner with VAT number " + vatNumber);
        }
        return properties;
    }

    @Override
    public Property createProperty(Long propertyIdNumber, String address, int yearOfConstruction, PropertyType propertyType, Long propertyOwnerId) {
        
        //check if owner exists
        Optional<PropertyOwner> owner = propertyOwnerRepository.findById(propertyOwnerId);
        
        if (!owner.isPresent()) {
            throw new OwnerNotFoundException("Owner with ID " + propertyOwnerId + " does not exist.");
        }
        //check if property id (e9) is unique
        Optional<Property> existingProperty = propertyRepository.findByPropertyIdNumber(propertyIdNumber);
        
        if (existingProperty.isPresent()) {
            throw new DuplicateEntryException("Property with ID " + propertyIdNumber + " already exists.");
        }
        
        Property property = Property.builder()
                .propertyId(propertyIdNumber)
                .address(address)
                .yearOfConstruction(yearOfConstruction)
                .propertyType(propertyType)
                .propertyOwner(owner.get())
                .isActive(true)
                .build();
        
        return propertyRepository.create(property);
    }

    @Override
    public Property updateProperty(Long id, Long propertyIdNumber, String address, int yearOfConstruction, PropertyType propertyType, Long propertyOwnerId) {
        Optional<Property> propertyToUpdateCheck = propertyRepository.findById(id);
        if (!propertyToUpdateCheck.isPresent() || !propertyToUpdateCheck.get().getIsActive()){
            throw new ResourceNotFoundException("Property with ID " + id + " does not exist or is inactive.");
        }
        
        Property propertyToUpdate = propertyToUpdateCheck.get();
        
        //check if its going to be changed and validate that the new property id will be different from the previous one if its going to be changed
        if (propertyIdNumber != null && !propertyIdNumber.equals(propertyToUpdate.getPropertyId())) {
            //check if the propertyId already exists
            Optional<Property> existingPropertyWithSameIdNumber = findByPropertyIdNumber(propertyIdNumber);
            if (existingPropertyWithSameIdNumber.isPresent()) {
                throw new DuplicateEntryException("Property with identification number " + propertyIdNumber + " already exists.");
            }
            propertyToUpdate.setPropertyId(propertyIdNumber);
        }
        
        if (address != null) {
            propertyToUpdate.setAddress(address);
        }
        
        if (yearOfConstruction > 1800 && yearOfConstruction <= java.time.Year.now().getValue()) {
            propertyToUpdate.setYearOfConstruction(yearOfConstruction);
        } else if (yearOfConstruction != 0) {
            throw new InvalidYearException("Year of construction must be between 1800 and the current year.");
        }
        
        if (propertyType != null) {
            propertyToUpdate.setPropertyType(propertyType);
        }
        
        if (propertyOwnerId != null) {
            Optional<PropertyOwner> owner = propertyOwnerRepository.findById(propertyOwnerId);
            if (!owner.isPresent()) {
                throw new OwnerNotFoundException("Owner with ID " + propertyOwnerId + " does not exist.");
            }
            propertyToUpdate.setPropertyOwner(owner.get());
        }
        
        propertyRepository.update(propertyToUpdate);
        
        return propertyToUpdate;
        
        
    }

    @Override
    public Property deleteProperty(Long id) {
        Optional<Property> propertyToDeleteCheck = propertyRepository.findById(id);
        if (!propertyToDeleteCheck.isPresent() || !propertyToDeleteCheck.get().getIsActive()) {
            throw new ResourceNotFoundException("Property with ID " + id + " does not exist or is inactive.");
        }
        
        boolean isDeleted = propertyRepository.deleteById(id);
        if (!isDeleted) {
            throw new RuntimeException("Failed to delete Property with ID " + id);
        }

        return propertyToDeleteCheck.get();
    }
    
    @Override
    public Property softDeleteProperty(Long id){
        Optional<Property> propertyToDeleteCheck = propertyRepository.findById(id);
        if (!propertyToDeleteCheck.isPresent() || !propertyToDeleteCheck.get().getIsActive()) {
            throw new ResourceNotFoundException("Property with ID " + id + " does not exist or is inactive.");
        }
        
        Property propertyToDelete = propertyToDeleteCheck.get();
        propertyRepository.softDelete(propertyToDelete);
        return propertyToDelete;
    }

    @Override
    public List<Property> findPropertiesByOwnerId(Long ownerId) {
        List<Property> properties = propertyRepository.findByOwnerId(ownerId);
        if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found for owner with ID " + ownerId);
        }
        return properties;
    }

    @Override
    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
    }

    
    
}

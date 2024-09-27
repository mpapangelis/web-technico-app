package com.example.webtechnico.services;


import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.exceptions.PropertyNotFoundException;
import com.example.webtechnico.exceptions.PropertyOwnerExistsException;
import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.Property;
import com.example.webtechnico.models.PropertyOwner;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.StatusOfRepairEnum;
import com.example.webtechnico.models.TypeOfRepairEnum;
import com.example.webtechnico.repositories.PropertyOwnerRepository;
import com.example.webtechnico.repositories.PropertyRepairRepository;
import com.example.webtechnico.repositories.PropertyRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named("PropertyRepairService")
@RequestScoped
public class PropertyRepairServiceImpl implements PropertyRepairService {

    @Inject
    @Named("PropertyRepairRepoDb")
    private PropertyRepairRepository propertyRepairRepository;
    
    @Inject
    @Named("PropertyOwnerRepoDb")
    private PropertyOwnerRepository propertyOwnerRepository;
    
    @Inject
    @Named("PropertyRepoDb")
    private PropertyRepository propertyRepository;


    @Override
    public void initiateRepair(Long propertyId, TypeOfRepairEnum typeOfRepair, String shortDescription, String fullDescription) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property with id " + propertyId + " not found"));

        PropertyRepair repair = PropertyRepair.builder()
                .shortDescription(shortDescription)
                .property(property)
                .typeOfRepair(typeOfRepair)
                .submissionDate(new Date())
                .description(fullDescription)
                .status(StatusOfRepairEnum.PENDING)
                .isActive(true)
                .build();
        propertyRepairRepository.create(repair);
    }

//    @Override
//    public Boolean acceptRepair(PropertyRepair propertyRepair) throws PropertyOwnerExistsException {
//        try {
//            if (propertyRepair != null) {
//                if (propertyRepair.isOwnerAcceptance() == true) {
//                    propertyRepair.setStatus(StatusOfRepairEnum.INPROGRESS);
//                    propertyRepair.setActualStartDate(propertyRepair.getProposedStartDate());
//                    propertyRepair.setActualEndDate(propertyRepair.getProposedEndDate());
//                    propertyRepairRepository.update(propertyRepair);
//                    return true;
//                }
//                propertyRepair.setStatus(StatusOfRepairEnum.DECLINED);
//                /*-------------------Stay null--------------*/
//                propertyRepairRepository.update(propertyRepair);
//            }
//        } catch (PersistenceException e) {
//            throw new OwnerNotFoundException("There is no property to repair");
//        }
//        return false;
//
//    }

//    @Override
//    public List<PropertyRepair> findRepairsInProgressByOwner(Long ownerId) {
//        return propertyRepairRepository.findAll().stream()
//                .filter(repair -> StatusOfRepairEnum.INPROGRESS.equals(repair.getStatus()) && ownerId.equals(repair.getPropertyOwner().getId()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<PropertyRepair> findRepairsCompletedByOwner(Long ownerId) {
//        return propertyRepairRepository.findAll().stream()
//                .filter(repair -> StatusOfRepairEnum.COMPLETE.equals(repair.getStatus()) && ownerId.equals(repair.getPropertyOwner().getId()))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void deletePendingRepair(Long repairId) {
//        propertyRepairRepository.findById(repairId).ifPresent(repair -> {
//            if (StatusOfRepairEnum.PENDING.equals(repair.getStatus())) {
//                propertyRepairRepository.delete(repair);
//            }
//        });
//    }

//    @Override
//    public <T> void updateRepair(PropertyRepair propertyRepair, T value, int choose) {//any type of data
//        try {
//            if (propertyRepair.getStatus().equals(StatusOfRepairEnum.PENDING) && propertyRepair.getIsActive()) {
//                switch (choose) {
//                    case 1 -> {
//                        if (value instanceof TypeOfRepairEnum typeOfRepairStr) {
//                            propertyRepair.setTypeOfRepair(typeOfRepairStr);
//                        }
//                    }
//                    case 2 -> {
//                        if (value instanceof String shortDesciptionStr) {
//                            propertyRepair.setShortDescription(shortDesciptionStr);
//                        }
//                        //auta isws ginoun throws h exceptions
//                    }
//                    case 3 -> {
//                        if (value instanceof String descriptionStr) {
//                            propertyRepair.setDescription(descriptionStr);
//                        }
//                    }
//
//                }
//
//                propertyRepairRepository.update(propertyRepair);
//            }
//        } catch (PersistenceException e) {
//            throw new InvalidInputException("Your input is Invalid");
//        }
//
//    }

    @Override
    public List<PropertyRepair> searchRepairsByDateRange(Date startDate, Date endDate) {
        return propertyRepairRepository.searchByDateRange(startDate, endDate);
    }

    @Override
    public List<PropertyRepair> searchRepairsBySubmissionDate(Date submissionDate) {
        return propertyRepairRepository.searchBySubmissionDate(submissionDate);
    }

    @Override
    public void softDelete(Long repairId) {
          PropertyRepair repair = propertyRepairRepository.findById(repairId)
            .orElseThrow(() -> new IllegalStateException("Repair with id " + repairId + " does not exist"));

        if (repair.getStatus().equals(StatusOfRepairEnum.PENDING)) {
            repair.setIsActive(false); 
            propertyRepairRepository.update(repair);
        } else {
            throw new IllegalStateException("Repair is not pending and cannot be deleted");
        }
    }

    @Override
    public List<PropertyRepair> searchRepairsByOwnerId(Long ownerId) {
        return propertyRepairRepository.searchByOwnerId(ownerId);
    }
    
    @Override
    public boolean delete(Long id) throws ResourceNotFoundException {
        boolean deleted = propertyRepairRepository.deleteById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Property Repair with ID " + id + " not found.");
        }
        return deleted;
    }

    @Override
    public PropertyRepair updatePropertyRepair(Long repairId, Long propertyId, TypeOfRepairEnum typeOfRepair,
            String shortDescription, String description,
            Date proposedStartDate, Date proposedEndDate,
            int proposedCost, boolean ownerAcceptance,
            StatusOfRepairEnum status, Date actualStartDate, Date actualEndDate) {
        
        Optional<PropertyRepair> repairToUpdateCheck = propertyRepairRepository.findById(repairId);
        if (!repairToUpdateCheck.isPresent() || !repairToUpdateCheck.get().getIsActive()) {
            throw new ResourceNotFoundException("PropertyRepair with ID " + repairId + " does not exist or is inactive.");
        }

        PropertyRepair repairToUpdate = repairToUpdateCheck.get();

        if (propertyId != null) {
            Optional<Property> property = propertyRepository.findById(propertyId);
            if (!property.isPresent()) {
                throw new PropertyNotFoundException("Property with ID " + propertyId + " does not exist.");
            }
            repairToUpdate.setProperty(property.get());
        }

        if (typeOfRepair != null) {
            repairToUpdate.setTypeOfRepair(typeOfRepair);
        }

        if (shortDescription != null) {
            repairToUpdate.setShortDescription(shortDescription);
        }

        if (description != null) {
            repairToUpdate.setDescription(description);
        }

        if (proposedStartDate != null) {
            repairToUpdate.setProposedStartDate(proposedStartDate);
        }

        if (proposedEndDate != null) {
            repairToUpdate.setProposedEndDate(proposedEndDate);
        }

        if (proposedCost >= 0) {
            repairToUpdate.setProposedCost(proposedCost);
        }

        repairToUpdate.setOwnerAcceptance(ownerAcceptance);

        if (status != null) {
            repairToUpdate.setStatus(status);
        }

        if (actualStartDate != null) {
            repairToUpdate.setActualStartDate(actualStartDate);
        }

        if (actualEndDate != null) {
            repairToUpdate.setActualEndDate(actualEndDate);
        }

        propertyRepairRepository.update(repairToUpdate);

        return repairToUpdate;
    }
}

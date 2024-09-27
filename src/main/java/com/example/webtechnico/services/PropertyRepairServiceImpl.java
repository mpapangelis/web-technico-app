package com.example.webtechnico.services;


import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.exceptions.PropertyNotFoundException;
import com.example.webtechnico.exceptions.PropertyOwnerExistsException;
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
    public List<PropertyRepair> searchRepairsByDateRage(LocalDate startDate, LocalDate endDate) {
        return propertyRepairRepository.searchByDateRange(startDate, endDate);
    }

    @Override
    public List<PropertyRepair> searchRepairsBySubmissionDate(LocalDate submissionDate) {
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
}

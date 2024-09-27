package com.example.webtechnico.services;


import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.StatusOfRepairEnum;
import com.example.webtechnico.models.TypeOfRepairEnum;
import java.util.Date;
import java.util.List;

public interface PropertyRepairService {
    void initiateRepair(Long propertyId, TypeOfRepairEnum typeOfRepair, String shortDescription, String fullDescription);
    //List<PropertyRepair> findRepairsInProgressByOwner(Long ownerId);
    //List<PropertyRepair> findRepairsCompletedByOwner(Long ownerId);
    //void deletePendingRepair(Long repairId);
 
    //Boolean acceptRepair(PropertyRepair propertyRepair);
    //<T> void updateRepair(PropertyRepair propertyRepair,T value,int choose);
    PropertyRepair updatePropertyRepair(Long repairId, Long propertyId, TypeOfRepairEnum typeOfRepair,
                    String shortDescription, String description, Date proposedStartDate, Date proposedEndDate,
                    int proposedCost, boolean ownerAcceptance, StatusOfRepairEnum status, Date actualStartDate, Date actualEndDate);
    List<PropertyRepair> searchRepairsByDateRange(Date startDate,Date endDate);
    List<PropertyRepair> searchRepairsBySubmissionDate(Date submissionDate);
    List<PropertyRepair> searchRepairsByOwnerId(Long ownerId);
    void softDelete(Long repairId);
    boolean delete(Long repairId) throws ResourceNotFoundException;
 
}

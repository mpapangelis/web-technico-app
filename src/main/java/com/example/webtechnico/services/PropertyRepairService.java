package com.example.webtechnico.services;


import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.TypeOfRepairEnum;
import java.time.LocalDate;
import java.util.List;

public interface PropertyRepairService {
    void initiateRepair(Long propertyId, TypeOfRepairEnum typeOfRepair, String shortDescription, String fullDescription);
    //List<PropertyRepair> findRepairsInProgressByOwner(Long ownerId);
    //List<PropertyRepair> findRepairsCompletedByOwner(Long ownerId);
    //void deletePendingRepair(Long repairId);
 
    //Boolean acceptRepair(PropertyRepair propertyRepair);
    //<T> void updateRepair(PropertyRepair propertyRepair,T value,int choose);
    List<PropertyRepair> searchRepairsByDateRage(LocalDate startDate,LocalDate endDate);
    List<PropertyRepair> searchRepairsBySubmissionDate(LocalDate submissionDate);
    void softDelete(Long repairId);
 
}

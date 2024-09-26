package com.example.webtechnico.services;


import com.example.webtechnico.exceptions.InvalidInputException;
import com.example.webtechnico.exceptions.PropertyNotFoundException;
import com.example.webtechnico.exceptions.ResourceNotFoundException;
import com.example.webtechnico.models.Admin;
import com.example.webtechnico.models.Property;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.StatusOfRepairEnum;
import com.example.webtechnico.repositories.AdminRepository;
import com.example.webtechnico.repositories.PropertyRepairRepository;
import com.example.webtechnico.repositories.PropertyRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Named("AdminService")
@RequestScoped
public class AdminServiceImpl implements AdminService {

    @Inject
    @Named("AdminRepoDb")
    private AdminRepository adminRepository;
    
    @Inject
    @Named("PropertyRepoDb")
    private PropertyRepository propertyRepository;
    
    @Inject
    @Named("PropertyRepairRepoDb")
    private PropertyRepairRepository propertyRepairRepository;


    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.createAdmin(admin);
    }

    @Override
    public Admin changeAdmin(String username, String password) {
        return adminRepository.changeAdmin(username, password);
    }

    @Override
    public String getAdminUsername() {
        return adminRepository.getAdminUsername();
    }

    @Override
    public List<PropertyRepair> getAllRepairs() {
        return propertyRepairRepository.findAll();
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public PropertyRepair repairProposition(Long repairId, String newStatus, LocalDate proposedStartDate, LocalDate proposedEndDate, int proposedCost)
            throws PropertyNotFoundException, InvalidInputException {
        // Find the repair by ID
        Optional<PropertyRepair> optionalPropertyRepair = propertyRepairRepository.findById(repairId);

        if (optionalPropertyRepair.isEmpty()) {
            throw new PropertyNotFoundException("Repair with id " + repairId + " does not exist");
        } else {
            PropertyRepair repair = optionalPropertyRepair.get();

            // Validate and set the new status
            try {
                StatusOfRepairEnum statusEnum = StatusOfRepairEnum.valueOf(newStatus);
                repair.setStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException("Invalid status: " + newStatus);
            }

            // Update the proposed cost and dates
            repair.setProposedCost(proposedCost);
            repair.setProposedStartDate(proposedStartDate);
            repair.setProposedEndDate(proposedEndDate);

            // Save the updated repair
            propertyRepairRepository.update(repair);

            return repair;
        }
    }

    @Override
    public List<PropertyRepair> getActiveRepairs() {
        List<PropertyRepair> activeRepairs = adminRepository.getActiveRepairs();
        if (activeRepairs.isEmpty()) {
            throw new ResourceNotFoundException("No active repairs found.");
        }
        return activeRepairs;
    }

    @Override
    public List<PropertyRepair> getInactiveRepairs() {
        List<PropertyRepair> inactiveRepairs = adminRepository.getInactiveRepairs();
        if (inactiveRepairs.isEmpty()) {
            throw new ResourceNotFoundException("No inactive repairs found.");
        }
        return inactiveRepairs;
    }

    @Override
    public List<PropertyRepair> getAllPendingRepairs() {
        List<PropertyRepair> pendingRepairs = adminRepository.getAllPendingRepairs();
        if (pendingRepairs.isEmpty()) {
            throw new ResourceNotFoundException("No pending repairs found.");
        }
        return pendingRepairs;
    }
}

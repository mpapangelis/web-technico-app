
package com.example.webtechnico.services;

import com.example.webtechnico.exceptions.*;
import com.example.webtechnico.models.PropertyOwner;
import com.example.webtechnico.repositories.PropertyOwnerRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import java.util.List;

import java.util.Optional;

@Named("PropertyOwnerService")
@RequestScoped
public class PropertyOwnerServiceImpl implements PropertyOwnerService{

    @Inject
    @Named("PropertyOwnerRepoDb")
    private PropertyOwnerRepository propertyOwnerRepository;

    private PropertyOwner propertyOwner;

    @Override
    public PropertyOwner get(Long id) throws OwnerNotFoundException {
        Optional<PropertyOwner> optionalPropertyOwner = propertyOwnerRepository.findById(id);

        if (optionalPropertyOwner.isEmpty()){
            throw new OwnerNotFoundException("This is not an existing owner");
        } else {
            return optionalPropertyOwner.get();
        }
    }

    public PropertyOwner signInOwner(String email, String password) throws OwnerNotFoundException, InvalidInputException{

            propertyOwner = propertyOwnerRepository.searchByEmail(email);
            if (propertyOwner.getPassword().equals(password)) {
                return propertyOwner;
            }
            throw new InvalidInputException("You entered the wrong password");

    }

    @Override
    public PropertyOwner create(String firstName,
                                String lastName,
                                String email,
                                String userName,
                                String phoneNumber,
                                String address,
                                String vat,
                                String password) throws PropertyOwnerExistsException, InvalidInputException, MissingInputException {
        try {
//            if (!PatternService.EMAIL_PATTERN.matcher(email.trim()).matches()) {
//                throw new InvalidInputException("This is not a valid email");
//            }
//            if (!PatternService.PASSWORD_PATTERN.matcher(password.trim()).matches()) {
//                throw new InvalidInputException("This is not a valid password");
//            }
//            if (!PatternService.VAT_PATTERN.matcher(vat.trim()).matches()) {
//                throw new InvalidInputException("This is not a valid vat number");
//            }
//            if (!PatternService.PHONE_NUMBER_PATTERN.matcher(phoneNumber.trim()).matches()) {
//                throw new InvalidInputException("This is not a valid phone number");
//            }

            propertyOwner = PropertyOwner.builder()
                    .vat(Long.valueOf(vat))
                    .phoneNumber(Long.valueOf(phoneNumber))
                    .address(address)
                    .firstName(firstName)
                    .lastName(lastName)
                    .password(password)
                    .userName(userName)
                    .email(email)
                    .isActive(true)
                    .build();

            // throws a persistence exception
            propertyOwnerRepository.create(propertyOwner);
            return propertyOwner;
        } catch (PersistenceException e) {
            // a null value was passed
            if (e.getMessage().contains("PropertyValueException")) {
                throw new MissingInputException("There's input missing");

            // the user already exists
            } else {
                throw new PropertyOwnerExistsException("This user already exists");
            }
        }
    }

    @Override
    public void delete(Long id) throws OwnerNotFoundException{
        PropertyOwner existingOwner = get(id);
        propertyOwnerRepository.delete(existingOwner);
    }

    @Override
    public void softDelete(Long id) throws PersistenceException, OwnerNotFoundException {

        PropertyOwner existingOwner = get(id);
        
        existingOwner.setIsActive(false);
        propertyOwnerRepository.update(existingOwner);
    }
    
    @Override
    public List<PropertyOwner> findAll(){
        return propertyOwnerRepository.findAll();
    }

    @Override
    public PropertyOwner update(Long id, String firstName, String lastName, String email, String userName, String phoneNumber, String address, Long vat) {
        Optional<PropertyOwner> ownerToUpdateCheck = propertyOwnerRepository.findById(id);
        if (!ownerToUpdateCheck.isPresent() || !ownerToUpdateCheck.get().getIsActive()) {
            throw new OwnerNotFoundException("Owner with ID " + id + " does not exist or is inactive.");
        }

        PropertyOwner ownerToUpdate = ownerToUpdateCheck.get();

        if (firstName != null) {
            ownerToUpdate.setFirstName(firstName);
        }

        if (lastName != null) {
            ownerToUpdate.setLastName(lastName);
        }

        if (email != null) {
            ownerToUpdate.setEmail(email);
        }

        if (userName != null) {
            ownerToUpdate.setUserName(userName);
        }

        if (phoneNumber != null) {
            ownerToUpdate.setPhoneNumber(Long.valueOf(phoneNumber));
        }

        if (address != null) {
            ownerToUpdate.setAddress(address);
        }

        if (vat != null) {
            ownerToUpdate.setVat(vat);
        }

        propertyOwnerRepository.update(ownerToUpdate);

        return ownerToUpdate;

    }

    @Override
    public PropertyOwner findByEmail(String email) throws OwnerNotFoundException{
        return propertyOwnerRepository.searchByEmail(email);
    }

    @Override
    public PropertyOwner findByVat(Long vat) throws OwnerNotFoundException{
        return propertyOwnerRepository.searchByVat(vat);
    }

}

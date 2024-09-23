
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
    public void update(PropertyOwner updatedPropertyOwner) throws InvalidInputException, PropertyOwnerExistsException, OwnerNotFoundException {

        // checks if the id of the updatedPropertyOwner belongs to an existing user. Throws an exception otherwise
        PropertyOwner existingOwner = get(updatedPropertyOwner.getId());
        try {

            // only three fields can be updated
            if (!existingOwner.getFirstName().equals(updatedPropertyOwner.getFirstName()) ||
                    !existingOwner.getLastName().equals(updatedPropertyOwner.getLastName()) ||
                    !existingOwner.getPhoneNumber().equals(updatedPropertyOwner.getPhoneNumber()) ||
                    !existingOwner.getVat().equals(updatedPropertyOwner.getVat()) ||
                    !existingOwner.getUserName().equals(updatedPropertyOwner.getUserName()) ||
                     existingOwner.getIsActive()!=updatedPropertyOwner.getIsActive()){

                throw new InvalidInputException("You tried to update an unmodifiable field. You can only update the email, password and address");
            }

            // if the address is changed
            if (!existingOwner.getAddress().equals(updatedPropertyOwner.getAddress())) {
                existingOwner.setAddress(updatedPropertyOwner.getAddress());
            }

            // if the password is changed and of a certain pattern
//            if (!existingOwner.getPassword().equals(updatedPropertyOwner.getPassword())) {
//                if (!PatternService.PASSWORD_PATTERN.matcher(updatedPropertyOwner.getPassword().trim()).matches()) {
//                    throw new InvalidInputException("This is not a valid password");
//                }
//                existingOwner.setPassword(updatedPropertyOwner.getPassword());
//
//            }
//
//            // if the email is changed and of a certain pattern
//            if (!existingOwner.getEmail().equals(updatedPropertyOwner.getEmail())) {
//                if (!PatternService.EMAIL_PATTERN.matcher(updatedPropertyOwner.getEmail().trim()).matches()) {
//                    throw new InvalidInputException("This is not a valid email");
//                }
//
//                // if the email exists in the database it throws an exception
//                PropertyOwner existingOwnerEmail = null;
//                try {
//                    existingOwnerEmail = propertyOwnerRepository.searchByEmail(updatedPropertyOwner.getEmail());
//                    throw new PropertyOwnerExistsException("This email is already being used");
//                } catch (OwnerNotFoundException e) {
//                    existingOwner.setEmail(updatedPropertyOwner.getEmail());
//                }
//            }

            propertyOwnerRepository.update(existingOwner);

        // throws an exception if something goes wrong with the propertyOwnerRepository.update
        } catch (PersistenceException e) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }
    }

}

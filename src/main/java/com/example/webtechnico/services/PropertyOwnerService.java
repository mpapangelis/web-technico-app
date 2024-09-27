
package com.example.webtechnico.services;




import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.models.PropertyOwner;
import jakarta.persistence.PersistenceException;
import java.util.List;

public interface PropertyOwnerService {

    PropertyOwner get(Long id);

    PropertyOwner create(String firstName,
                         String lastName,
                         String email,
                         String userName,
                         String phoneNumber,
                         String address,
                         String vat,
                         String password);

    void delete(Long id);

    PropertyOwner update(Long id, String firstName, String lastName, String email, String userName, String phoneNumber, String address, Long vat);
    void softDelete(Long id) throws PersistenceException;
    List<PropertyOwner> findAll();
    PropertyOwner findByEmail(String email) throws OwnerNotFoundException;
    PropertyOwner findByVat(Long vat) throws OwnerNotFoundException;
    
}

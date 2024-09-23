package com.example.webtechnico.repositories;


import com.example.webtechnico.models.Property;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;


@Named("PropertyRepoDb")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class PropertyRepository implements Repository<Property> {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    @Override
    public Property create(Property property) {
        entityManager.persist(property);
        return property;
    }

    @Override
    public void update(Property property) {
        entityManager.getTransaction().begin();
        entityManager.merge(property);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Property property) {
        entityManager.getTransaction().begin();
        entityManager.remove(property);
        entityManager.getTransaction().commit();
    }
    
    public void softDelete(Property property) {
        entityManager.getTransaction().begin();
        property.setIsActive(false);
        entityManager.merge(property);
        entityManager.getTransaction().commit();
    }

    @Override
    public <V> Optional<Property> findById(V id) {
        try {
            Property property = entityManager.find(Property.class, id);
            if (property != null && property.getIsActive()) {
                return Optional.of(property);
            }
        } catch (Exception e) {
            log.debug("Property not found");
        }
        return Optional.empty();
    }

    @Override
    public List<Property> findAll() {
        TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.isActive = true", Property.class);
        return query.getResultList();
    }
    
    public Optional<Property> findByPropertyIdNumber(Long propertyId) {
        try {
            TypedQuery<Property> query = entityManager.createQuery(
                "SELECT p FROM Property p WHERE p.propertyId = :propertyId AND p.isActive = true", Property.class);
            query.setParameter("propertyId", propertyId);
            List<Property> result = query.getResultList();
            if (result.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(result.get(0));
            }
        } catch (Exception e) {
            log.debug("Property with this propertyid not found");
        }
        return Optional.empty();
    }
    
    public List<Property> findByOwnerVatNumber(Long vatNumber) {
        TypedQuery<Property> query = entityManager.createQuery(
            "SELECT p FROM Property p WHERE p.propertyOwner.vat = :vatNumber AND p.isActive = true", Property.class);
        query.setParameter("vatNumber", vatNumber);
        return query.getResultList();
    }

}

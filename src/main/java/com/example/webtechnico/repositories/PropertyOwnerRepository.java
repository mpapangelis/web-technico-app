package com.example.webtechnico.repositories;

import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.models.PropertyOwner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Named("PropertyOwnerRepoDb")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
//@AllArgsConstructor
public class PropertyOwnerRepository implements Repository<PropertyOwner> {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public PropertyOwner create(PropertyOwner propertyOwner) throws PersistenceException {
        entityManager.persist(propertyOwner);
        return propertyOwner;
    }

    @Transactional
    @Override
    public void update(PropertyOwner propertyOwner) throws PersistenceException {
        entityManager.merge(propertyOwner);
    }

    @Transactional
    @Override
    public void delete(PropertyOwner propertyOwner) {
        if (!entityManager.contains(propertyOwner)) {
            propertyOwner = entityManager.merge(propertyOwner);
        }
        entityManager.remove(propertyOwner);
    }

    @Override
    public <V> Optional<PropertyOwner> findById(V id) {
        try {
            PropertyOwner owner = entityManager.find(PropertyOwner.class, id);

            if (owner == null) {
                return Optional.empty();
            }
            return Optional.of(owner);
        } catch (Exception e) {
            log.debug("Owner not found");
        }
        return Optional.empty();
    }

    @Override
    public List<PropertyOwner> findAll() {
        TypedQuery<PropertyOwner> query
                = entityManager.createQuery("SELECT po FROM PropertyOwner po WHERE po.isActive=true", PropertyOwner.class);
        return query.getResultList();
    }

    public PropertyOwner searchByEmail(String email) {

        List<PropertyOwner> owner = entityManager.createQuery("SELECT po FROM PropertyOwner po WHERE po.email LIKE: givenEmail")
                .setParameter("givenEmail", email)
                .getResultList();

        if (owner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        if (owner.get(0).getIsActive() == false) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        return (PropertyOwner) owner.get(0);
    }//TODO

    public PropertyOwner searchByVat(Long vat) {

        List<PropertyOwner> owner = entityManager.createQuery("SELECT po FROM PropertyOwner po WHERE po.vat LIKE ?1")
                .setParameter(1, vat)
                .getResultList();

        if (owner.isEmpty()) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        if (owner.get(0).getIsActive() == false) {
            throw new OwnerNotFoundException("This is not an existing owner");
        }

        return (PropertyOwner) owner.get(0);
    }//TODO
}

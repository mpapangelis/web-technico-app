package com.example.webtechnico.repositories;


import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.models.PropertyRepair;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyRepairRepository implements Repository<PropertyRepair> {

    private EntityManager entityManager;

    public PropertyRepairRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PropertyRepair create(PropertyRepair propertyRepair) {
        entityManager.getTransaction().begin();
        entityManager.persist(propertyRepair);
        entityManager.getTransaction().commit();
        return propertyRepair;
    }

    @Override
    public void update(PropertyRepair v) throws PersistenceException {
        if (v instanceof PropertyRepair) {
            PropertyRepair repair = (PropertyRepair) v;
            entityManager.getTransaction().begin();
            entityManager.merge(repair);//Ενημερώνει τον πίνακα με τα νέα δεδομένα 
            entityManager.getTransaction().commit();
        }
    }

    @Override
    public void delete(PropertyRepair propertyRepair) {
        entityManager.getTransaction().begin();
        entityManager.remove(propertyRepair);
        entityManager.getTransaction().commit();
    }

    public List<PropertyRepair> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        TypedQuery<PropertyRepair> query;
        query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE isActive = TRUE AND r.submissionDate BETWEEN :startDate AND :endDate", PropertyRepair.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("There is no Repair between" + startDate + "and" + endDate);
        }
        return query.getResultList();
    }

    public List<PropertyRepair> searchBySubmissionDate(LocalDate submissionDate) {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE r.submissionDate = :submissionDate AND isActive = TRUE", PropertyRepair.class);
        query.setParameter("submissionDate", submissionDate);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("There is no Property Repair at" + submissionDate);
        }
        return query.getResultList();
    }

    public List<PropertyRepair> searchByOwnerId(Long ownerId) {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE r.ownerId = :ownerId AND isActive = TRUE", PropertyRepair.class);
        query.setParameter("ownerId", ownerId);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("There is no Owner :" + ownerId);
        }
        return query.getResultList();
    }

    @Override
    public List<PropertyRepair> findAll() {
        TypedQuery<PropertyRepair> query
                = entityManager.createQuery("SELECT po FROM PropertyRepair po WHERE isActive = TRUE", PropertyRepair.class);
        if (query.getResultList().isEmpty()) {
            throw new OwnerNotFoundException("No Property Repair Found...");
        }
        return query.getResultList();
    }

    @Override
    public <V> Optional<PropertyRepair> findById(V id) {
        try {
            entityManager.getTransaction().begin();
            PropertyRepair repair = entityManager.find(PropertyRepair.class, id);
            entityManager.getTransaction().commit();
            return Optional.of(repair);
        } catch (Exception e) {
            log.debug("Property's repair not found");
        }
        return Optional.empty();
    }

}

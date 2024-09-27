package com.example.webtechnico.repositories;


import com.example.webtechnico.exceptions.OwnerNotFoundException;
import com.example.webtechnico.models.PropertyRepair;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Named("PropertyRepairRepoDb")
@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class PropertyRepairRepository implements Repository<PropertyRepair> {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public PropertyRepair create(PropertyRepair propertyRepair) {
        entityManager.persist(propertyRepair);
        return propertyRepair;
    }

    @Transactional
    @Override
    public void update(PropertyRepair propertyRepair) {
        entityManager.merge(propertyRepair);
    }

    @Transactional
    @Override
    public void delete(PropertyRepair propertyRepair) {
        if (!entityManager.contains(propertyRepair)) {
            propertyRepair = entityManager.merge(propertyRepair);
        }
        entityManager.remove(propertyRepair);
    }
    
    @Transactional
    public boolean deleteById(Long id) {
        try {
            PropertyRepair repair = entityManager.find(PropertyRepair.class, id);
            if (repair != null) {
                entityManager.remove(repair);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public List<PropertyRepair> searchByDateRange(Date startDate, Date endDate) {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE isActive = TRUE AND DATE(r.submissionDate) BETWEEN DATE(:startDate) AND DATE(:endDate)", PropertyRepair.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<PropertyRepair> repairs = query.getResultList();
        if (repairs.isEmpty()) {
            throw new OwnerNotFoundException("There is no Repair between " + startDate + " and " + endDate);
        }
        return repairs;
    }

    public List<PropertyRepair> searchBySubmissionDate(Date submissionDate) {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE DATE(r.submissionDate) = DATE(:submissionDate) AND isActive = TRUE", PropertyRepair.class);
        query.setParameter("submissionDate", submissionDate);
        List<PropertyRepair> repairs = query.getResultList();
        if (repairs.isEmpty()) {
            throw new OwnerNotFoundException("There is no Property Repair on " + submissionDate);
        }
        return repairs;
    }

    public List<PropertyRepair> searchByOwnerId(Long ownerId) {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT r FROM PropertyRepair r WHERE r.property.propertyOwner.id = :ownerId AND r.isActive = TRUE", PropertyRepair.class);
        query.setParameter("ownerId", ownerId);
        List<PropertyRepair> repairs = query.getResultList();
        if (repairs.isEmpty()) {
            throw new OwnerNotFoundException("There is no Repair for owner with ID: " + ownerId);
        }
        return repairs;
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
            PropertyRepair repair = entityManager.find(PropertyRepair.class, id);
            return Optional.of(repair);
        } catch (Exception e) {
            log.debug("Property's repair not found");
        }
        return Optional.empty();
    }

}

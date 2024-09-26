package com.example.webtechnico.repositories;


import com.example.webtechnico.models.Admin;
import com.example.webtechnico.models.PropertyRepair;
import com.example.webtechnico.models.StatusOfRepairEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Named("AdminRepoDb")
@ApplicationScoped
@Slf4j
@NoArgsConstructor
public class AdminRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Admin createAdmin(Admin admin) {
        if (getAdmin() != null) {
            throw new IllegalStateException("Only one admin entry is allowed.");
        }
        entityManager.getTransaction().begin();
        entityManager.persist(admin);
        entityManager.getTransaction().commit();
        return admin;
    }

    public Admin changeAdmin(String username, String password) {
        Admin admin = getAdmin();
        if (admin == null) {
            throw new IllegalStateException("No admin entry found.");
        }
        entityManager.getTransaction().begin();
        admin.setUsername(username);
        admin.setPassword(password);
        entityManager.merge(admin);
        entityManager.getTransaction().commit();
        return admin;
    }

    public String getAdminUsername() {
        Admin admin = getAdmin();
        if (admin == null) {
            throw new IllegalStateException("No admin entry found.");
        }
        return admin.getUsername();
    }

    private Admin getAdmin() {
        TypedQuery<Admin> query = entityManager.createQuery("SELECT a FROM Admin a", Admin.class);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PropertyRepair> getActiveRepairs() {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT pr FROM PropertyRepair pr WHERE pr.isActive = true", PropertyRepair.class);
        return query.getResultList();
    }

    public List<PropertyRepair> getInactiveRepairs() {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT pr FROM PropertyRepair pr WHERE pr.isActive = false", PropertyRepair.class);
        return query.getResultList();
    }
    
      public List<PropertyRepair> getAllPendingRepairs() {
        TypedQuery<PropertyRepair> query = entityManager.createQuery(
                "SELECT pr FROM PropertyRepair pr WHERE pr.status = :status", PropertyRepair.class);
        query.setParameter("status", StatusOfRepairEnum.PENDING);
        return query.getResultList();
    }
}

package com.agrofarm.repository;

import com.agrofarm.entity.Purchase;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class PurchaseRepository extends GenericRepository<Purchase, Integer> {


    public PurchaseRepository() {
        super(Purchase.class);
    }


    public List<Purchase> findAllWithDetails() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT p FROM Purchase p " +
                                    "LEFT JOIN FETCH p.accountingOfParties " +
                                    "LEFT JOIN FETCH p.culture " +
                                    "ORDER BY p.id", Purchase.class)
                    .getResultList();
        }
    }


    public Optional<Purchase> findByIdWithDetails(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Purchase> result = em.createQuery(
                            "SELECT p FROM Purchase p " +
                                    "LEFT JOIN FETCH p.accountingOfParties " +
                                    "LEFT JOIN FETCH p.culture " +
                                    "WHERE p.id = :id", Purchase.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}
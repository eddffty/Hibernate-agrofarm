package com.agrofarm.repository;

import com.agrofarm.entity.AccountingOfParties;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class AccountingOfPartiesRepository extends GenericRepository<AccountingOfParties, Integer> {


    public AccountingOfPartiesRepository() {
        super(AccountingOfParties.class);
    }


    public List<AccountingOfParties> findAllWithPurchases() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT a FROM AccountingOfParties a " +
                                    "LEFT JOIN FETCH a.purchases " +
                                    "ORDER BY a.id", AccountingOfParties.class)
                    .getResultList();
        }
    }

    public Optional<AccountingOfParties> findByIdWithPurchases(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<AccountingOfParties> result = em.createQuery(
                            "SELECT DISTINCT a FROM AccountingOfParties a " +
                                    "LEFT JOIN FETCH a.purchases " +
                                    "WHERE a.id = :id", AccountingOfParties.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}
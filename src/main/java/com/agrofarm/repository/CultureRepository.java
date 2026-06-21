package com.agrofarm.repository;

import com.agrofarm.entity.Culture;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CultureRepository extends GenericRepository<Culture, Integer> {


    public CultureRepository() {
        super(Culture.class);
    }


    public List<Culture> findAllWithDetails() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT c FROM Culture c " +
                                    "LEFT JOIN FETCH c.shelf " +
                                    "ORDER BY c.id", Culture.class)
                    .getResultList();
        }
    }


    public Optional<Culture> findByIdWithDetails(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Culture> result = em.createQuery(
                            "SELECT DISTINCT c FROM Culture c " +
                                    "LEFT JOIN FETCH c.shelf " +
                                    "WHERE c.id = :id", Culture.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}

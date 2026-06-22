package com.agrofarm.repository;

import com.agrofarm.entity.Cell;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class CellRepository extends GenericRepository<Cell, Integer> {

    public CellRepository() {
        super(Cell.class);
    }


    public List<Cell> findAllWithDetails() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT c FROM Cell c " +
                                    "LEFT JOIN FETCH c.shelf " +
                                    "LEFT JOIN FETCH c.sensors " +
                                    "ORDER BY c.id", Cell.class)
                    .getResultList();
        }
    }


    public Optional<Cell> findByIdWithDetails(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Cell> result = em.createQuery(
                            "SELECT DISTINCT c FROM Cell c " +
                                    "LEFT JOIN FETCH c.shelf " +
                                    "LEFT JOIN FETCH c.sensors " +
                                    "WHERE c.id = :id", Cell.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}
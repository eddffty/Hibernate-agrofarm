package com.agrofarm.repository;

import com.agrofarm.entity.Shelf;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ShelfRepository extends GenericRepository<Shelf, Integer>{

    public ShelfRepository() {
        super(Shelf.class);
    }

    public List<Shelf> findAllWithDetails() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Shelf> shelves = em.createQuery(
                            "SELECT DISTINCT s FROM Shelf s " +
                                    "LEFT JOIN FETCH s.cultures " +
                                    "ORDER BY s.id", Shelf.class)
                    .getResultList();

            if (!shelves.isEmpty()) {
                em.createQuery(
                                "SELECT DISTINCT s FROM Shelf s " +
                                        "LEFT JOIN FETCH s.cells " +
                                        "WHERE s IN :shelves", Shelf.class)
                        .setParameter("shelves", shelves)
                        .getResultList();
            }

            return shelves;
        }
    }

    public Optional<Shelf> findByIdWithDetails(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Shelf> result = em.createQuery(
                            "SELECT DISTINCT s FROM Shelf s " +
                                    "LEFT JOIN FETCH s.cultures " +
                                    "WHERE s.id = :id", Shelf.class)
                    .setParameter("id", id)
                    .getResultList();

            if (result.isEmpty()) {
                return Optional.empty();
            }

            Shelf shelf = result.get(0);
            em.createQuery(
                            "SELECT DISTINCT s FROM Shelf s " +
                                    "LEFT JOIN FETCH s.cells " +
                                    "WHERE s.id = :id", Shelf.class)
                    .setParameter("id", id)
                    .getResultList();

            return Optional.of(shelf);
        }
    }
}

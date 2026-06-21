package com.agrofarm.repository;

import com.agrofarm.entity.Equipment;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;


public class EquipmentRepository extends GenericRepository<Equipment, Integer> {

    public EquipmentRepository() {
        super(Equipment.class);
    }


    public List<Equipment> findAllWithSensors() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT e FROM Equipment e " +
                                    "LEFT JOIN FETCH e.sensor " +
                                    "ORDER BY e.id", Equipment.class)
                    .getResultList();
        }
    }


    public Optional<Equipment> findByIdWithSensor(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Equipment> result = em.createQuery(
                            "SELECT e FROM Equipment e " +
                                    "LEFT JOIN FETCH e.sensor " +
                                    "WHERE e.id = :id", Equipment.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }
}
package com.agrofarm.repository;

import com.agrofarm.entity.Sensor;
import com.agrofarm.entity.Equipment;
import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class SensorRepository extends GenericRepository<Sensor, Integer> {


    public SensorRepository() {
        super(Sensor.class);
    }

    public List<Sensor> findAllWithDetails() {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT s FROM Sensor s " +
                                    "LEFT JOIN FETCH s.cell " +
                                    "LEFT JOIN FETCH s.equipments " +
                                    "ORDER BY s.id", Sensor.class)
                    .getResultList();
        }
    }


    public Optional<Sensor> findByIdWithDetails(int id) {
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Sensor> result = em.createQuery(
                            "SELECT DISTINCT s FROM Sensor s " +
                                    "LEFT JOIN FETCH s.cell " +
                                    "LEFT JOIN FETCH s.equipments " +
                                    "WHERE s.id = :id", Sensor.class)
                    .setParameter("id", id)
                    .getResultList();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }


    public void addEquipment(int sensorId, Equipment equipment) {
        EntityManager em = HibernateUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();


            Sensor sensor = em.find(Sensor.class, sensorId);

            if (sensor != null && equipment != null) {

                equipment.setSensor(sensor);
                sensor.getEquipments().add(equipment);

                em.persist(equipment);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
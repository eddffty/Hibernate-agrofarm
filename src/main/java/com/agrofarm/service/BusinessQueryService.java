package com.agrofarm.service;

import com.agrofarm.util.HibernateUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;


public class BusinessQueryService {


    public void totalCostByParty() {
        printHeader("Общая стоимость закупок по партиям");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            // Навигация по связям: p.accountingOfParties.id
            List<Object[]> results = em.createQuery("""
                    SELECT p.accountingOfParties.id,
                           COUNT(p),
                           SUM(p.accountingOfParties.cost)
                    FROM Purchase p
                    GROUP BY p.accountingOfParties.id
                    ORDER BY SUM(p.accountingOfParties.cost) DESC
                    """, Object[].class).getResultList();

            System.out.printf("     %-12s %-15s %-20s%n", "ID Партии", "Кол-во закупок", "Общая стоимость (₽)");
            System.out.println("     " + "─".repeat(50));
            for (Object[] row : results) {
                System.out.printf("     %-12d %-15d %-20.2f%n", asInt(row[0]), asLong(row[1]), row[2]);
            }
        }
        printDivider();
    }


    public void shelfOccupancy() {
        printHeader("Заполняемость полок ячейками");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT s.level,
                           s.cultureName,
                           COUNT(c),
                           SUM(c.capacity)
                    FROM Shelf s
                    LEFT JOIN Cell c ON c.shelf = s
                    GROUP BY s.level, s.cultureName
                    ORDER BY SUM(c.capacity) DESC
                    """, Object[].class).getResultList();

            System.out.printf("     %-10s %-20s %-15s %-15s%n", "Уровень", "Культура полки", "Ячеек", "Общая вместимость");
            System.out.println("     " + "─".repeat(65));
            for (Object[] row : results) {
                long cellCount = asLong(row[2]);
                BigDecimal totalCapacity = row[3] != null ? BigDecimal.valueOf(((Number) row[3]).longValue()) : BigDecimal.ZERO;
                System.out.printf("     %-10d %-20s %-15d %-15s%n", asInt(row[0]), row[1], cellCount, totalCapacity);
            }
        }
        printDivider();
    }


    public void criticalSensors() {
        printHeader("Топ датчиков по температуре");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT s.name, s.temperature, s.cell.shelf.level
                    FROM Sensor s
                    ORDER BY s.temperature DESC
                    """, Object[].class)
                    .setMaxResults(3) // LIMIT 3
                    .getResultList();

            System.out.printf("     %-25s %-15s %-10s%n", "Датчик", "Температура °C", "Уровень полки");
            System.out.println("     " + "─".repeat(55));
            for (Object[] row : results) {
                System.out.printf("     %-25s %-15.1f %-10d%n", row[0], asDouble(row[1]), asInt(row[2]));
            }
        }
        printDivider();
    }


    public void shelfCulturesCount() {
        printHeader("Полки и количество культур");
        try (EntityManager em = HibernateUtil.createEntityManager()) {

            List<Object[]> results = em.createQuery("""
                    SELECT s.level, s.cultureName, SIZE(s.cultures)
                    FROM Shelf s
                    ORDER BY SIZE(s.cultures) DESC
                    """, Object[].class).getResultList();

            System.out.printf("     %-10s %-25s %-10s%n", "Уровень", "Культура по умолчанию", "Всего культур");
            System.out.println("     " + "─".repeat(50));
            for (Object[] row : results) {
                System.out.printf("     %-10d %-25s %-10d%n", asInt(row[0]), row[1], asInt(row[2]));
            }
        }
        printDivider();
    }


    public void sensorsWithDetails() {
        printHeader("Датчики с привязкой к ячейкам и уровням");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT sn.name, sn.humidityLevel, sn.cell.id, sn.cell.shelf.level
                    FROM Sensor sn
                    ORDER BY sn.cell.shelf.level
                    """, Object[].class).getResultList();

            System.out.printf("     %-25s %-15s %-12s %-10s%n", "Датчик", "Влажность %", "ID Ячейки", "Уровень полки");
            System.out.println("     " + "─".repeat(65));
            for (Object[] row : results) {
                System.out.printf("     %-25s %-15.1f %-12d %-10d%n", row[0], asDouble(row[1]), asInt(row[2]), asInt(row[3]));
            }
        }
        printDivider();
    }


    public void supplierPopularity() {
        printHeader("Рейтинг поставщиков по закупкам");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT p.supplier, COUNT(p)
                    FROM Purchase p
                    GROUP BY p.supplier
                    ORDER BY COUNT(p) DESC
                    """, Object[].class).getResultList();

            System.out.printf("     %-25s %-15s%n", "Поставщик семян", "Кол-во закупок");
            System.out.println("     " + "─".repeat(40));
            for (Object[] row : results) {
                System.out.printf("     %-25s %-15d%n", row[0], asLong(row[1]));
            }
        }
        printDivider();
    }


    public void avgHumidityByCell() {
        printHeader("Средняя влажность по ячейкам");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT sn.cell.id,
                           COUNT(sn),
                           AVG(sn.humidityLevel)
                    FROM Sensor sn
                    GROUP BY sn.cell.id
                    ORDER BY AVG(sn.humidityLevel) DESC
                    """, Object[].class).getResultList();

            System.out.printf("     %-15s %-15s %-20s%n", "ID Ячейки", "Кол-во датчиков", "Средняя влажность %");
            System.out.println("     " + "─".repeat(55));
            for (Object[] row : results) {
                System.out.printf("     %-15d %-15d %-20.2f%n", asInt(row[0]), asLong(row[1]), asDouble(row[2]));
            }
        }
        printDivider();
    }


    public void culturesWithoutPurchases() {
        printHeader("Культуры без оформленных закупок");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT c.name, c.compositionSolution
                    FROM Culture c
                    WHERE c NOT IN (SELECT p.culture FROM Purchase p)
                    ORDER BY c.name
                    """, Object[].class).getResultList();

            System.out.printf("     %-25s %-25s%n", "Название культуры", "Питательный раствор");
            System.out.println("     " + "─".repeat(52));
            if (results.isEmpty()) {
                System.out.println("     (Все культуры имеют оформленные закупки)");
            }
            for (Object[] row : results) {
                System.out.printf("     %-25s %-25s%n", row[0], row[1] != null ? row[1] : "—");
            }
        }
        printDivider();
    }


    public void freeCellsForShelf(int shelfId) {
        printHeader("Поиск ячеек для полки ID=" + shelfId);
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT c.id, c.capacity
                    FROM Cell c
                    WHERE c.shelf.id = :shelfId
                    ORDER BY c.id
                    """, Object[].class)
                    .setParameter("shelfId", shelfId)
                    .getResultList();

            System.out.printf("     %-15s %-15s%n", "ID Ячейки", "Вместимость");
            System.out.println("     " + "─".repeat(30));
            for (Object[] row : results) {
                System.out.printf("     %-15d %-15d%n", asInt(row[0]), asInt(row[1]));
            }
            System.out.printf("     Всего ячеек на полке: %d%n", results.size());
        }
        printDivider();
    }


    public void shelfStatistics() {
        printHeader("Общая статистика по ярусам агрофермы");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            List<Object[]> results = em.createQuery("""
                    SELECT s.level,
                           (SELECT COUNT(c) FROM Cell c WHERE c.shelf = s),
                           (SELECT COUNT(sn) FROM Sensor sn WHERE sn.cell.shelf = s)
                    FROM Shelf s
                    ORDER BY s.level
                    """, Object[].class).getResultList();

            System.out.printf("     %-12s %-15s %-15s%n", "Ярус (Уровень)", "Кол-во ячеек", "Кол-во датчиков");
            System.out.println("     " + "─".repeat(45));
            for (Object[] row : results) {
                System.out.printf("     %-12d %-15d %-15d%n", asInt(row[0]), asLong(row[1]), asLong(row[2]));
            }
        }
        printDivider();
    }

    public void runAll() {
        totalCostByParty();
        shelfOccupancy();
        criticalSensors();
        shelfCulturesCount();
        sensorsWithDetails();
        supplierPopularity();
        avgHumidityByCell();
        culturesWithoutPurchases();
        freeCellsForShelf(1);
        shelfStatistics();
    }

    private void printHeader(String title) {
        System.out.println();
        System.out.println("╔" + "═".repeat(title.length() + 4) + "╗");
        System.out.println("║  " + title + "  ║");
        System.out.println("╚" + "═".repeat(title.length() + 4) + "╝");
    }

    private void printDivider() {
        System.out.println("─".repeat(80));
    }

    private int asInt(Object value) {
        return ((Number) value).intValue();
    }

    private long asLong(Object value) {
        return ((Number) value).longValue();
    }

    private double asDouble(Object value) {
        return ((Number) value).doubleValue();
    }
}

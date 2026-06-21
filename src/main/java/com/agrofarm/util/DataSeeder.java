package com.agrofarm.util;

import com.agrofarm.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public final class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private DataSeeder() {}

    public static void seed() {
        EntityManager em = HibernateUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();


            Long shelvesCount = em.createQuery("SELECT COUNT(s) FROM Shelf s", Long.class)
                    .getSingleResult();
            if (shelvesCount > 0) {
                tx.commit();
                log.info("Начальные данные агрофермы уже есть, заполнение пропущено");
                return;
            }


            Shelf shelf1 = new Shelf("Томат Черри", 1);
            Shelf shelf2 = new Shelf("Клубника Наша", 2);
            Shelf shelf3 = new Shelf("Огурец Хрустящий", 3);
            List.of(shelf1, shelf2, shelf3).forEach(em::persist);

            em.flush();


            Culture tomato = new Culture("Томат Черри", "Сине-красный спектр", 2, "Раствор №1 (Старт)");
            tomato.setShelf(shelf1);

            Culture strawberry = new Culture("Клубника Наша", "Красно-синий спектр (Fruity)", 3, "N-P-K 15-15-30 + Mg");
            strawberry.setShelf(shelf2);

            Culture cucumber = new Culture("Огурец Хрустящий", "Белый полный спектр", 1, "Раствор №3 (Органический)");
            cucumber.setShelf(shelf3);

            List.of(tomato, strawberry, cucumber).forEach(em::persist);


            Cell cell1 = new Cell(100);
            cell1.setShelf(shelf1);

            Cell cell2 = new Cell(200);
            cell2.setShelf(shelf2);

            Cell cell3 = new Cell(150);
            cell3.setShelf(shelf3);
            List.of(cell1, cell2, cell3).forEach(em::persist);


            em.flush();


            Sensor sensor1 = new Sensor("Датчик Влажности №1", BigDecimal.valueOf(6.55), BigDecimal.valueOf(8.20), BigDecimal.valueOf(6.50));
            sensor1.setCell(cell1);

            Sensor sensor2 = new Sensor("Термометр №2", BigDecimal.valueOf(6.00), BigDecimal.valueOf(7.50), BigDecimal.valueOf(6.80));
            sensor2.setCell(cell2);

            Sensor sensor3 = new Sensor("Датчик Азота №5", BigDecimal.valueOf(4.52), BigDecimal.valueOf(6.90), BigDecimal.valueOf(6.20));
            sensor3.setCell(cell3);
            List.of(sensor1, sensor2, sensor3).forEach(em::persist);


            em.flush();


            Equipment eq1 = new Equipment("Заменена батарейка питания и откалиброван заново");
            eq1.setSensor(sensor1);

            Equipment eq2 = new Equipment("Первичная установка и проверка связи с сервером");
            eq2.setSensor(sensor2);

            Equipment eq3 = new Equipment("Откалиброван по ГОСТу");
            eq3.setSensor(sensor3);
            List.of(eq1, eq2, eq3).forEach(em::persist);



            AccountingOfParties party1 = new AccountingOfParties(BigDecimal.valueOf(15000.50), List.of("Томат Черри", "Огурец Хрустящий"));
            AccountingOfParties party2 = new AccountingOfParties(BigDecimal.valueOf(28000.00), List.of("Клубника Наша"));
            List.of(party1, party2).forEach(em::persist);

            em.flush();


            Purchase purchase1 = new Purchase(BigDecimal.valueOf(0.95), "ООО АгроСемена Юг");
            purchase1.setAccountingOfParties(party1);
            purchase1.setCulture(tomato);

            Purchase purchase2 = new Purchase(BigDecimal.valueOf(0.98), "АО Клубничные Поля");
            purchase2.setAccountingOfParties(party2);
            purchase2.setCulture(strawberry);

            List.of(purchase1, purchase2).forEach(em::persist);


            tx.commit();
            log.info("Начальные данные для Агрофермы (Hibernate) успешно добавлены");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}

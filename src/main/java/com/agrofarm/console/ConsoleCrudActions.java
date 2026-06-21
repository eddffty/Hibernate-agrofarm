package com.agrofarm.console;

import com.agrofarm.entity.AccountingOfParties;
import com.agrofarm.entity.Cell;
import com.agrofarm.entity.Culture;
import com.agrofarm.entity.Equipment;
import com.agrofarm.entity.Purchase;
import com.agrofarm.entity.Sensor;
import com.agrofarm.entity.Shelf;
import com.agrofarm.repository.AccountingOfPartiesRepository;
import com.agrofarm.repository.CellRepository;
import com.agrofarm.repository.CultureRepository;
import com.agrofarm.repository.EquipmentRepository;
import com.agrofarm.repository.PurchaseRepository;
import com.agrofarm.repository.SensorRepository;
import com.agrofarm.repository.ShelfRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleCrudActions {

    private final Scanner scanner;

    private final ShelfRepository shelfRepo = new ShelfRepository();
    private final CultureRepository cultureRepo = new CultureRepository();
    private final CellRepository cellRepo = new CellRepository();
    private final SensorRepository sensorRepo = new SensorRepository();
    private final EquipmentRepository equipmentRepo = new EquipmentRepository();
    private final AccountingOfPartiesRepository partiesRepo = new AccountingOfPartiesRepository();
    private final PurchaseRepository purchaseRepo = new PurchaseRepository();

    public ConsoleCrudActions(Scanner scanner) {
        this.scanner = scanner;
    }

    public void create() {
        printEntityMenu("CREATE");
        switch (readChoice()) {
            case "1" -> createShelf();
            case "2" -> createCulture();
            case "3" -> createCell();
            case "4" -> createSensor();
            case "5" -> createEquipment();
            case "6" -> createParty();
            case "7" -> createPurchase();
            default -> printInvalidChoice();
        }
    }

    public void read() {
        printEntityMenu("READ");
        switch (readChoice()) {
            case "1" -> printShelves();
            case "2" -> printCultures();
            case "3" -> printCells();
            case "4" -> printSensors();
            case "5" -> printEquipment();
            case "6" -> printParties();
            case "7" -> printPurchases();
            default -> printInvalidChoice();
        }
    }

    public void update() {
        printEntityMenu("UPDATE");
        switch (readChoice()) {
            case "1" -> updateShelf();
            case "2" -> updateCulture();
            case "3" -> updateCell();
            case "4" -> updateSensor();
            case "5" -> updateEquipment();
            case "6" -> updateParty();
            case "7" -> updatePurchase();
            default -> printInvalidChoice();
        }
    }

    public void delete() {
        printEntityMenu("DELETE");
        switch (readChoice()) {
            case "1" -> deleteById("полка", shelfRepo::deleteById);
            case "2" -> deleteById("культура", cultureRepo::deleteById);
            case "3" -> deleteById("ячейка", cellRepo::deleteById);
            case "4" -> deleteById("датчик", sensorRepo::deleteById);
            case "5" -> deleteById("оборудование", equipmentRepo::deleteById);
            case "6" -> deleteById("партия", partiesRepo::deleteById);
            case "7" -> deleteById("закупка", purchaseRepo::deleteById);
            default -> printInvalidChoice();
        }
    }

    private void createShelf() {
        String cultureName = readRequiredText("Название культуры на полке: ");
        int level = readPositiveInt("Уровень полки: ");

        Shelf shelf = shelfRepo.save(new Shelf(cultureName, level));
        System.out.printf("Полка создана: id=%d%n", shelf.getId());
    }

    private void createCulture() {
        printShelves();
        Shelf shelf = requireShelf(readPositiveInt("ID полки: "));

        Culture culture = new Culture(
                readRequiredText("Название культуры: "),
                readRequiredText("Световой спектр: "),
                readPositiveInt("Частота полива: "),
                readRequiredText("Состав раствора: ")
        );
        culture.setShelf(shelf);
        cultureRepo.save(culture);
        System.out.printf("Культура создана: id=%d%n", culture.getId());
    }

    private void createCell() {
        printShelves();
        Shelf shelf = requireShelf(readPositiveInt("ID полки: "));

        Cell cell = new Cell(readPositiveInt("Вместимость ячейки: "));
        cell.setShelf(shelf);
        cellRepo.save(cell);
        System.out.printf("Ячейка создана: id=%d%n", cell.getId());
    }

    private void createSensor() {
        printCells();
        Cell cell = requireCell(readPositiveInt("ID ячейки: "));

        Sensor sensor = new Sensor(
                readRequiredText("Название датчика: "),
                readPositiveDecimal("Влажность (например 6.55): "),
                readPositiveDecimal("Температура (например 8.20): "),
                readPositiveDecimal("Кислотность почвы (например 6.50): ")
        );
        sensor.setCell(cell);
        sensorRepo.save(sensor);
        System.out.printf("Датчик создан: id=%d%n", sensor.getId());
    }

    private void createEquipment() {
        printSensors();
        int sensorId = readPositiveInt("ID датчика: ");
        requireSensor(sensorId);

        Equipment equipment = new Equipment(readRequiredText("История изменений/обслуживания: "));
        sensorRepo.addEquipment(sensorId, equipment);
        System.out.println("Запись оборудования создана.");
    }

    private void createParty() {
        AccountingOfParties party = new AccountingOfParties(
                readPositiveDecimal("Стоимость партии: "),
                readTextList("Культуры через запятую: ")
        );
        partiesRepo.save(party);
        System.out.printf("Партия создана: id=%d%n", party.getId());
    }

    private void createPurchase() {
        printParties();
        AccountingOfParties party = requireParty(readPositiveInt("ID партии: "));
        printCultures();
        Culture culture = requireCulture(readPositiveInt("ID культуры: "));

        Purchase purchase = new Purchase(
                readPositiveDecimal("Процент всхожести (например 0.95): "),
                readRequiredText("Поставщик: ")
        );
        purchase.setAccountingOfParties(party);
        purchase.setCulture(culture);
        purchaseRepo.save(purchase);
        System.out.printf("Закупка создана: id=%d%n", purchase.getId());
    }

    private void updateShelf() {
        printShelves();
        Shelf shelf = requireShelf(readPositiveInt("ID полки: "));
        shelf.setCultureName(readRequiredText("Новое название культуры на полке: "));
        shelf.setLevel(readPositiveInt("Новый уровень: "));
        shelfRepo.update(shelf);
        System.out.println("Полка обновлена.");
    }

    private void updateCulture() {
        printCultures();
        Culture culture = requireCulture(readPositiveInt("ID культуры: "));
        printShelves();
        culture.setShelf(requireShelf(readPositiveInt("Новый ID полки: ")));
        culture.setName(readRequiredText("Новое название культуры: "));
        culture.setLightSpectre(readRequiredText("Новый световой спектр: "));
        culture.setWateringFrequency(readPositiveInt("Новая частота полива: "));
        culture.setCompositionSolution(readRequiredText("Новый состав раствора: "));
        cultureRepo.update(culture);
        System.out.println("Культура обновлена.");
    }

    private void updateCell() {
        printCells();
        Cell cell = requireCell(readPositiveInt("ID ячейки: "));
        printShelves();
        cell.setShelf(requireShelf(readPositiveInt("Новый ID полки: ")));
        cell.setCapacity(readPositiveInt("Новая вместимость: "));
        cellRepo.update(cell);
        System.out.println("Ячейка обновлена.");
    }

    private void updateSensor() {
        printSensors();
        Sensor sensor = requireSensor(readPositiveInt("ID датчика: "));
        printCells();
        sensor.setCell(requireCell(readPositiveInt("Новый ID ячейки: ")));
        sensor.setName(readRequiredText("Новое название датчика: "));
        sensor.setHumidityLevel(readPositiveDecimal("Новая влажность: "));
        sensor.setTemperature(readPositiveDecimal("Новая температура: "));
        sensor.setSoilAcidityLevel(readPositiveDecimal("Новая кислотность почвы: "));
        sensorRepo.update(sensor);
        System.out.println("Датчик обновлен.");
    }

    private void updateEquipment() {
        printEquipment();
        Equipment equipment = requireEquipment(readPositiveInt("ID оборудования: "));
        printSensors();
        equipment.setSensor(requireSensor(readPositiveInt("Новый ID датчика: ")));
        equipment.setChangeHistory(readRequiredText("Новая история изменений: "));
        equipmentRepo.update(equipment);
        System.out.println("Оборудование обновлено.");
    }

    private void updateParty() {
        printParties();
        AccountingOfParties party = requireParty(readPositiveInt("ID партии: "));
        party.setCost(readPositiveDecimal("Новая стоимость партии: "));
        party.setCultures(readTextList("Новые культуры через запятую: "));
        partiesRepo.update(party);
        System.out.println("Партия обновлена.");
    }

    private void updatePurchase() {
        printPurchases();
        Purchase purchase = requirePurchase(readPositiveInt("ID закупки: "));
        printParties();
        purchase.setAccountingOfParties(requireParty(readPositiveInt("Новый ID партии: ")));
        printCultures();
        purchase.setCulture(requireCulture(readPositiveInt("Новый ID культуры: ")));
        purchase.setGerminationPercentage(readPositiveDecimal("Новый процент всхожести: "));
        purchase.setSupplier(readRequiredText("Новый поставщик: "));
        purchaseRepo.update(purchase);
        System.out.println("Закупка обновлена.");
    }

    private void printShelves() {
        System.out.println();
        System.out.println("Полки:");
        System.out.printf("%-5s %-8s %-30s %-10s %-10s%n", "ID", "Уровень", "Культура", "Культур", "Ячеек");
        for (Shelf shelf : shelfRepo.findAllWithDetails()) {
            System.out.printf("%-5d %-8d %-30s %-10d %-10d%n",
                    shelf.getId(), shelf.getLevel(), shelf.getCultureName(),
                    shelf.getCultures().size(), shelf.getCells().size());
        }
    }

    private void printCultures() {
        System.out.println();
        System.out.println("Культуры:");
        System.out.printf("%-5s %-30s %-24s %-10s %-8s%n", "ID", "Название", "Спектр", "Полив", "Полка");
        for (Culture culture : cultureRepo.findAllWithDetails()) {
            System.out.printf("%-5d %-30s %-24s %-10d %-8d%n",
                    culture.getId(), culture.getName(), culture.getLightSpectre(),
                    culture.getWateringFrequency(), culture.getShelf().getId());
        }
    }

    private void printCells() {
        System.out.println();
        System.out.println("Ячейки:");
        System.out.printf("%-5s %-14s %-8s %-10s%n", "ID", "Вместимость", "Полка", "Датчиков");
        for (Cell cell : cellRepo.findAllWithDetails()) {
            System.out.printf("%-5d %-14d %-8d %-10d%n",
                    cell.getId(), cell.getCapacity(), cell.getShelf().getId(), cell.getSensors().size());
        }
    }

    private void printSensors() {
        System.out.println();
        System.out.println("Датчики:");
        System.out.printf("%-5s %-30s %-10s %-10s %-10s %-8s%n", "ID", "Название", "Влажн.", "Темп.", "Кислот.", "Ячейка");
        for (Sensor sensor : sensorRepo.findAllWithDetails()) {
            System.out.printf("%-5d %-30s %-10s %-10s %-10s %-8d%n",
                    sensor.getId(), sensor.getName(), sensor.getHumidityLevel(),
                    sensor.getTemperature(), sensor.getSoilAcidityLevel(), sensor.getCell().getId());
        }
    }

    private void printEquipment() {
        System.out.println();
        System.out.println("Оборудование:");
        System.out.printf("%-5s %-8s %-50s%n", "ID", "Датчик", "История изменений");
        for (Equipment equipment : equipmentRepo.findAllWithSensors()) {
            System.out.printf("%-5d %-8d %-50s%n",
                    equipment.getId(), equipment.getSensor().getId(), equipment.getChangeHistory());
        }
    }

    private void printParties() {
        System.out.println();
        System.out.println("Партии:");
        System.out.printf("%-5s %-14s %-40s %-10s%n", "ID", "Стоимость", "Культуры", "Закупок");
        for (AccountingOfParties party : partiesRepo.findAllWithPurchases()) {
            System.out.printf("%-5d %-14s %-40s %-10d%n",
                    party.getId(), party.getCost(), party.getCultures(), party.getPurchases().size());
        }
    }

    private void printPurchases() {
        System.out.println();
        System.out.println("Закупки:");
        System.out.printf("%-5s %-8s %-28s %-12s %-30s%n", "ID", "Партия", "Культура", "Всхож.", "Поставщик");
        for (Purchase purchase : purchaseRepo.findAllWithDetails()) {
            System.out.printf("%-5d %-8d %-28s %-12s %-30s%n",
                    purchase.getId(), purchase.getAccountingOfParties().getId(),
                    purchase.getCulture().getName(), purchase.getGerminationPercentage(), purchase.getSupplier());
        }
    }

    private Shelf requireShelf(int id) {
        return shelfRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Полка с id=" + id + " не найдена"));
    }

    private Culture requireCulture(int id) {
        return cultureRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Культура с id=" + id + " не найдена"));
    }

    private Cell requireCell(int id) {
        return cellRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ячейка с id=" + id + " не найдена"));
    }

    private Sensor requireSensor(int id) {
        return sensorRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Датчик с id=" + id + " не найден"));
    }

    private Equipment requireEquipment(int id) {
        return equipmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Оборудование с id=" + id + " не найдено"));
    }

    private AccountingOfParties requireParty(int id) {
        return partiesRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Партия с id=" + id + " не найдена"));
    }

    private Purchase requirePurchase(int id) {
        return purchaseRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Закупка с id=" + id + " не найдена"));
    }

    private void deleteById(String title, DeleteAction action) {
        int id = readPositiveInt("ID: ");
        boolean deleted = action.delete(id);
        if (deleted) {
            System.out.println("Удалено: " + title + " id=" + id);
        } else {
            System.out.println("Запись не найдена.");
        }
    }

    private void printEntityMenu(String action) {
        System.out.println();
        System.out.println(action + ": выберите сущность");
        System.out.println("1. Полка");
        System.out.println("2. Культура");
        System.out.println("3. Ячейка");
        System.out.println("4. Датчик");
        System.out.println("5. Оборудование");
        System.out.println("6. Партия");
        System.out.println("7. Закупка");
        System.out.print("Выберите пункт: ");
    }

    private String readChoice() {
        return scanner.nextLine().trim();
    }

    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("Значение не может быть пустым.");
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Введите число больше нуля.");
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private BigDecimal readPositiveDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace(',', '.');
            try {
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    return value;
                }
                System.out.println("Введите число больше нуля.");
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
    }

    private List<String> readTextList(String prompt) {
        while (true) {
            String input = readRequiredText(prompt);
            List<String> values = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .toList();
            if (!values.isEmpty()) {
                return values;
            }
            System.out.println("Введите хотя бы одно значение.");
        }
    }

    private void printInvalidChoice() {
        System.out.println("Нет такого пункта меню.");
    }

    @FunctionalInterface
    private interface DeleteAction {
        boolean delete(int id);
    }
}

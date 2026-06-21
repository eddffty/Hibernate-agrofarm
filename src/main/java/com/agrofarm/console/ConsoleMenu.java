package com.agrofarm.console;

import com.agrofarm.service.BusinessQueryService;
import com.agrofarm.util.DataSeeder;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner;
    private final ConsoleCrudActions crudActions;
    private final BusinessQueryService queryService;

    public ConsoleMenu(Scanner scanner) {
        this.scanner = scanner;
        this.crudActions = new ConsoleCrudActions(scanner);
        this.queryService = new BusinessQueryService();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            switch (readChoice()) {
                case "1" -> runCrudMenu();
                case "2" -> runBusinessQueryMenu();
                case "3" -> {
                    DataSeeder.seed();
                    System.out.println("Начальные данные проверены.");
                    waitForEnter();
                }
                case "0" -> running = false;
                default -> printInvalidChoice();
            }
        }
    }

    private void runCrudMenu() {
        boolean running = true;
        while (running) {
            printCrudMenu();
            try {
                switch (readChoice()) {
                    case "1" -> crudActions.create();
                    case "2" -> crudActions.read();
                    case "3" -> crudActions.update();
                    case "4" -> crudActions.delete();
                    case "0" -> running = false;
                    default -> printInvalidChoice();
                }
            } catch (Exception e) {
                printError(e);
            }
            if (running) {
                waitForEnter();
            }
        }
    }

    private void runBusinessQueryMenu() {
        boolean running = true;
        while (running) {
            printBusinessQueryMenu();
            try {
                switch (readChoice()) {
                    case "1" -> queryService.totalCostByParty();
                    case "2" -> queryService.shelfOccupancy();
                    case "3" -> queryService.criticalSensors();
                    case "4" -> queryService.shelfCulturesCount();
                    case "5" -> queryService.sensorsWithDetails();
                    case "6" -> queryService.supplierPopularity();
                    case "7" -> queryService.avgHumidityByCell();
                    case "8" -> queryService.culturesWithoutPurchases();
                    case "9" -> queryService.freeCellsForShelf(readInt("Введите ID полки: "));
                    case "10" -> queryService.shelfStatistics();
                    case "11" -> queryService.runAll();
                    case "0" -> running = false;
                    default -> printInvalidChoice();
                }
            } catch (Exception e) {
                printError(e);
            }
            if (running) {
                waitForEnter();
            }
        }
    }

    private void printMainMenu() {
        printTitle("АГРОФЕРМА - HIBERNATE");
        System.out.println("1. CRUD-операции");
        System.out.println("2. Бизнес-запросы JPQL");
        System.out.println("3. Проверить/добавить начальные данные");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private void printCrudMenu() {
        printTitle("CRUD-ОПЕРАЦИИ");
        System.out.println("1. Create - добавить запись вручную");
        System.out.println("2. Read - посмотреть записи");
        System.out.println("3. Update - изменить запись вручную");
        System.out.println("4. Delete - удалить запись по ID");
        System.out.println("0. Назад");
        System.out.print("Выберите пункт: ");
    }

    private void printBusinessQueryMenu() {
        printTitle("БИЗНЕС-ЗАПРОСЫ");
        System.out.println("1. Общая стоимость закупок по партиям");
        System.out.println("2. Заполняемость полок ячейками");
        System.out.println("3. Топ датчиков по температуре");
        System.out.println("4. Полки и количество культур");
        System.out.println("5. Датчики с ячейками и полками");
        System.out.println("6. Рейтинг поставщиков");
        System.out.println("7. Средняя влажность по ячейкам");
        System.out.println("8. Культуры без закупок");
        System.out.println("9. Ячейки выбранной полки");
        System.out.println("10. Общая статистика по полкам");
        System.out.println("11. Выполнить все бизнес-запросы");
        System.out.println("0. Назад");
        System.out.print("Выберите пункт: ");
    }

    private String readChoice() {
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private void waitForEnter() {
        System.out.println();
        System.out.print("Нажмите Enter, чтобы продолжить...");
        scanner.nextLine();
    }

    private void printTitle(String title) {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println(title);
        System.out.println("=".repeat(80));
    }

    private void printInvalidChoice() {
        System.out.println("Нет такого пункта меню.");
    }

    private void printError(Exception e) {
        System.err.println("Ошибка: " + e.getMessage());
    }
}

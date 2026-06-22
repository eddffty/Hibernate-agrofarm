package com.agrofarm;

import com.agrofarm.console.ConsoleMenu;
import com.agrofarm.util.DataSeeder;
import com.agrofarm.util.HibernateUtil;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        try {

            HibernateUtil.getEntityManagerFactory();
            DataSeeder.seed();
            System.out.println("Hibernate инициализирован, схема создана и заполнена");

            new ConsoleMenu(new Scanner(System.in)).run();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.close();
            System.out.println("\nHibernate закрыт. Готово.");
        }
    }
}

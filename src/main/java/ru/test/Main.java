package ru.test;
public class Main {

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setUrl("jdbc:postgresql://localhost:5432/postgres");
        controller.setLogin("postgres");
        controller.setPassword("123");
        controller.setMaxValue(100000);

        controller.process();
    }
}

package ru.test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class DB {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS public.test (field int4);"; // cql команда создания таблицы
    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE public.test;"; // очистка таблицы

    private String url;
    private String login;
    private String password;

    public DB(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }
    //подключение к бд
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(
                    url,
                    login,
                    password);
            return connection;
        } catch (SQLException e) {
            throw e;
        }
    }

    // создание таблицы
    private void createTableIfNotExist() throws SQLException {
        executeQuery(CREATE_TABLE);
        System.out.println("Создать таблицу (если таблица существует - не создавать) успех ");
    }

    //запись данных в таблицу
    public void put(int maxValue) throws SQLException {
        final String queryForInsertData = "INSERT into TEST (field) VALUES (?)";

        createTableIfNotExist();
        clearTable();
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryForInsertData)) {
                for (int i = 1; i <= maxValue; i++) {
                    preparedStatement.setInt(1, i);
                    preparedStatement.addBatch();
                    if (i % 5000 == 0) {
                        preparedStatement.executeBatch();
                    }
                }

                // Дополнительная функция executeBatch() необходима для записи хвоста
                preparedStatement.executeBatch();

                System.out.println("Запись данных успешна");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // получение данных из таблицы
    public Collection<Integer> get()  {
        final String sqlQueryForGet = "SELECT field FROM public.test;";// sql запрос взять данные из таблицы
        Collection<Integer> collection = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQueryForGet)) {
                try (ResultSet resultSet = preparedStatement.executeQuery();) { // выполнение запроса
                    while (resultSet.next()) {
                        collection.add(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace();}

        System.out.println("Получение данных прошло успешно");
        return collection;
    }

   // очистка таблицы
    private void clearTable() throws SQLException {
        executeQuery(TRUNCATE_TABLE);
        System.out.println("Чистка таблицы прошла успешно ");
    }
   //выполнение sql запроса
    private void executeQuery(String query) throws SQLException {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
            }
        }
    }


}

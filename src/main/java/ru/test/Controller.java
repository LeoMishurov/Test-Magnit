package ru.test;

import java.util.Collection;

public class Controller implements java.io.Serializable {

    public static final String xml1 = "1.xml";
    public static final String xml2 = "2.xml";
    public static final String xslt = "Transform.xslt";
    private String url;
    private String login;
    private String password;
    private int maxValue;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }


    public void process(){
        if (url == null || url.isEmpty() || login == null || login.isEmpty() || password == null || password.isEmpty() || maxValue <= 0 ) {
            System.out.println("Недостаточно данных");
            return;
        }
        try {
            DB db = new DB(url, login, password);
            db.put(maxValue);
            Collection<Integer> collectionFromDb = db.get();

            Xml.createXML(collectionFromDb, xml1);
            Xml.transform(xml1, xslt, xml2 );
            Collection<Integer> collectionFromXml = Xml.doParse(xml2);

            int sum = collectionFromXml.stream().mapToInt(i -> i).sum();
            System.out.println("Арифметическая сумма значений всех атрибутов field: " + sum);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Controller that = (Controller) o;

        return url == that.url && login == that.login && password == that.password && maxValue == that.maxValue;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result += login != null ? login.hashCode() : 0;
        result += password != null ? password.hashCode() : 0;
        result += maxValue * 31;
        return result;
    }

    //Переопределенный метод toString()
    @Override
    public String toString() {
        return "Controller{" +
                "url='" + url +
                ", login=" + login +
                ", password=" + password +
                ", maxValue=" + maxValue +
                '}';
    }
}

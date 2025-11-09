package RDS.utils;

import java.io.Serializable;

import RDS.classes.DoubleRange;

public class RestSearchRequest implements Serializable {
    String name;
    DoubleRange scoreRange;
    String price;
    String category;
    String zipCode;

    public RestSearchRequest(String name, DoubleRange scoreRange, String price, String category, String zipCode) {
        this.name = name;
        this.scoreRange = scoreRange;
        this.price = price;
        this.category = category;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public DoubleRange getScoreRange() {
        return scoreRange;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getZipCode() {
        return zipCode;
    }

}

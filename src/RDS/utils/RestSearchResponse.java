package RDS.utils;

import java.io.Serializable;
import java.util.List;

import RDS.classes.Restaurant;

public class RestSearchResponse implements Serializable {
    List<Restaurant> result;

    public RestSearchResponse(List<Restaurant> result) {
        this.result = result;
    }

    public List<Restaurant> getResult() {
        return result;
    }

}

package RDS.utils;

import java.io.Serializable;

public class ConnectionMessage implements Serializable {
    String type;
    String name;
    String password;

    public ConnectionMessage(String type, String name, String password) {
        this.type = type;
        this.name = name;
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }
}
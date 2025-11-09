package RDS.classes;

import java.io.Serializable;

public class DoubleRange implements Serializable {
    public double lower;
    public double upper;

    public DoubleRange() {
        this.lower = 0;
        this.upper = Double.POSITIVE_INFINITY;
    }

    public DoubleRange(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }
}

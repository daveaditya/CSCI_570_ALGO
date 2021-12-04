package edu.usc.algorithm_design;

import java.math.BigInteger;

public class GeneratedOutput {

    String firstAlignment;

    String secondAlignment;

    double timeTaken;

    double memoryUsed;

    public GeneratedOutput(String firstAlignment, String secondAlignment, double timeTaken, double memoryUsed) {
        this.firstAlignment = firstAlignment;
        this.secondAlignment = secondAlignment;
        this.timeTaken = timeTaken;
        this.memoryUsed = memoryUsed;
    }

    public String getFirstAlignment() {
        return firstAlignment;
    }

    public void setFirstAlignment(String firstAlignment) {
        this.firstAlignment = firstAlignment;
    }

    public String getSecondAlignment() {
        return secondAlignment;
    }

    public void setSecondAlignment(String secondAlignment) {
        this.secondAlignment = secondAlignment;
    }

    public double getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        this.timeTaken = timeTaken;
    }

    public double getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(double memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    @Override
    public String toString() {
        return firstAlignment + "\n" + secondAlignment + "\n" + timeTaken + "\n" + "memoryUsed";
    }
}

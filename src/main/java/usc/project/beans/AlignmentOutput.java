package main.java.usc.project.beans;

import java.util.Arrays;

public class AlignmentOutput {

    char[] firstAlignment;
    char[] secondAlignment;
    double cost;

    public AlignmentOutput(char[] firstAlignment, char[] secondAlignment, double cost) {
        this.firstAlignment = firstAlignment;
        this.secondAlignment = secondAlignment;
        this.cost = cost;
    }

    public char[] getFirstAlignment() {
        return firstAlignment;
    }

    public char[] getSecondAlignment() {
        return secondAlignment;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "AlignmentOutput{" +
                "firstAlignment='" + new String(firstAlignment) + '\'' +
                ", secondAlignment='" + new String(secondAlignment) + '\'' +
                ", cost=" + cost +
                '}';
    }
}

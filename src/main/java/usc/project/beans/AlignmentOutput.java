package main.java.usc.project.beans;

public class AlignmentOutput {

    String firstAlignment;
    String secondAlignment;
    double cost;

    public AlignmentOutput(String firstAlignment, String secondAlignment, double cost) {
        this.firstAlignment = firstAlignment;
        this.secondAlignment = secondAlignment;
        this.cost = cost;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "AlignmentOutput{" +
                "firstAlignment='" + firstAlignment + '\'' +
                ", secondAlignment='" + secondAlignment + '\'' +
                ", cost=" + cost +
                '}';
    }
}

package main.java.usc.project.beans;


public class GeneratedOutput {

    String firstAlignment;
    String secondAlignment;
    double alignmentCost;
    double timeTaken;
    double memoryUsed;

    public GeneratedOutput(String firstAlignment, String secondAlignment, double alignmentCost, double timeTaken, double memoryUsed) {
        this.firstAlignment = firstAlignment;
        this.secondAlignment = secondAlignment;
        this.alignmentCost = alignmentCost;
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

    public double getAlignmentCost() {
        return alignmentCost;
    }

    public void setAlignmentCost(float alignmentCost) {
        this.alignmentCost = alignmentCost;
    }

    @Override
    public String toString() {
        return firstAlignment + "\n" + secondAlignment + "\n" + alignmentCost + "\n" +  timeTaken + "\n" + memoryUsed;
    }
}

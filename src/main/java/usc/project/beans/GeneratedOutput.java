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

    @Override
    public String toString() {
        return firstAlignment + "\n" + secondAlignment + "\n" + alignmentCost + "\n" +  timeTaken + "\n" + memoryUsed;
    }
}

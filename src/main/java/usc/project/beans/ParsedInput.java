package main.java.usc.project.beans;

import java.util.List;

public class ParsedInput {

    String firstBase;
    String secondBase;
    List<Integer> firstBaseIndices;
    List<Integer> secondBaseIndices;

    public ParsedInput(String firstBase, List<Integer> firstBaseIndices, String secondBase, List<Integer> secondBaseIndices) {
        this.firstBase = firstBase;
        this.secondBase = secondBase;
        this.firstBaseIndices = firstBaseIndices;
        this.secondBaseIndices = secondBaseIndices;
    }

    public String getFirstBase() {
        return firstBase;
    }

    public void setFirstBase(String firstBase) {
        this.firstBase = firstBase;
    }

    public String getSecondBase() {
        return secondBase;
    }

    public void setSecondBase(String secondBase) {
        this.secondBase = secondBase;
    }

    public List<Integer> getFirstBaseIndices() {
        return firstBaseIndices;
    }

    public void setFirstBaseIndices(List<Integer> firstBaseIndices) {
        this.firstBaseIndices = firstBaseIndices;
    }

    public List<Integer> getSecondBaseIndices() {
        return secondBaseIndices;
    }

    public void setSecondBaseIndices(List<Integer> secondBaseIndices) {
        this.secondBaseIndices = secondBaseIndices;
    }
}

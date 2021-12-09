package main.java.usc.project.utils;

public class Timer {

    private long startTime, endTime, elapsedTime, memoryAvailable, memoryUsed;

    boolean ready;


    public Timer() {
        startTime = System.currentTimeMillis();
        ready = false;
    }


    public void start() {
        startTime = System.currentTimeMillis();
        ready = false;
    }


    public Timer end() {
        endTime = System.currentTimeMillis();
        elapsedTime = endTime - startTime;
        memoryAvailable = Runtime.getRuntime().totalMemory();
        memoryUsed = memoryAvailable - Runtime.getRuntime().freeMemory();
        ready = true;
        return this;
    }


    public long duration() {
        if (!ready) {
            end();
        }
        return elapsedTime;
    }


    public long memory() {
        if (!ready) {
            end();
        }
        return memoryUsed;
    }

}
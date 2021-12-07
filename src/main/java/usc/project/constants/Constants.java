package main.java.usc.project.constants;

public interface Constants {

    public static final String OUTPUT_FILE = "./output1.txt";
    public static final long KILOBYTE = 1024L;

    public static final int GAP_PENALTY = 30;
    public static final int[][] MISMATCH_COST = {
            {0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };
    public static final String ALPHABETS = "ACGT";

}

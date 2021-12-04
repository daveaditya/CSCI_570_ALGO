package edu.usc.algorithm_design;

import java.util.List;

public interface SequenceAlignment {
    int GAP_PENALTY = 30;
    int[][] MISMATCH_COST = {
            {0,110,48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };
    String ALPHABETS = "ACGT";

    void run();

    String[] getAlignment();
}

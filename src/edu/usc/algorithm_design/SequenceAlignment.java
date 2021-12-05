package edu.usc.algorithm_design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequenceAlignment {

    private static final int GAP_PENALTY = 30;
    private static final int[][] MISMATCH_COST = {
            {0,110,48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };
    private static final String ALPHABETS = "ACGT";
    private int[][] dp = null;
    private List<Integer> P = null;

    public SequenceAlignment(String X, String Y) {
        this.dp = new int[X.length()][Y.length()];
        this.P = new ArrayList<>();
    }

    public int alignment(String X, String Y){
        System.out.println("First Base: " + X);
        System.out.println("Second Base: " + Y);
        StringBuilder result = new StringBuilder();

        int xLength = X.length();
        int yLength = Y.length();

        for (int i = 0; i < xLength; i++) {
            this.dp[i][0] = i * SequenceAlignment.GAP_PENALTY;
        }
        for (int j = 0; j < yLength; j++) {
            this.dp[0][j] = j * SequenceAlignment.GAP_PENALTY;
        }

        for (int i = 1; i < xLength; i++) {
            for (int j = 1; j < yLength; j++) {
                this.dp[i][j] = Math.min(
                        Math.min(
                                SequenceAlignment.GAP_PENALTY + this.dp[i - 1][j], SequenceAlignment.GAP_PENALTY + this.dp[i][j - 1]
                        ), SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i-1))][ALPHABETS.indexOf(Y.charAt(j-1))] + this.dp[i - 1][j - 1]);
            }
        }

        System.out.println("Printing DP");
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                System.out.format("%6d",dp[i][j]);
            }
            System.out.println();
        }

        System.out.println("Final alignment cost :: " + dp[xLength-1][yLength-1]);
        return dp[xLength-1][yLength-1];
    }

    /** Divide and Conquer - Efficient Implementation
     * @param X
     * @param Y
     * @return
     */
    public void divideAndConquerAlignment(String X, String Y) {
        int x = X.length();
        int y = Y.length();
        String xFlip= null, yFlip= null;
        StringBuilder str = new StringBuilder();
        str.append(X);
        xFlip = String.valueOf(str.reverse());

        if(x <= 2 || y <= 2){
            this.alignment(X,Y);
        }
        spaceEfficientAlignment(X, Y.substring(1, y/2));
        assert false;
        spaceEfficientAlignment(xFlip, yFlip.substring(1, y/2));   // backwardEfficientAligment(X, Y.substring(y/2+1, n));

        int best_cost = Integer.MAX_VALUE;
        int best_q = x; // value for best_q
        for (int q = 0; q <= x ; q++) {
            int cost = Integer.parseInt(Y.substring(q) + Y.substring(q+1)); //post flip check q for Y
            if(cost < best_cost) {
                best_cost = cost;
                best_q = q;
            }
        }
        P.add(best_q,y/2);
        divideAndConquerAlignment(X.substring(1,best_q), Y.substring(1, y/2));
        divideAndConquerAlignment(X.substring(1,best_q), Y.substring(1, y/2));
    }


    /**
     * @param X
     * @param Y
     * @return
     */
    private void spaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m][1];
        for (int j = 1; j < n; j++) {
            B[0][1] = j * SequenceAlignment.GAP_PENALTY;
            for (int i = 1; i < m; i++) {
                B[i][1] = Math.min(Math.min(SequenceAlignment.GAP_PENALTY + B[i - 1][1], SequenceAlignment.GAP_PENALTY + B[i][0]), SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i))][ALPHABETS.indexOf(Y.charAt(j))] + B[i - 1][0]); // is the alpha value correct
            }
            for (int i = 0; i < m; i++) {
                B[i][0] = B[i][1];
            }
        }
    }

    public String[] getAlignment(String x, String y) {
        int maxLength = x.length() + y.length();

        int i = x.length() - 1;
        int j = y.length() - 1;

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        while (!(i == 0 || j == 0)) {
            if (x.charAt(i) == y.charAt(j)) {
                xResult[xPosition--] = x.charAt(i);
                yResult[yPosition--] = y.charAt(j);
                i--;
                j--;
            }
            else if (this.dp[i - 1][j - 1] + SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i-1))][ALPHABETS.indexOf(y.charAt(j-1))] == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i);
                yResult[yPosition--] = y.charAt(j);
                i--;
                j--;
            }
            else if (this.dp[i - 1][j] + SequenceAlignment.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i);
                yResult[yPosition--] = '_';
                i--;
            }
            else if (this.dp[i][j - 1] + SequenceAlignment.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = y.charAt(j);
                j--;
            }
        }
        while (xPosition > 0) {
            if (i > 0) xResult[xPosition--] = x.charAt(--i);
            else xResult[xPosition--] = (int)'_';
        }
        while (yPosition > 0) {
            if (j > 0) yResult[yPosition--] = y.charAt(--j);
            else yResult[yPosition--] = (int)'_';
        }

        // Todo: To remove extra gaps or not? Refer Geeksforgeeks
        int id = 1;
        for (i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }

        StringBuilder xFinalResult = new StringBuilder();
        for (i = id; i <= maxLength; i++) {
            xFinalResult.append(xResult[i]);
        }

        StringBuilder yFinalResult = new StringBuilder();
        System.out.print("\n");
        for (i = id; i <= maxLength; i++) {
            yFinalResult.append(yResult[i]);
        }
        return new String[]{xFinalResult.toString(), yFinalResult.toString()};
    }


    public String[] getAlignmentDC(String x, String y) {
        int maxLength = x.length() + y.length();

        int i = x.length();
        int j = y.length();

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        List<Integer> P = new ArrayList<>(this.P);
        Collections.reverse(P);

        while ( !(i == 0 || j == 0)) {
            if (x.charAt(i - 1) == y.charAt(j - 1)) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            }
            else if (dp[i - 1][j - 1] + SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i-1))][ALPHABETS.indexOf(y.charAt(j-1))] == dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            }
            else if (dp[i - 1][j] + SequenceAlignment.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = '_';
                i--;
            }
            else if (dp[i][j - 1] + SequenceAlignment.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = y.charAt(j - 1);
                j--;
            }
        }
        while (xPosition > 0) {
            if (i > 0) xResult[xPosition--] = x.charAt(--i);
            else xResult[xPosition--] = (int)'_';
        }
        while (yPosition > 0) {
            if (j > 0) yResult[yPosition--] = y.charAt(--j);
            else yResult[yPosition--] = (int)'_';
        }

        // Todo: To remove extra gaps or not? Refer Geeksforgeeks
        int id = 1;
        for (i = maxLength; i >= 1; i--) {
            if (yResult[i] == '_' && xResult[i] == '_') {
                id = i + 1;
                break;
            }
        }
        for (i = id; i <= maxLength; i++) {
            System.out.print(xResult[i]);
        }
        System.out.print("\n");
        for (i = id; i <= maxLength; i++)
        {
            System.out.print(yResult[i]);
        }
        return new String[]{new String(xResult), new String(yResult)};
    }

}

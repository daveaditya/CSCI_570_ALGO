package edu.usc.algorithm_design;

import java.util.ArrayList;
import java.util.List;

public class SequenceAlignmentDC implements SequenceAlignment {

    /** Divide and Conquer - Efficient Implementation
     * @param X
     * @param Y
     * @return
     */
    public void run(String X, String Y){
        int x = X.length();
        int y = Y.length();
        List<Integer> P = new ArrayList<>();
        String xFlip= null, yFlip= null;
        StringBuffer str = new StringBuffer();
        str.append(X);
        xFlip = String.valueOf(str.reverse());

        if(x <= 2 || y <= 2){
            alignment(X,Y);
        }
        spaceEfficientAligment(X, Y.substring(1, y/2));
        spaceEfficientAligment(xFlip, yFlip.substring(1, y/2));   // backwardEfficientAligment(X, Y.substring(y/2+1, n));

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
        run(X.substring(1,best_q), Y.substring(1, y/2), P);
        run(X.substring(1,best_q), Y.substring(1, y/2), P);
    }


    /**
     * @param X
     * @param Y
     * @return
     */
    private int[] spaceEfficientAligment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m][1];
        for (int j = 1; j < n; j++) {
            B[0][1] = j * this.GAP_PENALTY;
            for (int i = 1; i < m; i++) {
                B[i][1] = Math.min(Math.min(this.GAP_PENALTY + B[i - 1][1], this.GAP_PENALTY + B[i][0]), this.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i))][ALPHABETS.indexOf(Y.charAt(j))] + B[i - 1][0]); // is the alpha value correct
            }
            for (int i = 0; i < m; i++) {
                B[i][0] = B[i][1];
            }
        }

        int[] R = new int[m];
        for (int i = 0; i < m; i++) {
            R[i] = B[i][1];
        }
        return R;
    }

    public String[] getAlignment(String x, String y, int[][] dp) {
        int maxLength = x.length() + y.length();

        int i = x.length();
        int j = y.length();

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        while ( !(i == 0 || j == 0)) {
            if (x.charAt(i - 1) == y.charAt(j - 1)) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            }
            else if (dp[i - 1][j - 1] + this.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i))][ALPHABETS.indexOf(y.charAt(j))] == dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            }
            else if (dp[i - 1][j] + this.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = '_';
                i--;
            }
            else if (dp[i][j - 1] + this.GAP_PENALTY == dp[i][j]) {
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

        String[] result = {new String(xResult), new String(yResult)};
        return result;
    }

    @Override
    public String[] getAlignment(String x, String y, List<Integer> dp) {
        return new String[0];
    }

}

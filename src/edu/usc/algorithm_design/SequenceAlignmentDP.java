package edu.usc.algorithm_design;

import java.util.List;

public class SequenceAlignmentDP implements SequenceAlignment {

    /**
     * Alignment
     * @param X
     * @param Y
     * @return
     */
    public void run(String X, String Y){
        System.out.println("First Base: " + X);
        System.out.println("Second Base: " + Y);
        StringBuilder result = new StringBuilder();

        int xLength = X.length();
        int yLength = Y.length();
        int[][] dp = new int[xLength + 1][yLength + 1];

        for (int i = 0; i < xLength; i++) {
            dp[i][0] = i * this.GAP_PENALTY;
        }
        for (int j = 0; j < yLength; j++) {
            dp[0][j] = j * this.GAP_PENALTY;
        }

        for (int i = 1; i < xLength; i++) {
            for (int j = 1; j < yLength; j++) {
                dp[i][j] = Math.min(Math.min(this.GAP_PENALTY + dp[i-1][j], this.GAP_PENALTY + dp[i][j-1]), this.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i))][ALPHABETS.indexOf(Y.charAt(j))] + dp[i-1][j-1]);
                System.out.println("Alignment: " + dp[i][j]);

                result = InputGenerator.getAlignment(X, Y, dp);
                System.out.println("X: " + result.charAt(0));
                System.out.println("Y: " + result.charAt(0));
            }
        }

        System.out.println("Alignment: " + dp[xLength][yLength]);
    }


    public String[] getAlignment(String x, String y, int[][] dp) {

    }

    @Override
    public String[] getAlignment(String x, String y, List<Integer> dp) {
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
            } else if (dp[i - 1][j - 1] + this.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i))][ALPHABETS.indexOf(y.charAt(j))] == dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j] + this.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = '_';
                i--;
            } else if (dp[i][j - 1] + this.GAP_PENALTY == dp[i][j]) {
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

        return [new String(xResult), new String(yResult)];
    }

}

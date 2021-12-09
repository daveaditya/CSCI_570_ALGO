package main.java.usc.project.impl;

import main.java.usc.project.beans.AlignmentOutput;
import main.java.usc.project.constants.Constants;

import java.util.stream.IntStream;

@SuppressWarnings("FieldMayBeFinal")
public class SequenceAlignment {

    public SequenceAlignment() {
    }


    public AlignmentOutput alignmentWithDynamicProgramming(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i * Constants.GAP_PENALTY;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j * Constants.GAP_PENALTY;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.min(
                        Math.min(
                                Constants.GAP_PENALTY + dp[i - 1][j], Constants.GAP_PENALTY + dp[i][j - 1]
                        ), Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + dp[i - 1][j - 1]);
            }
        }

        int maxLength = X.length() + Y.length();
        int i = X.length();
        int j = Y.length();

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        while (!(i == 0 || j == 0)) {
            if (X.charAt(i - 1) == Y.charAt(j - 1)) {
                xResult[xPosition--] = X.charAt(i - 1);
                yResult[yPosition--] = Y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j - 1] + Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] == dp[i][j]) {
                xResult[xPosition--] = X.charAt(i - 1);
                yResult[yPosition--] = Y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i][j - 1] + Constants.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = Y.charAt(j - 1);
                j--;
            } else if (dp[i - 1][j] + Constants.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = X.charAt(i - 1);
                yResult[yPosition--] = '_';
                i--;
            }
        }

        while (xPosition > 0) {
            if (i > 0) xResult[xPosition--] = X.charAt(--i);
            else xResult[xPosition--] = (int) '_';
        }

        while (yPosition > 0) {
            if (j > 0) yResult[yPosition--] = Y.charAt(--j);
            else yResult[yPosition--] = (int) '_';
        }

        int id = 1;
        for (i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }

        return new AlignmentOutput(new String(xResult).substring(id), new String(yResult).substring(id), dp[m][n]);
    }


    public int[] spaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m + 1][2];
        for (int i = 0; i <= m; i++) {
            B[i][0] = i * Constants.GAP_PENALTY;
        }

        // Find cost for X and Y
        for (int j = 1; j <= n; j++) {
            B[0][1] = j * Constants.GAP_PENALTY;
            for (int i = 1; i <= m; i++) {
                B[i][1] = Math.min(
                        Math.min(
                                Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + B[i - 1][0], Constants.GAP_PENALTY + B[i - 1][1]
                        ), Constants.GAP_PENALTY + B[i][0]);
            }

            // Swap the columns, to be ready for next iteration
            for (int i = 0; i <= m; i++) {
                B[i][0] = B[i][1];
            }
        }

        return IntStream.range(0, m + 1).map(row -> B[row][0]).toArray();
    }


    public AlignmentOutput alignmentWithDivideAndConquer(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        if (m <= 2 || n <= 2) {
            return this.alignmentWithDynamicProgramming(X, Y);
        }

        String xLeft = X.substring(0, m / 2);
        String xRight = X.substring(m / 2);

        int[] left = spaceEfficientAlignment(xLeft, Y);
        int[] right = spaceEfficientAlignment(
                new StringBuilder(xRight).reverse().toString(),
                new StringBuilder(Y).reverse().toString()
        );

        int bestCost = Integer.MAX_VALUE;
        int bestIndex = 0;
        for (int q = 0, r = right.length - 1; q < left.length && r >= 0; q++, r--) {
            System.out.println(left[q] + right[r]);
            if (left[q] + right[r] < bestCost) {
                bestCost = left[q] + right[r];
                bestIndex = q;
            }
        }

        String yLeft = Y.substring(0, bestIndex);
        String yRight = Y.substring(bestIndex);

        AlignmentOutput leftOutput = alignmentWithDivideAndConquer(xLeft, yLeft);
        AlignmentOutput rightOutput = alignmentWithDivideAndConquer(xRight, yRight);
        return new AlignmentOutput(
                leftOutput.getFirstAlignment() + rightOutput.getFirstAlignment(),
                rightOutput.getFirstAlignment() + rightOutput.getSecondAlignment(),
                leftOutput.getCost() + rightOutput.getCost()
        );
    }

}

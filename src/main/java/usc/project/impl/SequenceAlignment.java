package main.java.usc.project.impl;

import main.java.usc.project.beans.AlignmentOutput;
import main.java.usc.project.constants.Constants;

import java.util.stream.IntStream;

@SuppressWarnings("FieldMayBeFinal")
public class SequenceAlignment {

    public int total = 0;

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


//    public int[] spaceEfficientAlignment(String X, String Y) {
//        int m = X.length();
//        int n = Y.length();
//
//        int[][] B = new int[2][n + 1];
//        for (int i = 0; i <= n; i++) {
//            B[0][i] = i * Constants.GAP_PENALTY;
//        }
//
//        // Find cost for X and Y
//        for (int i = 1; i <= m; i++) {
//            B[1][0] = i * Constants.GAP_PENALTY;
//
//            for (int j = 1; j <= n; j++) {
//                B[1][j] = Math.min(
//                        Math.min(
//                                Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + B[0][j - 1],
//                                Constants.GAP_PENALTY + B[0][j]
//                        ), Constants.GAP_PENALTY + B[1][j - 1]);
//            }
//
//            // Swap the columns, to be ready for next iteration
//            for (int k = 0; k <= n; k++) {
//                B[0][i] = B[1][i];
//            }
//        }
//
//        int[] result = new int[n + 1];
//        for (int i = 0; i <= n; i++) {
//            result[i] = B[1][i];
//        }
//
//        return result;
//
////        return IntStream.range(0, n + 1).map(row -> B[row][0]).toArray();
//    }


    public int[] spaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            prev[i] = 0;
            curr[i] = 0;
        }

        for (int i = 0; i <= n; i++) {
            prev[i] = i * Constants.GAP_PENALTY;
        }

        // Find cost for X and Y
        for (int i = 1; i <= m; i++) {
            curr[0] = i * Constants.GAP_PENALTY;

            for (int j = 1; j <= n; j++) {
                if(X.charAt(i - 1) == Y.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = Math.min(
                            Math.min(
                                    Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + prev[j - 1],
                                    Constants.GAP_PENALTY + prev[j]
                            ), Constants.GAP_PENALTY + curr[j - 1]);
                }
            }

            for (int j = 0; j <= n; j++) {
                prev[j] = curr[j];
                curr[j] = 0;
            }
        }

        for (int i = 0; i <= n; i++) {
            System.out.print(prev[i] + " ");
        }
        System.out.println();

        return prev;

//        return IntStream.range(0, n + 1).map(row -> B[row][0]).toArray();
    }


    public AlignmentOutput alignmentWithDivideAndConquer(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        if (m <= 1 || n <= 1) {
            AlignmentOutput result = this.alignmentWithDynamicProgramming(X, Y);
            System.out.println(X + " " + Y);
            System.out.println(result.getFirstAlignment() + " " + result.getSecondAlignment());
            System.out.println(result.getCost());
            this.total += result.getCost();
            return result;
        }

        String xLeft = X.substring(0, m / 2);
        String xRight = X.substring(m / 2);

        System.out.println("X :: " + X);
        System.out.println("xLeft :: " + xLeft);
        System.out.println("xRight :: " + xRight);

        String xRReversed = new StringBuilder(xRight).reverse().toString();
        String yReversed = new StringBuilder(Y).reverse().toString();
        System.out.println("Xr Reversed :: " + xRReversed);
        System.out.println("Y Reversed :: " + yReversed);


        int[] left = spaceEfficientAlignment(xLeft, Y);
        int[] right = spaceEfficientAlignment(xRReversed, yReversed);

        int bestCost = left[0] + right[n];
        int bestIndex = 0;
//        for (int q = 0, r = right.length - 1; q < left.length && r >= 0; q++, r--) {
//            System.out.printf("%-8d%-8d\n", left[q], right[r]);
//            if (left[q] + right[r] < bestCost) {
//                bestCost = left[q] + right[r];
//                bestIndex = q;
//            }
//        }
        for (int q = 0; q <= n; q++) {
            int cost = left[q] + right[n - q];
            if (cost < bestCost) {
                bestCost = cost;
                bestIndex = q;
            }
        }

        System.out.printf("%-8d%-8d%-8d\n", m / 2, bestIndex, bestCost);

        String yLeft = Y.substring(0, bestIndex);
        String yRight = Y.substring(bestIndex);

        AlignmentOutput leftOutput = alignmentWithDivideAndConquer(xLeft, yLeft);
        AlignmentOutput rightOutput = alignmentWithDivideAndConquer(xRight, yRight);
        return new AlignmentOutput(
                leftOutput.getFirstAlignment() + rightOutput.getFirstAlignment(),
                leftOutput.getSecondAlignment() + rightOutput.getSecondAlignment(),
                leftOutput.getCost() + rightOutput.getCost()
        );
    }

}

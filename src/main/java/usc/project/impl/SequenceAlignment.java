package main.java.usc.project.impl;

import main.java.usc.project.beans.AlignmentOutput;
import main.java.usc.project.beans.Pair;
import main.java.usc.project.constants.Constants;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("FieldMayBeFinal")
public class SequenceAlignment {

    public List<Pair> P;

    public SequenceAlignment() {
        this.P = new ArrayList<>();
    }


//    public AlignmentOutput alignmentWithDynamicProgramming(String X, String Y) {
//        int m = X.length();
//        int n = Y.length();
//
//        int[][] dp = new int[m + 1][n + 1];
//        for (int i = 0; i <= m; i++) {
//            dp[i][0] = i * Constants.GAP_PENALTY;
//        }
//        for (int j = 0; j <= n; j++) {
//            dp[0][j] = j * Constants.GAP_PENALTY;
//        }
//
//        for (int i = 1; i <= m; i++) {
//            for (int j = 1; j <= n; j++) {
//                dp[i][j] = Math.min(
//                        Math.min(
//                                Constants.GAP_PENALTY + dp[i - 1][j], Constants.GAP_PENALTY + dp[i][j - 1]
//                        ), Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + dp[i - 1][j - 1]);
//            }
//        }
//
//        int maxLength = X.length() + Y.length();
//        int i = X.length();
//        int j = Y.length();
//
//        int xPosition = maxLength;
//        int yPosition = maxLength;
//
//        char[] xResult = new char[maxLength + 1];
//        char[] yResult = new char[maxLength + 1];
//
//        while (!(i == 0 || j == 0)) {
//            if (X.charAt(i - 1) == Y.charAt(j - 1)) {
//                xResult[xPosition--] = X.charAt(i - 1);
//                yResult[yPosition--] = Y.charAt(j - 1);
//                i--;
//                j--;
//            } else if (dp[i - 1][j - 1] + Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] == dp[i][j]) {
//                xResult[xPosition--] = X.charAt(i - 1);
//                yResult[yPosition--] = Y.charAt(j - 1);
//                i--;
//                j--;
//            } else if (dp[i][j - 1] + Constants.GAP_PENALTY == dp[i][j]) {
//                xResult[xPosition--] = '_';
//                yResult[yPosition--] = Y.charAt(j - 1);
//                j--;
//            } else if (dp[i - 1][j] + Constants.GAP_PENALTY == dp[i][j]) {
//                xResult[xPosition--] = X.charAt(i - 1);
//                yResult[yPosition--] = '_';
//                i--;
//            }
//        }
//
//        while (xPosition > 0) {
//            if (i > 0) xResult[xPosition--] = X.charAt(--i);
//            else xResult[xPosition--] = (int) '_';
//        }
//
//        while (yPosition > 0) {
//            if (j > 0) yResult[yPosition--] = Y.charAt(--j);
//            else yResult[yPosition--] = (int) '_';
//        }
//
//        int id = 1;
//        for (i = maxLength; i >= 1; i--) {
//            if (xResult[i] == '_' && yResult[i] == '_') {
//                id = i + 1;
//                break;
//            }
//        }
//
//        return new AlignmentOutput(new String(xResult).substring(id), new String(yResult).substring(id), dp[m][n]);
//    }


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

        System.out.println("X :: " + X);
        System.out.println("Y :: " + Y);
        return new AlignmentOutput(new String(xResult).substring(id), new String(yResult).substring(id), dp[m][n]);
    }


    public int[] spaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[n + 1][2];
        for (int i = 0; i <= n; i++) {
            B[i][0] = i * Constants.GAP_PENALTY;
        }

        // Find cost for X and Y
        for (int i = 1; i <= m; i++) {
            B[0][1] = i * Constants.GAP_PENALTY;

            for (int j = 1; j <= n; j++) {
                B[j][1] = Math.min(
                        Math.min(
                                Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + B[j - 1][0],
                                Constants.GAP_PENALTY + B[j][0]
                        ), Constants.GAP_PENALTY + B[j - 1][1]);
            }

            // Swap the columns, to be ready for next iteration
            for (int k = 0; k <= n; k++) {
                B[k][0] = B[k][1];
            }
        }

        int[] finalResult = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            finalResult[i] = B[i][0];
        }
        return finalResult;
    }


    public AlignmentOutput alignmentWithDivideAndConquer(String X, int offsetX, String Y, int offsetY) {
        int m = X.length();
        int n = Y.length();

        if (m <= 1 || n <= 1) {
            AlignmentOutput result = this.alignmentWithDynamicProgramming(X, Y);
            System.out.println(result);
            return result;
        }

        String xLeft = X.substring(0, m / 2);
        String xRight = X.substring(m / 2);

        int[] left = spaceEfficientAlignment(xLeft, Y);
        int[] right = spaceEfficientAlignment(
                new StringBuilder(xRight).reverse().toString(),
                new StringBuilder(Y).reverse().toString()
        );

        int bestCost = left[0] + right[n];
        int bestIndex = 0;
        for (int q = 0; q < left.length; q++) {
            if (left[q] + right[n - q] < bestCost) {
                bestCost = left[q] + right[n - q];
                bestIndex = q;
            }
        }

        this.P.add(new Pair(offsetX + m / 2, offsetY + bestIndex));

        String yLeft = Y.substring(0, bestIndex);
        String yRight = Y.substring(bestIndex);

        AlignmentOutput leftOutput = alignmentWithDivideAndConquer(xLeft, 0, yLeft, 0);
        System.out.println("Left :: " + leftOutput);
        AlignmentOutput rightOutput = alignmentWithDivideAndConquer(xRight, m / 2, yRight, n / 2);
        System.out.println("Right :: " + rightOutput);
        return new AlignmentOutput(
                leftOutput.getFirstAlignment() + rightOutput.getFirstAlignment(),
                rightOutput.getFirstAlignment() + rightOutput.getSecondAlignment(),
                leftOutput.getCost() + rightOutput.getCost()
        );
    }

}

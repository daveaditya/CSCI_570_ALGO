package main.java.usc.project.impl;

import main.java.usc.project.beans.AlignmentOutput;
import main.java.usc.project.constants.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings({"FieldMayBeFinal", "ManualArrayCopy"})
public class SequenceAlignment {

    public static final int GAP_PENALTY = 30;
    public static final int[][] MISMATCH_COST = {
            {0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };

    public static final Map<Character, Integer> ALPHABETS = new HashMap<>();


    public SequenceAlignment() {
        ALPHABETS.put('A', 0);
        ALPHABETS.put('C', 1);
        ALPHABETS.put('G', 2);
        ALPHABETS.put('T', 3);
    }


    public AlignmentOutput alignmentWithDynamicProgramming(char[] X, char[] Y) {
        int m = X.length;
        int n = Y.length;

        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++)
            dp[i][0] = i * GAP_PENALTY;

        for (int j = 0; j <= n; j++)
            dp[0][j] = j * GAP_PENALTY;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Math.min(
                                MISMATCH_COST[ALPHABETS.get(X[i - 1])][ALPHABETS.get(Y[j - 1])] + dp[i - 1][j - 1],
                                Math.min(
                                    GAP_PENALTY + dp[i - 1][j],
                                    GAP_PENALTY + dp[i][j - 1]
                                )
                        );
            }
        }

        int maxLength = m + n;
        int i = m;
        int j = n;

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        while (!(i == 0 || j == 0)) {
            if (X[i - 1] == Y[j - 1]) {
                xResult[xPosition--] = X[i - 1];
                yResult[yPosition--] = Y[j - 1];
                i--;
                j--;
            } else if (dp[i - 1][j - 1] + MISMATCH_COST[ALPHABETS.get(X[i - 1])][ALPHABETS.get(Y[j - 1])] == dp[i][j]) {
                xResult[xPosition--] = X[i - 1];
                yResult[yPosition--] = Y[j - 1];
                i--;
                j--;
            } else if (dp[i][j - 1] + GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = Y[j - 1];
                j--;
            } else if (dp[i - 1][j] + GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = X[i - 1];
                yResult[yPosition--] = '_';
                i--;
            }
        }

        while (xPosition > 0) {
            if (i > 0)
                xResult[xPosition--] = X[--i];
            else
                xResult[xPosition--] = '_';
        }

        while (yPosition > 0) {
            if (j > 0)
                yResult[yPosition--] = Y[--j];
            else
                yResult[yPosition--] = '_';
        }

        int id = 1;
        for (i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }

        // Create X Result
        char[] finalXResult = new char[xResult.length - id];
        for(int p = 0 ; p < yResult.length - id; p++)
            finalXResult[p] = xResult[id + p];

        // Create Y Result
        char[] finalYResult = new char[yResult.length - id];
        for(int p = 0 ; p < yResult.length - id; p++)
            finalYResult[p] = yResult[id + p];

        return new AlignmentOutput(finalXResult, finalYResult, dp[m][n]);
    }


    public int[] spaceEfficientAlignment(char[] X, char[] Y) {
        int m = X.length;
        int n = Y.length;

        int[][] B = new int[2][n + 1];
        for (int i = 0; i <= n; i++) {
            B[0][i] = i * GAP_PENALTY;
        }

        // Find cost for X and Y
        for (int i = 1; i <= m; i++) {
            B[1][0] = i * GAP_PENALTY;

            for (int j = 1; j <= n; j++) {
                B[1][j] = Math.min(
                        Math.min(
                                MISMATCH_COST[ALPHABETS.get(X[i - 1])][ALPHABETS.get(Y[j - 1])] + B[0][j - 1],
                                GAP_PENALTY + B[0][j]
                        ), GAP_PENALTY + B[1][j - 1]
                );
            }

            // Swap the rows, to be ready for next iteration
            for (int k = 0; k <= n; k++) {
                B[0][k] = B[1][k];
            }
        }

        int[] result = new int[n + 1];
        for (int i = 0; i <= n; i++)
            result[i] = B[1][i];

        return result;
    }


    public AlignmentOutput alignmentWithDivideAndConquer(char[] X, char[] Y) {
        int m = X.length;
        int n = Y.length;

        if (m <= 1 || n <= 1)
            return this.alignmentWithDynamicProgramming(X, Y);

        // Get left part of X
        char[] xLeft = new char[m / 2];
        System.arraycopy(X, 0, xLeft, 0, m / 2);

        // Get right part of Y
        char[] xRight = new char[m / 2];
        System.arraycopy(X, m / 2, xRight, 0, m / 2);

        char[] xRightReversed = new char[m / 2];
        for (int i = 0; i < xRightReversed.length; i++) {
            xRightReversed[i] = xRight[xRight.length - i - 1];
        }

        char[] yReversed = new char[n];
        for (int i = 0; i < yReversed.length; i++) {
            yReversed[i] = Y[n - i - 1];
        }

        int[] left = spaceEfficientAlignment(xLeft, Y);
        int[] right = spaceEfficientAlignment(xRightReversed, yReversed);

        int bestCost = left[0] + right[n];
        int bestIndex = 0;

        for (int q = 0; q <= n; q++) {
            int cost = left[q] + right[n - q];
            if (cost < bestCost) {
                bestCost = cost;
                bestIndex = q;
            }
        }

        // Get left of Y
        char[] yLeft = new char[bestIndex];
        System.arraycopy(Y, 0, yLeft, 0, bestIndex);

        // Get right of Y
        char[] yRight = new char[n - bestIndex];
        System.arraycopy(Y, bestIndex, yRight, 0, n - bestIndex);

        AlignmentOutput leftOutput = alignmentWithDivideAndConquer(xLeft, yLeft);
        AlignmentOutput rightOutput = alignmentWithDivideAndConquer(xRight, yRight);

        char[] leftOutputFirstAlignment = leftOutput.getFirstAlignment();
        char[] rightOutputFirstAlignment = rightOutput.getFirstAlignment();
        char[] firstAlignmentResult = new char[leftOutputFirstAlignment.length + rightOutputFirstAlignment.length];
        for (int i = 0; i < leftOutputFirstAlignment.length; i++) {
            firstAlignmentResult[i] = leftOutputFirstAlignment[i];
        }
        for (int i = leftOutputFirstAlignment.length, j = 0; i < firstAlignmentResult.length && j < rightOutputFirstAlignment.length; i++, j++) {
            firstAlignmentResult[i] = rightOutputFirstAlignment[j];
        }

        char[] leftOutputSecondAlignment = leftOutput.getSecondAlignment();
        char[] rightOutputSecondAlignment = rightOutput.getSecondAlignment();
        char[] secondAlignmentResult = new char[leftOutputSecondAlignment.length + rightOutputSecondAlignment.length];
        for (int i = 0; i < leftOutputSecondAlignment.length; i++) {
            secondAlignmentResult[i] = leftOutputSecondAlignment[i];
        }
        for (int i = leftOutputSecondAlignment.length, j = 0; i < secondAlignmentResult.length && j < rightOutputSecondAlignment.length; i++, j++) {
            secondAlignmentResult[i] = rightOutputSecondAlignment[j];
        }

        return new AlignmentOutput(
                firstAlignmentResult,
                secondAlignmentResult,
                leftOutput.getCost() + rightOutput.getCost()
        );
    }

}

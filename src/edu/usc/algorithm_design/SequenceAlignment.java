package edu.usc.algorithm_design;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
public class SequenceAlignment {

    private static final int GAP_PENALTY = 30;
    private static final int[][] MISMATCH_COST = {
            {0, 110, 48, 94},
            {110, 0, 118, 48},
            {48, 118, 0, 110},
            {94, 48, 110, 0}
    };
    private static final String ALPHABETS = "ACGT";
    private int[][] dp;
    private List<Pair> P;


    public List<Pair> getP() {
        return this.P;
    }


    public SequenceAlignment(String X, String Y) {
        this.dp = new int[X.length() + 1][Y.length() + 1];
        this.P = new ArrayList<>();
    }


    public int alignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        for (int i = 0; i <= m; i++) {
            this.dp[i][0] = i * SequenceAlignment.GAP_PENALTY;
        }
        for (int j = 0; j <= n; j++) {
            this.dp[0][j] = j * SequenceAlignment.GAP_PENALTY;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                this.dp[i][j] = Math.min(
                        Math.min(
                                SequenceAlignment.GAP_PENALTY + this.dp[i - 1][j], SequenceAlignment.GAP_PENALTY + this.dp[i][j - 1]
                        ), SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i - 1))][ALPHABETS.indexOf(Y.charAt(j - 1))] + this.dp[i - 1][j - 1]);
            }
        }

//        System.out.println("Printing DP");
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                System.out.format("%6d",dp[i][j]);
//            }
//            System.out.println();
//        }

//        System.out.println("Final alignment cost :: " + dp[m-1][n-1]);
        return dp[m][n];
    }


    /**
     * @param X
     * @param Y
     * @return
     */
    public int[][] spaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m + 1][2];
        for (int i = 1; i < m; i++) {
            B[i][0] = i * SequenceAlignment.GAP_PENALTY;
        }

        if(X.length() == 0 || Y.length() == 0) {
            return B;
        }

        // Find cost for X and Y
        for (int j = 1; j < n; j++) {
            B[0][1] = j * SequenceAlignment.GAP_PENALTY;
            for (int i = 1; i < m; i++) {
                B[i][1] = Math.min(
                        Math.min(
                                SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i))][ALPHABETS.indexOf(Y.charAt(j))] + B[i - 1][0], SequenceAlignment.GAP_PENALTY + B[i - 1][1]
                        ), SequenceAlignment.GAP_PENALTY + B[i][0]);
            }

            // Swap the columns, to be ready for next iteration
            for (int i = 1; i < m; i++) {
                B[i][0] = B[i][1];
            }
        }

        return B;
    }


    public int[][] backwardSpaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m + 1][2];
        for (int i = m - 1; i >= 0; i--) {
            B[i][0] = (m - i) * SequenceAlignment.GAP_PENALTY;
        }

        if(X.length() == 0 || Y.length() == 0) {
            return B;
        }

        for (int j = n - 1; j >= 0; j--) {
            B[m - 1][0] = (n - j) * SequenceAlignment.GAP_PENALTY;

            for (int i = m - 1; i >= 0; i--) {
                B[i][0] = Math.min(
                        Math.min(
                                SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i))][ALPHABETS.indexOf(Y.charAt(j))] + B[i + 1][1], SequenceAlignment.GAP_PENALTY + B[i + 1][0]
                        ), SequenceAlignment.GAP_PENALTY + B[i][1]);
            }

            // Swap the columns to prepare for next iteration
            for (int i = 0; i < m; i++) {
                B[i][1] = B[i][0];
            }
        }

        return B;
    }


    /**
     * Divide and Conquer - Efficient Implementation
     *
     * @param X
     * @param Y
     * @return
     */
    public void divideAndConquerAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        if (m <= 2 || n <= 2) {
            this.alignment(X, Y);
        }

        if(X.length() == 0 || Y.length() == 0) {
            return;
        }

        int[][] left = spaceEfficientAlignment(X, Y.substring(0, n / 2));
        int[][] right = backwardSpaceEfficientAlignment(X, Y.substring(n / 2, n));

        int min = left[1][1] + right[1][0];
        int best_q = 1;
        for (int q = 2; q <= m; q++) {
            if (min > left[q][1] + right[q][0]) {
                min = left[q][1] + right[q][0];
                best_q = q - 1;
            }
        }

        boolean alreadyAdded = true;
        for(Pair pair : P) {
            if(pair.getX() == best_q && pair.getY() == n / 2) {
                alreadyAdded = !alreadyAdded;
                break;
            }
        }
        if(alreadyAdded) {
            P.add(new Pair(best_q, n / 2, left[best_q][1]));
        }

        divideAndConquerAlignment(X.substring(0, best_q), Y.substring(0, n / 2));
        divideAndConquerAlignment(X.substring(best_q, m), Y.substring(n / 2, n));
    }


    public String[] getAlignment(String x, String y) {
        int maxLength = x.length() + y.length();

        int i = x.length();
        int j = y.length();

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        while (!(i == 0 || j == 0)) {
            if (x.charAt(i - 1) == y.charAt(j - 1)) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (this.dp[i - 1][j - 1] + SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i - 1))][ALPHABETS.indexOf(y.charAt(j - 1))] == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (this.dp[i][j - 1] + SequenceAlignment.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = y.charAt(j - 1);
                j--;
            } else if (this.dp[i - 1][j] + SequenceAlignment.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = '_';
                i--;
            }
        }
        while (xPosition > 0) {
            if (i > 0) xResult[xPosition--] = x.charAt(--i);
            else xResult[xPosition--] = (int) '_';
        }
        while (yPosition > 0) {
            if (j > 0) yResult[yPosition--] = y.charAt(--j);
            else yResult[yPosition--] = (int) '_';
        }

        int id = 1;
        for (i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }

        for (i = id; i <= maxLength; i++) {
            System.out.print(xResult[i]);
        }
        System.out.print("\n");
        for (i = id; i <= maxLength; i++) {
            System.out.print(yResult[i]);
        }
        return new String[]{new String(xResult), new String(yResult)};
    }


//    public String[] getAlignmentDC(String x, String y) {
//        int maxLength = x.length() + y.length();
//
//        int i = x.length();
//        int j = y.length();
//
//        int xPosition = maxLength;
//        int yPosition = maxLength;
//
//        char[] xResult = new char[maxLength + 1];
//        char[] yResult = new char[maxLength + 1];
//
//        List<Integer> P = new ArrayList<>(this.P);
//        Collections.reverse(P);
//
//        while ( !(i == 0 || j == 0)) {
//            if (x.charAt(i - 1) == y.charAt(j - 1)) {
//                xResult[xPosition--] = x.charAt(i - 1);
//                yResult[yPosition--] = y.charAt(j - 1);
//                i--;
//                j--;
//            }
//            else if (dp[i - 1][j - 1] + SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i-1))][ALPHABETS.indexOf(y.charAt(j-1))] == dp[i][j]) {
//                xResult[xPosition--] = x.charAt(i - 1);
//                yResult[yPosition--] = y.charAt(j - 1);
//                i--;
//                j--;
//            }
//            else if (dp[i - 1][j] + SequenceAlignment.GAP_PENALTY == dp[i][j]) {
//                xResult[xPosition--] = x.charAt(i - 1);
//                yResult[yPosition--] = '_';
//                i--;
//            }
//            else if (dp[i][j - 1] + SequenceAlignment.GAP_PENALTY == dp[i][j]) {
//                xResult[xPosition--] = '_';
//                yResult[yPosition--] = y.charAt(j - 1);
//                j--;
//            }
//        }
//        while (xPosition > 0) {
//            if (i > 0) xResult[xPosition--] = x.charAt(--i);
//            else xResult[xPosition--] = (int)'_';
//        }
//        while (yPosition > 0) {
//            if (j > 0) yResult[yPosition--] = y.charAt(--j);
//            else yResult[yPosition--] = (int)'_';
//        }
//
//        // Todo: To remove extra gaps or not? Refer Geeksforgeeks
//        int id = 1;
//        for (i = maxLength; i >= 1; i--) {
//            if (yResult[i] == '_' && xResult[i] == '_') {
//                id = i + 1;
//                break;
//            }
//        }
////        for (i = id; i <= maxLength; i++) {
////            System.out.print(xResult[i]);
////        }
////        System.out.print("\n");
////        for (i = id; i <= maxLength; i++)
////        {
////            System.out.print(yResult[i]);
////        }
//        return new String[]{new String(xResult), new String(yResult)};
//    }

}

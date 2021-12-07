package main.java.usc.project.impl;

import main.java.usc.project.beans.Pair;
import main.java.usc.project.constants.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("FieldMayBeFinal")
public class SequenceAlignment {

    private int[][] dp;
    private List<Pair> P;
    public List<Pair> getP() {
        return this.P;
    }

    public SequenceAlignment(String X, String Y) {
        this.dp = new int[X.length() + 1][Y.length() + 1];
        this.P = new ArrayList<>();
    }


    public float alignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        for (int i = 0; i <= m; i++) {
            this.dp[i][0] = i * Constants.GAP_PENALTY;
        }
        for (int j = 0; j <= n; j++) {
            this.dp[0][j] = j * Constants.GAP_PENALTY;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                this.dp[i][j] = Math.min(
                        Math.min(
                                Constants.GAP_PENALTY + this.dp[i - 1][j], Constants.GAP_PENALTY + this.dp[i][j - 1]
                        ), Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + this.dp[i - 1][j - 1]);
            }
        }
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
        for (int i = 0; i <= m; i++) {
            B[i][0] = i * Constants.GAP_PENALTY;
        }
//
//        if(X.length() == 0 || Y.length() == 0) {
//            return B;
//        }

        // Find cost for X and Y
        for (int j = 1; j < n; j++) {
            B[0][1] = j * Constants.GAP_PENALTY;
            for (int i = 1; i <= m; i++) {
                B[i][1] = Math.min(
                        Math.min(
                                Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i))][Constants.ALPHABETS.indexOf(Y.charAt(j))] + B[i - 1][0], Constants.GAP_PENALTY + B[i - 1][1]
                        ), Constants.GAP_PENALTY + B[i][0]);
            }

            // Swap the columns, to be ready for next iteration
            for (int i = 0; i <= m; i++) {
                B[i][0] = B[i][1];
            }
        }

        return B;
    }


    public int[][] backwardSpaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m + 1][2];
        for (int i = m; i >= 0; i--) {
            B[i][1] = (m - i) * Constants.GAP_PENALTY;
        }

        if(X.length() == 0 || Y.length() == 0) {
            return B;
        }

        for (int j = n - 1; j >= 0; j--) {
            B[m][0] = (n - j) * Constants.GAP_PENALTY;

            for (int i = m - 1; i >= 0; i--) {
                B[i][0] = Math.min(
                        Math.min(
                                Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i))][Constants.ALPHABETS.indexOf(Y.charAt(j))] + B[i + 1][1], Constants.GAP_PENALTY + B[i + 1][0]
                        ), Constants.GAP_PENALTY + B[i][1]);
            }

            // Swap the columns to prepare for next iteration
            for (int i = 0; i <= m; i++) {
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
//
//        if(X.length() == 0 || Y.length() == 0) {
//            return;
//        }

        int[][] left = spaceEfficientAlignment(X, Y.substring(0, n / 2));
        int[][] right = spaceEfficientAlignment(new StringBuilder(X).reverse().toString(), new StringBuilder(Y).reverse().substring(n / 2, n));

        int min = left[0][1] + right[0][0];
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
                alreadyAdded = false;
                break;
            }
        }
        if(alreadyAdded) {
            P.add(new Pair(best_q, n / 2, left[best_q][1]));
        }

        if(X.length() == 0 || Y.length() == 0) {
            return;
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
            } else if (this.dp[i - 1][j - 1] + Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(x.charAt(i - 1))][Constants.ALPHABETS.indexOf(y.charAt(j - 1))] == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i - 1);
                yResult[yPosition--] = y.charAt(j - 1);
                i--;
                j--;
            } else if (this.dp[i][j - 1] + Constants.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = y.charAt(j - 1);
                j--;
            } else if (this.dp[i - 1][j] + Constants.GAP_PENALTY == this.dp[i][j]) {
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

        return new String[]{new String(xResult), new String(yResult)};
    }


    public String[] getAlignmentDC(String X, String Y) {

        // Sort the P list based on X locations
        List<Pair> P = this.P.stream().sorted(Comparator.comparingInt(Pair::getX)).collect(Collectors.toList());
        System.out.println("sorted pair seq. :: " + P);

        int previousX = -1, previousY=-1;
        for (int i = 0; i < P.size() ; i++) {
            if (previousY > P.get(i).getY()){
                Collections.swap(P, i-1, i);
            }
            previousY = this.P.get(i).getY();
        }

        System.out.println("Prev Y :: " + previousY);
        System.out.println("P :: " + P);

        // Print sequence X
        StringBuilder resultX = new StringBuilder();
        for (Pair pair : P) {
            if (pair.getX() < 0 || pair.getX() == previousX) {
                resultX.append("_");
            } else {
                resultX.append(X.charAt(pair.getX()));
            }
            previousX = pair.getX();
        }

        // Print middle line
        previousX = -1;
        previousY = -1;
        for(Pair pair : P) {
            if(X.charAt(pair.getX()) == Y.charAt(pair.getY()) && previousX != pair.getX() && previousY != pair.getY()) {
                System.out.print("|");
            } else {
                System.out.print(" ");
            }
            previousX = pair.getX();
            previousY = pair.getY();
        }

        // Print sequence Y
        previousY = -1;
        StringBuilder resultY = new StringBuilder();
        for (Pair pair : P) {
            if (pair.getY() < 0 || pair.getY() == previousY){
                resultX.append("_");
            }else {
                resultX.append(Y.charAt(pair.getX()));
            }
            previousY = pair.getY();
        }
        return new String[]{new String(resultX), new String(resultY)};
    }

}

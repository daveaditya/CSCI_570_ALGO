package edu.usc.algorithm_design;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Pair> P = null;

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
        int m = X.length();
        int n = Y.length();

        String xFlip= null, yFlip= null;
        StringBuilder str = new StringBuilder();
        str.append(X);
        xFlip = String.valueOf(str.reverse());

        StringBuilder str1 = new StringBuilder();
        str1.append(Y);
        yFlip = String.valueOf(str1.reverse());

        if(m <= 2 || n <= 2){
            this.alignment(X,Y);
        }

        int[] left = spaceEfficientAlignment(X, Y.substring(0, n/2));
        int[] right = spaceEfficientAlignment(xFlip, yFlip.substring(0, n/2));

        int best_cost = Integer.MAX_VALUE;
        int best_q = -1;
        for (int q = 0; q < n ; q++) {
            int cost = left[q] + right[q+1];
            if(cost < best_cost) {
                best_cost = cost;
                best_q = q;
            }
        }

        //int q =0;
        P.add(new Pair(best_q, n/2));
        divideAndConquerAlignment(X.substring(1,best_q), Y.substring(1, n/2));
        divideAndConquerAlignment(X.substring(best_q+1,n), Y.substring(n/2 + 1, n));
    }

    public List<Pair> getP(){
        return this.P;
    }

//    public void divideAndConquerAlignment_Lecture(String X, String Y) {
//        int x = X.length();
//        int y = Y.length();
//        String xFlip= null, yFlip= null;
//        StringBuilder str = new StringBuilder();
//        str.append(X);
//        xFlip = String.valueOf(str.reverse());
//
//        StringBuilder str1 = new StringBuilder();
//        str1.append(Y);
//        yFlip = String.valueOf(str1.reverse());
//
//        // call alignment for Xleft to all of Y
//        // call alignment for Xright (flip) and Xright (flip)
//        // match the cost of alignment - that point is the DP point on Y.
//        int[] xleft_cost;
//        int[] xright_cost;
//        xleft_cost = spaceEfficientAlignment(X.substring(0,x/2), Y);   // gives opt value of xleft and all of Y
//        xright_cost = spaceEfficientAlignment(xFlip.substring(0, x/2) , yFlip);  // gives opt value of xright and all of Y flipped
//        System.out.println("Xleft__ " + xleft_cost);
//        System.out.println("Xright__ " + xright_cost);
//
//
//
//
//
//
//
////        if(x <= 2 || y <= 2){
////            this.alignment(X,Y);
////        }
////        System.out.println(" Space Efficient alignment :: " + alignment(X,Y));
////
////
////        System.out.println("sa 1 :: " + X);
////        System.out.println("\n sa y :: " + Y.substring(0,y/2));
////
////        System.out.println("\n sa 2 :: " + xFlip);
////        System.out.println("\n yFlip only:: " + yFlip);
////        System.out.println("\n sa 2 :: " + yFlip.substring(0,y/2));
////
////
////        spaceEfficientAlignment(X, Y.substring(0, y/2));
////        spaceEfficientAlignment(xFlip, yFlip.substring(0, y/2));
////
////        int best_cost = Integer.MAX_VALUE;
////        int best_q = x; // value for best_q
////        for (int q = 0; q <= x ; q++) {
////
////            int cost = Integer.parseInt(Y.substring(q) + Y.substring(q+1)); //post flip check q for Y
////            if(cost < best_cost) {
////                best_cost = cost;
////                best_q = q;
////            }
////        }
////        System.out.println(best_q);
////        P.add(best_q,y/2);
////        System.out.println("P == " + P);
////        divideAndConquerAlignment(X.substring(1,best_q), Y.substring(1, y/2));
////        divideAndConquerAlignment(X.substring(1,best_q), Y.substring(1, y/2));
//    }


    /**
     * @param X
     * @param Y
     * @return
     */
    public int[] spaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m][2];
        for (int j =1; j < m ; j++){
            B[j][0] = j * SequenceAlignment.GAP_PENALTY;
        }

        for (int j = 1; j < n; j++) {
            B[0][1] = j * SequenceAlignment.GAP_PENALTY;
            for (int i = 1; i < m; i++) {
                B[i][1] = Math.min(Math.min(SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i - 1))][ALPHABETS.indexOf(Y.charAt(j - 1))] + B[i - 1][0],
                        SequenceAlignment.GAP_PENALTY + B[i - 1][1]), SequenceAlignment.GAP_PENALTY + B[i][0]);
            }
            for (int i = 1; i < m; i++) {
                B[i][0] = B[i][1];
            }
        }

        int[] R = new int[m];
        for (int i = 0; i < m; i++) {
            R[i] = B[i][1];
        }
        return R;
    }

    public void backwardSpaceEfficientAlignment(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] B = new int[m][2];
        for (int j =1; j < m ; j++){
            B[j][0] = j * SequenceAlignment.GAP_PENALTY;
        }

        for (int j = 1; j < n; j++) {
            B[0][1] = j * SequenceAlignment.GAP_PENALTY;
            for (int i = 1; i < m; i++) {
                B[i][1] = Math.min(Math.min(SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i - 1))][ALPHABETS.indexOf(Y.charAt(j - 1))] + B[i - 1][0],
                        SequenceAlignment.GAP_PENALTY + B[i - 1][1]), SequenceAlignment.GAP_PENALTY + B[i][0]);
            }
            for (int i = 1; i < m; i++) {
                B[i][0] = B[i][1];
            }
        }

//        int[] R = new int[m];
//        for (int i = 0; i < m; i++) {
//            R[i] = B[i][1];
//        }
//        return R;
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
            if (x.charAt(i - 1) == y.charAt(j-1)) {
                xResult[xPosition--] = x.charAt(i-1);
                yResult[yPosition--] = y.charAt(j-1);
                i--;
                j--;
            }
            else if (this.dp[i - 1][j - 1] + SequenceAlignment.MISMATCH_COST[ALPHABETS.indexOf(x.charAt(i-1))][ALPHABETS.indexOf(y.charAt(j-1))] == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i-1);
                yResult[yPosition--] = y.charAt(j-1);
                i--;
                j--;
            }
            else if (this.dp[i][j - 1] + SequenceAlignment.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = y.charAt(j-1);
                j--;
            }
            else if (this.dp[i - 1][j] + SequenceAlignment.GAP_PENALTY == this.dp[i][j]) {
                xResult[xPosition--] = x.charAt(i-1);
                yResult[yPosition--] = '_';
                i--;
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

        int id = 1;
        for (i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }

        for (i = id; i <= maxLength; i++) {
            System.out.print((char)xResult[i]);
        }
        System.out.print("\n");
        for (i = id; i <= maxLength; i++) {
            System.out.print((char)yResult[i]);
        }
        return new String[]{new String(xResult), new String(yResult)};
    }

//
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

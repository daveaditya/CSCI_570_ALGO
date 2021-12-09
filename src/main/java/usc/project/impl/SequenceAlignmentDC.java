package main.java.usc.project.impl;

import main.java.usc.project.beans.Pair;
import main.java.usc.project.constants.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("FieldMayBeFinal")
public class SequenceAlignmentDC {

    private int[][] dp;
    private List<Pair> P;
    public List<Pair> getP() {
        return this.P;
    }

    public SequenceAlignmentDC(String X, String Y) {
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

    public int spaceEfficient(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        for(int i=0;i<=m;++i) {
            dp[i][0]=i*Constants.GAP_PENALTY;
        }

        for(int j=1;j<=n;++j)
        {
            dp[0][1]=j*Constants.GAP_PENALTY;

            for(int i=1;i<=m;++i) {
                dp[i][1] = Math.min(
                        Math.min(
                                Constants.GAP_PENALTY + this.dp[i - 1][0], Constants.GAP_PENALTY + this.dp[i][0]
                        ), Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i - 1))][Constants.ALPHABETS.indexOf(Y.charAt(j - 1))] + this.dp[i - 1][0]);
            }
            // move column 1 of dp to column 0 to make room for next iteration
            for(int i=0;i<=m;++i)
                dp[i][0]=dp[i][1];
        }
        return dp[m][1];
    }

    public int backwardSpaceEfficientAlignment(String X, String Y)
    {
        int m = X.length();
        int n = Y.length();

        for(int i=m;i>=0;--i) {
            dp[i][1]=(m-i)*Constants.GAP_PENALTY;
        }

        for(int j=n-1;j>=0;--j) {
            dp[m][0]=(n-j)*Constants.GAP_PENALTY;

            for(int i=m-1;i>=0;--i) {
                dp[i][0] = Math.min(
                        Math.min(
                                Constants.GAP_PENALTY + this.dp[i + 1][0], Constants.GAP_PENALTY + this.dp[i][1]
                        ), Constants.MISMATCH_COST[Constants.ALPHABETS.indexOf(X.charAt(i))][Constants.ALPHABETS.indexOf(Y.charAt(j))] + this.dp[i + 1][1]);
            }
            // move column 0 of dp to column 1 to make room for next iteration
            for(int i=0;i<=m;++i)
                dp[i][1]=dp[i][0];
        }
        return dp[0][0];
    }


    /**
     * @param X
     * @param Y
     * @return
     */

    public List<Pair> divideAndConquerAlignment_Lecture(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        if (m <= 2 || n <= 2){
            this.alignment(X,Y);
        }
        int left = spaceEfficient(X, Y.substring(0, n / 2));
        int right = backwardSpaceEfficientAlignment(X, Y.substring(1, n/2 + 1));
        int total = left + right;

        System.out.println("f : Shortest path from (0,0) to (i,j) :: " + left);
        System.out.println("g : Shortest path from (i,j) to (m,n) :: " + right);
        System.out.println(" Corner to Corner path :: " + total);

        // find min score
        int min=dp[1][1]+dp[1][0];
        int ret=1;
        int q = 0;
        for(q=2;q<=m;++q) {
            if(min>dp[q][1]+dp[q][0]) {
                min=dp[q][1]+dp[q][0];
                ret=q-1; // ret should be the position in the sequence, so minus 1
            }
        }
        System.out.println("Min cost :: " + min);
        System.out.println("Min index :: " + ret);
        System.out.println("q :: " + q);

        P.add(new Pair(ret, n / 2, dp[ret][1]));
        System.out.println("P :: " + P);


        divideAndConquerAlignment_Lecture(X.substring(0, ret), Y.substring(0, n / 2));
        divideAndConquerAlignment_Lecture(X.substring(ret, n), Y.substring(n / 2 + 1, n));
        return P;
    }

}

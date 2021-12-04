package edu.usc.algorithm_design;

public class SequenceAlignmentDP implements SequenceAlignment {

    private String X = null;
    private String Y = null;
    private int[][] dp = null;

    public SequenceAlignmentDP(String X, String Y) {
        this.X = X;
        this.Y = Y;
        this.dp = new int[this.X.length() + 1][this.Y.length() + 1];
    }

    /**
     * Alignment
     */
    public void run(){
        System.out.println("First Base: " + X);
        System.out.println("Second Base: " + Y);
        StringBuilder result = new StringBuilder();

        int xLength = this.X.length();
        int yLength = this.Y.length();

        for (int i = 0; i < xLength; i++) {
            this.dp[i][0] = i * this.GAP_PENALTY;
        }
        for (int j = 0; j < yLength; j++) {
            this.dp[0][j] = j * this.GAP_PENALTY;
        }

        for (int i = 1; i < xLength; i++) {
            for (int j = 1; j < yLength; j++) {
                this.dp[i][j] = Math.min(
                        Math.min(
                                this.GAP_PENALTY + this.dp[i - 1][j], this.GAP_PENALTY + this.dp[i][j - 1]
                        ), this.MISMATCH_COST[ALPHABETS.indexOf(X.charAt(i))][ALPHABETS.indexOf(Y.charAt(j))] + this.dp[i - 1][j - 1]);
            }
        }
    }


    @Override
    public String[] getAlignment() {
        int maxLength = this.X.length() + this.X.length();

        int i = this.X.length();
        int j = this.X.length();

        int xPosition = maxLength;
        int yPosition = maxLength;

        char[] xResult = new char[maxLength + 1];
        char[] yResult = new char[maxLength + 1];

        while ( !(i == 0 || j == 0)) {
            if (this.X.charAt(i - 1) == this.Y.charAt(j - 1)) {
                xResult[xPosition--] = this.X.charAt(i - 1);
                yResult[yPosition--] = this.Y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j - 1] + this.MISMATCH_COST[ALPHABETS.indexOf(this.X.charAt(i))][ALPHABETS.indexOf(this.Y.charAt(j))] == dp[i][j]) {
                xResult[xPosition--] = this.X.charAt(i - 1);
                yResult[yPosition--] = this.Y.charAt(j - 1);
                i--;
                j--;
            } else if (dp[i - 1][j] + this.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = this.X.charAt(i - 1);
                yResult[yPosition--] = '_';
                i--;
            } else if (dp[i][j - 1] + this.GAP_PENALTY == dp[i][j]) {
                xResult[xPosition--] = '_';
                yResult[yPosition--] = this.Y.charAt(j - 1);
                j--;
            }
        }

        while (xPosition > 0) {
            if (i > 0) xResult[xPosition--] = this.X.charAt(--i);
            else xResult[xPosition--] = (int)'_';
        }
        while (yPosition > 0) {
            if (j > 0) yResult[yPosition--] = this.Y.charAt(--j);
            else yResult[yPosition--] = (int)'_';
        }

        // Todo: To remove extra gaps or not? Refer Geeksforgeeks

        return new String[]{new String(xResult), new String(yResult)};
    }

}

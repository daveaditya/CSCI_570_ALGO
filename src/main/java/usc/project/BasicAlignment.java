package main.java.usc.project;

import main.java.usc.project.impl.InputStringGenerator;
import main.java.usc.project.impl.SequenceAlignment;
import main.java.usc.project.beans.GeneratedOutput;
import main.java.usc.project.beans.ParsedInput;
import main.java.usc.project.constants.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicAlignment {

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("No input file provided.");
            System.out.println("Usage: java Main.java <absolute-file-path>");
            System.exit(1);
        }

        // Create file object
        String inputFileLocation = args[0];

        try {

            long startTime = (System.currentTimeMillis() / 1000);
            ParsedInput input = parseInputFile(inputFileLocation);

            // Generate strings for input to the sequence alignment from the input
            String firstGeneratedBase = InputStringGenerator.run(input.getFirstBase(), input.getFirstBaseIndices());
            String secondGeneratedBase = InputStringGenerator.run(input.getSecondBase(), input.getSecondBaseIndices());

            SequenceAlignment sequenceAlignment = new SequenceAlignment(firstGeneratedBase, secondGeneratedBase);
            float alignmentCost = sequenceAlignment.alignment(firstGeneratedBase, secondGeneratedBase);
            String[] resultAlignment = sequenceAlignment.getAlignment(firstGeneratedBase, secondGeneratedBase);
            String firstAlignment = printAlignmentX(firstGeneratedBase, secondGeneratedBase, resultAlignment[0].toCharArray(), resultAlignment[1].toCharArray());
            String secondAlignment = printAlignmentY(firstGeneratedBase, secondGeneratedBase, resultAlignment[0].toCharArray(), resultAlignment[1].toCharArray());

            long stopTime = (System.currentTimeMillis() / 1000);
            long elapsedTime = stopTime - startTime;

            // memory required for alignment cost and print operations displayed in file
            double memoryUsed = memoryConsumption();
            // write to the output.txt file
            GeneratedOutput output = new GeneratedOutput(formatAlignment(firstAlignment), formatAlignment(secondAlignment), alignmentCost, elapsedTime, memoryUsed);
            writeOutput(Constants.OUTPUT_FILE, output.toString());
        } catch(FileNotFoundException exc) {
            System.out.println("The input file `" + inputFileLocation + "` does not exists. Please try again!");
        }
    }

    public static String formatAlignment(String alignmentStr) {
        String outputAlignment = null;
        if(alignmentStr.length() > 50){
            outputAlignment = alignmentStr.substring(0,50) + " " + alignmentStr.substring(alignmentStr.length() - 50);
        }
            return outputAlignment;
    }

    /**
     * @param X
     * @param Y
     * @param xResult
     * @param yResult
     * @return Alignment of X
     */
    public static String printAlignmentX(String X, String Y, char[] xResult, char[] yResult) {
        StringBuffer stringBuffer = new StringBuffer();
        int maxLength = X.length() + Y.length();
        int id = 1;
        for (int i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }
        for (int i = id; i <= maxLength; i++) {
            System.out.print(xResult[i]);
            stringBuffer.append(xResult[i]);
        }
        return String.valueOf(stringBuffer);
    }

    /**
     * @param X
     * @param Y
     * @param xResult
     * @param yResult
     * @return Alignment of Y
     */
    public static String printAlignmentY(String X, String Y, char[] xResult, char[] yResult) {
        StringBuffer stringBuffer = new StringBuffer();
        int maxLength = X.length() + Y.length();
        int id = 1;
        for (int i = maxLength; i >= 1; i--) {
            if (xResult[i] == '_' && yResult[i] == '_') {
                id = i + 1;
                break;
            }
        }
        System.out.print("\n");
        for (int i = id; i <= maxLength; i++) {
            System.out.print(yResult[i]);
            stringBuffer.append(yResult[i]);
        }
        return String.valueOf(stringBuffer);
    }

    /** Input validation -> 2j*str1.length and 2k*str2.length
     * @param firstBase First base string
     * @param noOfFirstBaseIndices No of indices from the input file for the first base string
     * @param secondBase Second base string
     * @param noOfSecondBaseIndices No of indices from the input file for the second base string
     */
    private static boolean validateInput(String firstBase, int noOfFirstBaseIndices, String secondBase, int noOfSecondBaseIndices) {
        // j and k validation TODO: change later
        if (firstBase.length() == (2 ^ noOfFirstBaseIndices) * firstBase.length()) {
            System.out.println("Valid j");
        } else {
            System.out.println("Invalid j");
        }
        if (secondBase.length() == (2 ^ noOfSecondBaseIndices) * secondBase.length()) {
            System.out.println("Valid k");
        } else {
            System.out.println("Invalid k");
        }

        return (
                firstBase.length() != (2 ^ noOfFirstBaseIndices) * firstBase.length() ||
                        secondBase.length() != (2 ^ noOfSecondBaseIndices) * secondBase.length()
        );
    }

    /** Read the file input bases and indices
     * @param fileLocation Input file given to the program
     * @throws FileNotFoundException If the input file is not found
     */
    private static ParsedInput parseInputFile(String fileLocation) throws FileNotFoundException {
        List<Integer> firstBaseIndices = new ArrayList<>();
        List<Integer> secondBaseIndices = new ArrayList<>();
        Scanner sc = new Scanner(new File(fileLocation));

        String firstBase = sc.nextLine();
        System.out.println("First Base string = " + firstBase);
        while (sc.hasNextInt()) {
            firstBaseIndices.add(sc.nextInt());
        }
        System.out.println("Indices for first base = " + firstBaseIndices);

        String secondBase = sc.next();
        System.out.println("Second Base string = " + secondBase);
        while (sc.hasNextInt()) {
            secondBaseIndices.add(sc.nextInt());
        }
        System.out.println("Indices for second base = " + secondBaseIndices);

        // validation for j and k that can be reused in the algo.
        if(!validateInput(firstBase, firstBaseIndices.size(), secondBase, secondBaseIndices.size())) {
            System.out.println("The input file does not pass input validation.");
            System.exit(1);
        }

        return new ParsedInput(firstBase, firstBaseIndices, secondBase, secondBaseIndices);
    }

    /**
     * @param outputFile
     * @param output
     */
    private static void writeOutput(String outputFile, String output) {
        try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile))) {
            outputWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return memory consumption in kilobytes
     */
    private static double memoryConsumption() {
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory in kilobytes
        long memory = bytesToKilobytes(runtime.totalMemory() - runtime.freeMemory());
        return memory;
    }

    /**
     * @param bytes
     * @return
     */
    public static long bytesToKilobytes(long bytes) {
        return bytes / Constants.KILOBYTE;
    }

}

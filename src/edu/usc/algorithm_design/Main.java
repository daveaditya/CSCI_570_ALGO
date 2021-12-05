package edu.usc.algorithm_design;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String OUTPUT_FILE = "./output.txt";

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("No input file provided.");
            System.out.println("Usage: java Main.java <absolute-file-path>");
            System.exit(1);
        }

        // Create file object
        String inputFileLocation = args[0];

        try {
            ParsedInput input = parseInputFile(inputFileLocation);

            System.out.println("Output: \n" + input);

            // Generate strings for input to the sequence alignment from the input
            String firstGeneratedBase = InputStringGenerator.run(input.getFirstBase(), input.getFirstBaseIndices());
            String secondGeneratedBase = InputStringGenerator.run(input.getSecondBase(), input.getSecondBaseIndices());

            System.out.println("First Base :: " + firstGeneratedBase);
            System.out.println("Second Base :: " + secondGeneratedBase);

            GeneratedOutput output = new GeneratedOutput(null, null, 0, 0.0);

            SequenceAlignment sequenceAlignment = new SequenceAlignment(firstGeneratedBase, secondGeneratedBase);
            sequenceAlignment.alignment(firstGeneratedBase, secondGeneratedBase);
            String[] resultAlignment = sequenceAlignment.getAlignment(firstGeneratedBase, secondGeneratedBase);
            System.out.println("First String" + resultAlignment[0]);
            System.out.println("Second String" + resultAlignment[1]);

            writeOutput(Main.OUTPUT_FILE, output.toString());
        } catch(FileNotFoundException exc) {
            System.out.println("The input file `" + inputFileLocation + "` does not exists. Please try again!");
        }
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

        // TODO : remove later. This part is to check only for the string generation part.
//        String finalString1 = null;
//        String finalString2 = null;
//        String newString = null;
//        if (firstBase != null || firstBaseIndices != null) {
//            finalString1 = inputStringGenerator(firstBase, firstBaseIndices); // currently only for first base
//        }
//        if (secondBase != null || secondBaseIndices != null) {
//            finalString2 = inputStringGenerator(secondBase, secondBaseIndices); // currently only for first base
//        }
//        newString = finalString1 + "\n" + finalString2;
//        return newString;
    }

    private static void writeOutput(String outputFile, String output) {
        try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile))) {
            outputWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

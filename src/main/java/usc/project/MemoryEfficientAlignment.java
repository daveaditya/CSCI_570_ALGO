package main.java.usc.project;

import main.java.usc.project.impl.InputStringGenerator;
import main.java.usc.project.impl.SequenceAlignment;
import main.java.usc.project.beans.GeneratedOutput;
import main.java.usc.project.beans.ParsedInput;
import main.java.usc.project.constants.Constants;
import main.java.usc.project.impl.SequenceAlignmentDC;
//import main.java.usc.project.impl.SequenceAlignmentDC;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemoryEfficientAlignment {

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

            // Generate strings for input to the sequence alignment from the input
            String firstGeneratedBase = InputStringGenerator.run(input.getFirstBase(), input.getFirstBaseIndices());
            String secondGeneratedBase = InputStringGenerator.run(input.getSecondBase(), input.getSecondBaseIndices());


            SequenceAlignmentDC sequenceAlignment = new SequenceAlignmentDC(firstGeneratedBase, secondGeneratedBase);
            sequenceAlignment.divideAndConquerAlignment_Lecture(firstGeneratedBase, secondGeneratedBase);
            //String[] result = sequenceAlignment.getAlignmentDC(firstGeneratedBase,secondGeneratedBase);


            GeneratedOutput output = new GeneratedOutput(null, null, 0,0, 0.0);
            writeOutput(Constants.OUTPUT_FILE, output.toString());
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
        while (sc.hasNextInt()) {
            firstBaseIndices.add(sc.nextInt());
        }

        String secondBase = sc.next();
        while (sc.hasNextInt()) {
            secondBaseIndices.add(sc.nextInt());
        }

        // validation for j and k that can be reused in the algo.
        if(!validateInput(firstBase, firstBaseIndices.size(), secondBase, secondBaseIndices.size())) {
            System.out.println("The input file does not pass input validation.");
            System.exit(1);
        }

        return new ParsedInput(firstBase, firstBaseIndices, secondBase, secondBaseIndices);

    }

    private static void writeOutput(String outputFile, String output) {
        try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile))) {
            outputWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

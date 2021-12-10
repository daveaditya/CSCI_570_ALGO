package main.java.usc.project.utils;

import main.java.usc.project.beans.ParsedInput;
import main.java.usc.project.constants.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utilities {

    public static String formatAlignment(char[] alignmentStr) {
        String outputAlignment;
        if(alignmentStr.length > 50){
            outputAlignment = new String(alignmentStr).substring(0, 50) + " " + new String(alignmentStr).substring(alignmentStr.length - 50);
        } else {
            // not required... can be removed
            outputAlignment = Arrays.toString(alignmentStr) + " " + Arrays.toString(alignmentStr);
        }
        return outputAlignment;
    }


    public static float bytesToKilobytes(long bytes) {
        return (float) bytes / Constants.KILOBYTE;
    }


    public static void writeOutput(String outputFile, String output) {
        try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile))) {
            outputWriter.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ParsedInput parseInputFile(String fileLocation) throws FileNotFoundException {
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

        return new ParsedInput(firstBase, firstBaseIndices, secondBase, secondBaseIndices);
    }

}

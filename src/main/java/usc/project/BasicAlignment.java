package main.java.usc.project;

import main.java.usc.project.beans.AlignmentOutput;
import main.java.usc.project.impl.InputStringGenerator;
import main.java.usc.project.impl.SequenceAlignment;
import main.java.usc.project.beans.GeneratedOutput;
import main.java.usc.project.beans.ParsedInput;
import main.java.usc.project.constants.Constants;
import main.java.usc.project.utils.Timer;
import main.java.usc.project.utils.Utilities;

import java.io.*;

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

            ParsedInput input = Utilities.parseInputFile(inputFileLocation);

            // Generate strings for input to the sequence alignment from the input
            char[] firstGeneratedBase = InputStringGenerator.run(input.getFirstBase(), input.getFirstBaseIndices());
            char[] secondGeneratedBase = InputStringGenerator.run(input.getSecondBase(), input.getSecondBaseIndices());

            Timer timer = new Timer();

            SequenceAlignment sequenceAlignment = new SequenceAlignment();

            // Run basic version of sequence alignment
            AlignmentOutput alignmentOutput = sequenceAlignment.alignmentWithDynamicProgramming(firstGeneratedBase, secondGeneratedBase);

            timer.end();

            // write to the output.txt file
            GeneratedOutput output = new GeneratedOutput(
                    Utilities.formatAlignment(alignmentOutput.getFirstAlignment()),
                    Utilities.formatAlignment(alignmentOutput.getSecondAlignment()),
                    alignmentOutput.getCost(),
                    (timer.duration() / 1000.0),
                    Utilities.bytesToKilobytes(timer.memory())
            );

            Utilities.writeOutput(Constants.OUTPUT_FILE, output.toString());
        } catch(FileNotFoundException exc) {
            System.out.println("The input file `" + inputFileLocation + "` does not exists. Please try again!");
        }
    }

}

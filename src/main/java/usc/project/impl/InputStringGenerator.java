package main.java.usc.project.impl;

import java.util.List;

public class InputStringGenerator {

    /**
     * @param inputBase
     * @param indices
     */
    public static String run(String inputBase, List<Integer> indices){
        StringBuilder output = new StringBuilder(inputBase);

        String appendString = output.toString();

        for (int i = 0; i < indices.size(); i++) {

            int index = indices.get(i);
            System.out.println(index);

            if(i == 0) {
                output.insert(index + 1, inputBase);
                System.out.println(output);
                appendString = String.valueOf(output);
            } else {
                output.insert(index + 1, appendString);
                appendString = String.valueOf(output);
                System.out.println(output);
            }

        }
        return output.toString();
    }

}
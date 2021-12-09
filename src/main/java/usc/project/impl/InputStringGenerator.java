package main.java.usc.project.impl;

import java.util.List;

public class InputStringGenerator {

    public static char[] run(String inputBase, List<Integer> indices){
        StringBuilder output = new StringBuilder(inputBase);
        String appendString = output.toString();

        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            if(i == 0) {
                output.insert(index + 1, inputBase);
                appendString = String.valueOf(output);
            } else {
                output.insert(index + 1, appendString);
                appendString = String.valueOf(output);
            }
        }
        return output.toString().toCharArray();
    }
}
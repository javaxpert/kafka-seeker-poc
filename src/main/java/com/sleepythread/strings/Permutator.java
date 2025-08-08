package com.sleepythread.strings;

import java.util.ArrayList;
import java.util.List;

public class Permutator {

    private List<String> permutations = new ArrayList<>();

    public List<String> getAllPermutations(String value) {
        var charArray = value.toCharArray();
        permutate(charArray, 0, charArray.length - 1);
        return List.of();
    }

    private String permutate(char [] chars, int index, int pos) {
        if (pos >= 0) {
            var copy = new char[chars.length];
            System.arraycopy(chars, 0, copy, 0, chars.length);
            char held = copy[index];
            copy[index] = copy[pos];
            copy[pos] = held;
            pos--;
            permutations.add(new String(copy));
            return permutate(chars, index, pos);

        }
        return new String(chars);
    }
}
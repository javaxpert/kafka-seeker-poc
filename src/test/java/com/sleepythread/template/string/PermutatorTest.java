package com.sleepythread.template.string;

import com.sleepythread.strings.Permutator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class PermutatorTest {

    Permutator permutator = new Permutator();

    @Test
    void pemutate() {
        var testValue = "abc";
        var mutations = permutator.getAllPermutations(testValue);
        log.info("{}", mutations);
    }
}

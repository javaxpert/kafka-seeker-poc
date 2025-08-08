package com.sleepythread.template.list;

import com.sleepythread.collections.LinkedList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


@Slf4j
public class LinkListReverserTest {


    @Test
    void reverseAList() {
        LinkedList<String> aList = new LinkedList<>();
        aList.add("a");
        aList.add("b");
        aList.add("c");
        aList.add("d");
        log.info("A list: {}", aList);
        aList.reverse();
        log.info("A list: {}", aList);
    }
}

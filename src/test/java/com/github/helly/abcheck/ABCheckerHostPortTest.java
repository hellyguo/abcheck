package com.github.helly.abcheck;

import org.junit.Test;

public class ABCheckerHostPortTest {

    @Test(expected = RuntimeException.class)
    public void test0StartFailTest() {
        ABChecker abChecker = new ABChecker("192.168.10.2:22222;192.168.10.3:22222");
        abChecker.init();
    }

    @Test
    public void test1StartSuccessTest() {
        ABChecker abChecker = new ABChecker("127.0.0.1:22222;192.168.10.3:22222");
        abChecker.init();
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            //
        }
        abChecker.destroy();
    }
}

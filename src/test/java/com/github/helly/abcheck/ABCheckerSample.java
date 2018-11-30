package com.github.helly.abcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ABCheckerSample {
    private static final Logger LOGGER = LoggerFactory.getLogger(ABCheckerSample.class);

    public static void main(String[] args) {
        ABCheckerSample sample = new ABCheckerSample();
        sample.test();
    }

    private void test() {
        ABChecker abChecker = new ABChecker("127.0.0.1:22222;127.0.0.1:22223");
        abChecker.init();
        for (int i = 0; i < 300; i++) {
            LOGGER.info("this node is main[{}]", abChecker.isMain());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                //
            }
        }
        abChecker.destroy();
    }
}

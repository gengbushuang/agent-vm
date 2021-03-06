package com.gbs.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpanId {

    public static final long NULL = -1;

//    private static final Random seed = new Random();

    public static long newSpanId() {
        final Random random = getRandom();

        return createSpanId(random);
    }

    // Changed to ThreadLocalRandom because unique value per thread will be enough.
    // If you need to change Random implementation, modify this method.
    private static Random getRandom() {
        return ThreadLocalRandom.current();
    }

    private static long createSpanId(Random seed) {
        long id = seed.nextLong();
        while (id == NULL) {
            id = seed.nextLong();
        }
        return id;
    }

    public static long nextSpanID(long spanId, long parentSpanId) {
        final Random seed = getRandom();

        long newId = createSpanId(seed);
        while (newId == spanId || newId == parentSpanId) {
            newId = createSpanId(seed);
        }
        return newId;
    }
}
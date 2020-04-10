package com.xusy.springbt.util;

import java.util.UUID;

public class UUIDGenerator {
    public static final UUIDGenerator INSTANCE = new UUIDGenerator();

    private static short counter = (short) 0;

    public static String sequentialUUIDString() {
        return INSTANCE.generateUUIDString();
    }

    public String generateUUIDString() {
        return generateUUID().toString().toUpperCase();
    }

    private UUID generateUUID() {
        long mostSignificantBits = (System.currentTimeMillis() << 20) | (getCount());
        long leastSignificantBits = UUID.randomUUID().getLeastSignificantBits();

        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    private short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }
}

package com.example.toiuu;

import java.math.BigInteger;

public class Fibonacci {
    static final int PRECOMPUTED_SIZE = 512;
    static BigInteger PRECOMPUTED[] = new BigInteger[PRECOMPUTED_SIZE];

    static {
        PRECOMPUTED[0] = BigInteger.ZERO;
        PRECOMPUTED[1] = BigInteger.ONE;
        for (int i = 2; i < PRECOMPUTED_SIZE; i++) {
            PRECOMPUTED[i] = PRECOMPUTED[i - 1].add(PRECOMPUTED[i - 2]);
        }
    }
}

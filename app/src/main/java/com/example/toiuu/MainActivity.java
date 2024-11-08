package com.example.toiuu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private EditText inputN;
    private TextView fibResult;
    private TextView iterFibResult;
    private TextView bigIntegerResult;
    private TextView bigIntegerAllocations;
    private TextView cachedFibResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputN = findViewById(R.id.inputN);
        fibResult = findViewById(R.id.FibResult);
        iterFibResult = findViewById(R.id.IterFibResult);
        bigIntegerResult = findViewById(R.id.BigIntegerFibResult);
        bigIntegerAllocations = findViewById(R.id.BigIntegerAllocations);
        cachedFibResult = findViewById(R.id.CachedFibResult);
        Button btnFibonacci = findViewById(R.id.btnFibonacci);
        Button btnIterativeFibonacci = findViewById(R.id.btnIterativeFibonacci);
        Button btnBigIntegerFibonacci = findViewById(R.id.btnBigIntegerFibonacci);
        Button btnCachedFibonacci = findViewById(R.id.btnCachedFibonacci);

        btnFibonacci.setOnClickListener(view -> FibonacciTime());
        btnIterativeFibonacci.setOnClickListener(view -> IterativeFibonacciTime());
        btnBigIntegerFibonacci.setOnClickListener(view -> BigIntegerFibonacciTime());
        btnCachedFibonacci.setOnClickListener(view -> CachedFibonacciTime());
    }

    private int getInputNumber() {
        try {
            return Integer.parseInt(inputN.getText().toString().trim());
        } catch (NumberFormatException e) {
            inputN.setError("Please enter a valid integer.");
            return -1;  // Return -1 if input is invalid
        }
    }

    private void FibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long totalElapsedTime = 0;
        int runs = 1;  // Number of test runs

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            computeRecursively(n);
            totalElapsedTime += System.nanoTime() - startTime;
        }

        long averageTime = totalElapsedTime / runs;
        fibResult.setText("Time of using Recursive: " + averageTime + " ns");
    }

    private void IterativeFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long totalElapsedTime = 0;
        int runs = 1;  // Number of test runs

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            computeIteratively(n);
            totalElapsedTime += System.nanoTime() - startTime;
        }

        long averageTime = totalElapsedTime / runs;
        iterFibResult.setText("Time of using Iterative: " + averageTime + " ns");
    }

    private void BigIntegerFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long totalElapsedTime = 0;
        long totalAllocations = 0;
        int runs = 1;  // Number of test runs

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            BigInteger result = computeRecursivelyFasterUsingBigInteger(n);
            totalElapsedTime += System.nanoTime() - startTime;

            totalAllocations += computeRecursivelyFasterUsingBigIntegerAllocations(n);
        }

        long averageTime = totalElapsedTime / runs;
        bigIntegerResult.setText("Time of using BigInteger: " + averageTime + " ns");
        bigIntegerAllocations.setText("Total number of BigInteger allocations: " + totalAllocations);
    }

    private void CachedFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long totalElapsedTime = 0;
        int runs = 1;  // Number of test runs

        for (int i = 0; i < runs; i++) {
            long startTime = System.nanoTime();
            BigInteger result = computeRecursivelyWithCache(n);  // Use cached version
            totalElapsedTime += System.nanoTime() - startTime;
        }

        long averageTime = totalElapsedTime / runs;
        cachedFibResult.setText("Time of using Cache: " + averageTime + " ns");
    }

    // Standard recursive Fibonacci
    public static long computeRecursively(int n) {
        if (n <= 1) return n;
        return computeRecursively(n - 1) + computeRecursively(n - 2);
    }

    // Iterative Fibonacci (no recursion)
    public static long computeIteratively(int n) {
        if (n > 1) {
            long a = 0, b = 1;
            do {
                long tmp = b;
                b += a;
                a = tmp;
            } while (--n > 1);
            return b;
        }
        return n;
    }

    // Fibonacci with BigInteger (faster recursive with matrix exponentiation)
    public static BigInteger computeRecursivelyFasterUsingBigInteger(int n) {
        if (n <= 1) return BigInteger.valueOf(n);
        int m = (n / 2) + (n & 1);
        BigInteger fM = computeRecursivelyFasterUsingBigInteger(m);
        BigInteger fM_1 = computeRecursivelyFasterUsingBigInteger(m - 1);
        if ((n & 1) == 1) {
            return fM.pow(2).add(fM_1.pow(2)); // F(m)^2 + F(m-1)^2
        } else {
            return fM_1.shiftLeft(1).add(fM).multiply(fM); // (2*F(m-1) + F(m)) * F(m)
        }
    }

    // Allocation counter for BigInteger Fibonacci computations
    public static long computeRecursivelyFasterUsingBigIntegerAllocations(int n) {
        long allocations = 0;
        if (n > 1) {
            int m = (n / 2) + (n & 1);
            allocations += computeRecursivelyFasterUsingBigIntegerAllocations(m);
            allocations += computeRecursivelyFasterUsingBigIntegerAllocations(m - 1);
            allocations += 3; // 3 more BigInteger objects allocated
        }
        return allocations;
    }

    // Fibonacci with cache (using HashMap)
    public static BigInteger computeRecursivelyWithCache(int n) {
        SparseArray<BigInteger> cache = new SparseArray<>();
        return computeRecursivelyWithCache(n, cache);
    }

    // Helper method for cached Fibonacci
    private static BigInteger computeRecursivelyWithCache(int n, SparseArray<BigInteger> cache) {
        if (n <= 1) return BigInteger.valueOf(n);

        BigInteger fN = cache.get(n);
        if (fN == null) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyWithCache(m, cache);
            BigInteger fM_1 = computeRecursivelyWithCache(m - 1, cache);
            if ((n & 1) == 1) {
                fN = fM.pow(2).add(fM_1.pow(2));
            } else {
                fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
            }
            cache.put(n, fN);
        }

        return fN;
    }
}

package com.example.toiuu;

import static com.example.toiuu.Fibonacci.PRECOMPUTED;
import static com.example.toiuu.Fibonacci.PRECOMPUTED_SIZE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.SparseArray;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private EditText inputN;
    private TextView RecursiveWithLoopfibResult;
    private TextView IterativeFibResult;
    private TextView IterativeUsingBigIntegerFibResult;
    private TextView bigIntegerResult;
    private TextView bigIntegerAllocations;
    private TextView RecursiveUsingBigIntegerAndPrimitiveFibResult;
    private TextView RecursiveUsingBigIntegerAndTableFibResult;
    private TextView cachedFibResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls() // API level 11, to use with StrictMode.noteSlowCode
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyFlashScreen() // API level 11
                .build());

        inputN = findViewById(R.id.inputN);
        RecursiveWithLoopfibResult = findViewById(R.id.RecursiveWithLoopResult);
        IterativeFibResult = findViewById(R.id.IterativeFibResult);
        IterativeUsingBigIntegerFibResult = findViewById(R.id.IterativeUsingBigIntegerFibResult);//1-6
        bigIntegerResult = findViewById(R.id.BigIntegerFibResult);
        bigIntegerAllocations = findViewById(R.id.BigIntegerAllocations);
        RecursiveUsingBigIntegerAndPrimitiveFibResult = findViewById(R.id.RecursiveUsingBigIntegerAndPrimitiveFibResult);
        RecursiveUsingBigIntegerAndTableFibResult = findViewById(R.id.RecursiveUsingBigIntegerAndTableFibResult);
        cachedFibResult = findViewById(R.id.CachedFibResult);

        Button btnRecursiveWithLoop = findViewById(R.id.btnRecursiveWithLoop);
        Button btnIterative = findViewById(R.id.btnIterative);
        Button btnIterativeUsingBigInteger = findViewById(R.id.btnIterativeUsingBigInteger); //1-6
        Button btnBigIntegerFibonacci = findViewById(R.id.btnBigIntegerFibonacci);
        Button btnRecursiveUsingBigIntegerAndPrimitiveFibonacci = findViewById(R.id.btnRecursiveUsingBigIntegerAndPrimitiveFibonacci);
        Button btnRecursiveUsingBigIntegerAndTableFibonacci = findViewById(R.id.btnRecursiveUsingBigIntegerAndTableFibonacci);
        Button btnCachedFibonacci = findViewById(R.id.btnCachedFibonacci);

        btnRecursiveWithLoop.setOnClickListener(view -> RecursiveWithLoopFibonacciTime());
        btnIterative.setOnClickListener(view -> IterativeFibonacciTime());
        btnIterativeUsingBigInteger.setOnClickListener(view -> IterativeUsingBigIntegerFibonacciTime());
        btnBigIntegerFibonacci.setOnClickListener(view -> BigIntegerFibonacciTime());
        btnRecursiveUsingBigIntegerAndPrimitiveFibonacci.setOnClickListener(view -> RecursiveUsingBigIntegerAndPrimitiveFibonacciTime());
        btnRecursiveUsingBigIntegerAndTableFibonacci.setOnClickListener(view -> RecursiveUsingBigIntegerAndTableFibonacciTime());
        btnCachedFibonacci.setOnClickListener(view -> CachedFibonacciTime());
    }

    private int getInputNumber() {
        try {
            return Integer.parseInt(inputN.getText().toString().trim());
        } catch (NumberFormatException e) {
            inputN.setError("Please enter a valid integer.");
            return -1;
        }
    }

    private void RecursiveWithLoopFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long startTime = System.nanoTime();
        computeRecursivelyWithLoop(n);
        ElapsedTime = System.nanoTime() - startTime;

        RecursiveWithLoopfibResult.setText("Time of Recursive with: " + ElapsedTime + " ns");
    }

    private void IterativeFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long startTime = System.nanoTime();
        computeIterativelyFaster(n);
        ElapsedTime = System.nanoTime() - startTime;

        IterativeFibResult.setText("Time of Iterative: " + ElapsedTime + " ns");
    }

    private void IterativeUsingBigIntegerFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long startTime = System.nanoTime();
        computeIterativelyFasterUsingBigInteger(n);
        ElapsedTime = System.nanoTime() - startTime;

        IterativeUsingBigIntegerFibResult.setText("Time of Iterative using BigInteger: " + ElapsedTime + " ns");
    }

    private void BigIntegerFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long Allocations = 0;
        long startTime = System.nanoTime();
        BigInteger result = computeRecursivelyFasterUsingBigInteger(n);
        ElapsedTime = System.nanoTime() - startTime;
        Allocations = computeRecursivelyFasterUsingBigIntegerAllocations(n);

        bigIntegerResult.setText("Time of using BigInteger: " + ElapsedTime + " ns");
        bigIntegerAllocations.setText("Total number of BigInteger allocations: " + Allocations);
    }

    private void RecursiveUsingBigIntegerAndPrimitiveFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long startTime = System.nanoTime();
        computeRecursivelyFasterUsingBigIntegerAndPrimitive(n);
        ElapsedTime = System.nanoTime() - startTime;

        RecursiveUsingBigIntegerAndPrimitiveFibResult.setText("Time of Recursive Using BigInteger And Primitive: " + ElapsedTime + " ns");
    }

    private void RecursiveUsingBigIntegerAndTableFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long startTime = System.nanoTime();
        computeRecursivelyFasterUsingBigIntegerAndTable(n);
        ElapsedTime = System.nanoTime() - startTime;

        RecursiveUsingBigIntegerAndTableFibResult.setText("Time of Recursive Using BigInteger And Table: " + ElapsedTime + " ns");
    }

    private void CachedFibonacciTime() {
        int n = getInputNumber();
        if (n < 0) return;  // Don't run if input is invalid

        long ElapsedTime = 0;
        long startTime = System.nanoTime();
        BigInteger result = computeRecursivelyWithCache(n);  // Use cached version
        ElapsedTime = System.nanoTime() - startTime;

        cachedFibResult.setText("Time of using Cache: " + ElapsedTime + " ns");
    }

    // Listing 1-3
    public static long computeRecursivelyWithLoop(int n) {
        if (n > 1) {
            long result = 1;
            do {
                result += computeRecursivelyWithLoop(n - 2);
                n--;
            } while (n > 1);
            return result;
        }
        return n;
    }

    // Listing 1-5
    public static long computeIterativelyFaster(int n) {
        if (n > 1) {
            long a, b = 1;
            n--;
            a = n & 1;
            n /= 2;
            while (n-- > 0) {
                a += b;
                b += a;
            }
            return b;
        }
        return n;
    }

    // Listing 1-6
    public static BigInteger computeIterativelyFasterUsingBigInteger(int n) {
        if (n > 1) {
            BigInteger a, b = BigInteger.ONE;
            n--;
            a = BigInteger.valueOf(n & 1);
            n /= 2;
            while (n-- > 0) {
                a = a.add(b);
                b = b.add(a);
            }
            return b;
        }
        return (n == 0) ? BigInteger.ZERO : BigInteger.ONE;
    }

    // Listing 1-7
    public static BigInteger computeRecursivelyFasterUsingBigInteger(int n) {
        if (n > 1) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyFasterUsingBigInteger(m);
            BigInteger fM_1 = computeRecursivelyFasterUsingBigInteger(m - 1);
            if ((n & 1) == 1) {
                // F(m)^2 + F(m-1)^2
                return fM.pow(2).add(fM_1.pow(2)); // three BigInteger objects created
            } else {
                // (2*F(m-1) + F(m)) * F(m)
                return fM_1.shiftLeft(1).add(fM).multiply(fM); // three BigInteger objects created
            }
        }
        return (n == 0) ? BigInteger.ZERO : BigInteger.ONE; // no BigInteger object created
    }

    public static long computeRecursivelyFasterUsingBigIntegerAllocations(int n) {
        long allocations = 0;
        if (n > 1) {
            int m = (n / 2) + (n & 1);
            allocations += computeRecursivelyFasterUsingBigIntegerAllocations(m);
            allocations += computeRecursivelyFasterUsingBigIntegerAllocations(m - 1);
            // 3 more BigInteger objects allocated
            allocations += 3;
        }
        return allocations;
    }

    // Listing 1-8
    public static BigInteger computeRecursivelyFasterUsingBigIntegerAndPrimitive(int n) {
        if (n > 92) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyFasterUsingBigIntegerAndPrimitive(m);
            BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndPrimitive(m - 1);
            if ((n & 1) == 1) {
                return fM.pow(2).add(fM_1.pow(2));
            } else {
                return fM_1.shiftLeft(1).add(fM).multiply(fM);
            }
        }
        return BigInteger.valueOf(computeIterativelyFaster(n));
    }

    // Listing 1-9
    public static BigInteger computeRecursivelyFasterUsingBigIntegerAndTable(int n) {
        if (n > PRECOMPUTED_SIZE - 1) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyFasterUsingBigIntegerAndTable(m);
            BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndTable(m - 1);
            if ((n & 1) == 1) {
                return fM.pow(2).add(fM_1.pow(2));
            } else {
                return fM_1.shiftLeft(1).add(fM).multiply(fM);
            }
        }
        return PRECOMPUTED[n];
    }

    // Listing 1-11
    public static BigInteger computeRecursivelyWithCache(int n) {
        SparseArray<BigInteger> cache = new SparseArray<>();
        StrictMode.noteSlowCall("test");
        return computeRecursivelyWithCache(n, cache);
    }

    private static BigInteger computeRecursivelyWithCache (int n, SparseArray<BigInteger> cache) {
        if (n > 92) {
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
        return BigInteger.valueOf(computeIterativelyFaster(n));
    }
}

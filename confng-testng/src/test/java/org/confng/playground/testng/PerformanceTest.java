package org.confng.playground.testng;

/**
 * Performance Test demonstrating configuration access performance with TestNG.
 *
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */

import org.confng.ConfNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

@Test(groups = {"performance"})
public class PerformanceTest {

    @BeforeClass
    public void setupPerformanceConfig() {
        System.setProperty("performance.testKey", "fast-access-value");
        System.setProperty("performance.iterations", "5000");
        System.setProperty("performance.thresholdMs", "50");
    }

    @Test
    public void testSingleConfigurationAccessPerformance() {
        long startTime = System.nanoTime();

        String value = ConfNG.get(PerfConfig.PERF_TEST_KEY);

        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        long durationMs = TimeUnit.NANOSECONDS.toMillis(durationNs);

        assertNotNull(value);
        assertEquals(value, "fast-access-value");

        // Single access should be very fast (under 7ms)
        assertTrue(durationMs < 10,
                "Single configuration access took too long: " + durationMs + "ms");
    }

    @Test
    public void testMultipleConfigurationAccessPerformance() {
        Integer iterations = ConfNG.getInt(PerfConfig.PERF_ITERATIONS);
        Integer thresholdMs = ConfNG.getInt(PerfConfig.PERF_THRESHOLD_MS);

        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            ConfNG.get(PerfConfig.PERF_TEST_KEY);
            ConfNG.getInt(PerfConfig.PERF_ITERATIONS);
            ConfNG.getInt(PerfConfig.PERF_THRESHOLD_MS);
        }

        long endTime = System.nanoTime();
        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

        System.out.printf("Completed %d iterations in %d ms (%.2f ops/ms)%n",
                iterations * 3, durationMs, (iterations * 3.0) / durationMs);

        assertTrue(durationMs < thresholdMs,
                String.format("Performance test exceeded threshold: %d ms > %d ms",
                        durationMs, thresholdMs));
    }

    @Test(invocationCount = 100, threadPoolSize = 10)
    public void testConcurrentPerformance() {
        long startTime = System.nanoTime();

        String value = ConfNG.get(PerfConfig.PERF_TEST_KEY);
        Integer iterations = ConfNG.getInt(PerfConfig.PERF_ITERATIONS);

        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;

        assertNotNull(value);
        assertNotNull(iterations);

        // Even under concurrent load, should be fast (under 5ms)
        assertTrue(TimeUnit.NANOSECONDS.toMillis(durationNs) < 5,
                "Concurrent access took too long: " + TimeUnit.NANOSECONDS.toMillis(durationNs) + "ms");
    }

    @Test
    public void testMemoryUsageStability() {
        Runtime runtime = Runtime.getRuntime();

        // Force garbage collection
        System.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Perform many configuration accesses
        for (int i = 0; i < 10000; i++) {
            ConfNG.get(PerfConfig.PERF_TEST_KEY);
        }

        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();

        long memoryIncrease = finalMemory - initialMemory;

        System.out.printf("Memory usage - Initial: %d bytes, Final: %d bytes, Increase: %d bytes%n",
                initialMemory, finalMemory, memoryIncrease);

        // Memory increase should be minimal (less than 1MB)
        assertTrue(memoryIncrease < 1024 * 1024,
                "Memory usage increased too much: " + memoryIncrease + " bytes");
    }

    @AfterClass
    public void cleanupPerformanceConfig() {
        System.clearProperty("performance.testKey");
        System.clearProperty("performance.iterations");
        System.clearProperty("performance.thresholdMs");
    }
}

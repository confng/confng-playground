package org.confng.playground.testng;

/**
 * Parallel Execution Test demonstrating thread-safe configuration access with TestNG.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.*;
import org.confng.playground.testng.ParallelConfig;
import java.util.concurrent.atomic.AtomicInteger;
import static org.testng.Assert.*;

@Test(groups = {"parallel-execution"})
public class ParallelExecutionTest {

    private static final AtomicInteger executionCounter = new AtomicInteger(0);

    @BeforeClass
    public void setupParallelConfig() {
        System.setProperty("parallel.threadCount", "8");
        System.setProperty("parallel.batchSize", "200");
        System.setProperty("parallel.timeoutMs", "10000");
        executionCounter.set(0);
    }

    @Test(invocationCount = 10, threadPoolSize = 5)
    public void testConcurrentConfigurationAccess() {
        String threadName = Thread.currentThread().getName();
        int executionNumber = executionCounter.incrementAndGet();
        
        Integer threadCount = ConfNG.getInt(ParallelConfig.THREAD_COUNT);
        Integer batchSize = ConfNG.getInt(ParallelConfig.BATCH_SIZE);
        Integer timeout = ConfNG.getInt(ParallelConfig.TIMEOUT_MS);
        
        assertEquals(threadCount, Integer.valueOf(8));
        assertEquals(batchSize, Integer.valueOf(200));
        assertEquals(timeout, Integer.valueOf(10000));
        
        System.out.printf("Execution %d on thread %s: threadCount=%d, batchSize=%d, timeout=%d%n",
            executionNumber, threadName, threadCount, batchSize, timeout);
    }

    @Test(invocationCount = 20, threadPoolSize = 10, timeOut = 15000)
    public void testHighConcurrencyAccess() {
        String threadName = Thread.currentThread().getName();
        
        // Simulate some processing time
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Integer threadCount = ConfNG.getInt(ParallelConfig.THREAD_COUNT);
        assertNotNull(threadCount);
        assertTrue(threadCount > 0);
        
        System.out.printf("High concurrency test on thread %s: threadCount=%d%n",
            threadName, threadCount);
    }

    @Test(dependsOnMethods = {"testConcurrentConfigurationAccess", "testHighConcurrencyAccess"})
    public void testExecutionCountValidation() {
        int totalExecutions = executionCounter.get();
        assertTrue(totalExecutions >= 10, "Should have at least 10 executions");
        System.out.printf("Total executions completed: %d%n", totalExecutions);
    }

    @AfterClass
    public void cleanupParallelConfig() {
        System.clearProperty("parallel.threadCount");
        System.clearProperty("parallel.batchSize");
        System.clearProperty("parallel.timeoutMs");
    }
}

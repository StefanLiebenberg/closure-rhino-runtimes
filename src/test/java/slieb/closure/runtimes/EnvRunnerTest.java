package slieb.closure.runtimes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EnvRunnerTest {

    private EnvRunner runner;

    private static final String TEST_FUNCTIONS_PATH =
            "/slieb/closure/runtimes/scripts/EnvRunnerTestFunctions.js";

    @Before
    public void setUp() throws Exception {
        runner = new EnvRunner();
        runner.initialize();
        runner.evaluateResource(TEST_FUNCTIONS_PATH);
    }

    @After
    public void tearDown() throws Exception {
        runner.close();
        runner = null;
    }

    @Test
    public void testInitialize() throws Exception {
        assertTrue(runner.getBoolean("window != null"));
        assertTrue(runner.getBoolean("document != null"));
        assertTrue(runner.getBoolean("console != null"));
    }


    @Test
    public void testDoLoad() throws Exception {
        runner.evaluateString("window.onload = callableFunction;");
        assertFalse(runner.getBoolean("callableFunctionCalled"));
        assertTrue(runner.getBoolean("callableFunctionCount == 0"));
        runner.doLoad();
        assertTrue(runner.getBoolean("callableFunctionCalled"));
        assertTrue(runner.getBoolean("callableFunctionCount == 1"));
    }

    @Test
    public void testDoWait() throws Exception {
        runner.doLoad();
        runner.evaluateString("window.setTimeout(callableFunction, 1000);");
        assertFalse(runner.getBoolean("callableFunctionCalled"));
        runner.doWait();
        assertTrue(runner.getBoolean("callableFunctionCalled"));
        assertTrue(runner.getBoolean("callableFunctionCount == 1"));
    }
}

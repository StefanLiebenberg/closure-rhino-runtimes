package slieb.closure.runtimes;

import com.google.javascript.rhino.head.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractRunnerTest {

    RunnerInterface runner;

    @Before
    public void setUp() throws Exception {
        runner = new AbstractRunner() {
        };
        runner.initialize();
    }

    @After
    public void tearDown() throws Exception {
        runner.close();
    }

    @Test
    public void testGetStringFromVar() throws Exception {
        runner.evaluateString("var object = 'One';");
        assertEquals("One", runner.getString("object"));
    }

    @Test
    public void testGetStringFromObjectProperty() throws Exception {
        runner.evaluateString("var object = {}");
        runner.evaluateString("object['attr'] = 'value';");
        assertEquals("value", runner.getString("object.attr"));
    }

    @Test(expected = ClassCastException.class)
    public void testGetStringFromIntVar() throws Exception {
        runner.evaluateString("var object = 1;");
        runner.getString("object");
    }

    @Test(expected = ClassCastException.class)
    public void testGetStringFromObjectVar() throws Exception {
        runner.evaluateString("var object = {};");
        runner.getString("object");
    }

    @Test
    public void testGetBoolean() throws Exception {
        runner.evaluateString("var object = true");
        assertTrue(runner.getBoolean("object"));

        runner.evaluateString("object = false;");
        Assert.assertFalse(runner.getBoolean("object"));
    }

    @Test(expected = ClassCastException.class)
    public void testGetBooleanException() throws Exception {
        runner.evaluateString("var object = 'red';");
        runner.getBoolean("object");
    }

    @Test
    public void testGetNumber() throws Exception {
        runner.evaluateString("var object = 1;");
        assertEquals(
                Double.valueOf(1),
                runner.getNumber("object"));
        runner.evaluateString("var object = 2;");
        assertEquals(
                Double.valueOf(2),
                runner.getNumber("object"));
        runner.evaluateString("var object = 3;");
        assertEquals(
                Double.valueOf(3),
                runner.getNumber("object"));
        runner.evaluateString("var object = 4;");
        assertEquals(
                Double.valueOf(4),
                runner.getNumber("object"));
        runner.evaluateString("object++;");
        assertEquals(
                Double.valueOf(5),
                runner.getNumber("object"));
    }

    @Test
    public void testGetObject() throws Exception {
        runner.evaluateString("var object = {}");
        runner.evaluateString("object.attr1 = 'one';");
        runner.evaluateString("object.attr2 = 2;");
        runner.evaluateString("object.attr3 = true;");
        Scriptable result = runner.getScriptable("object");
        assertNotNull(result);
        assertEquals("one",
                runner.getScriptableProperty(result, "attr1"));
        assertEquals(Double.valueOf(2),
                runner.getScriptableProperty(result, "attr2"));
        assertEquals(true,
                runner.getScriptableProperty(result, "attr3"));

    }

    @Test
    public void testGetFunction() throws Exception {
        runner.evaluateString("var method = function () {};");
        Function method = runner.getFunction("method");
        assertNotNull(method);

        runner.evaluateString("var invalid = null");
        Function invalidMethod = runner.getFunction("invalid");
        Assert.assertNull(invalidMethod);
    }

    @Test
    public void testCallFunction() throws Exception {
        runner.evaluateString("var value = null;");
        runner.evaluateString("var setMethod = function(x) { value = x };");
        Object setResult = runner.call("setMethod", null, true);
        assertEquals(Undefined.instance, setResult);
        assertTrue(runner.getBoolean("value"));

        runner.evaluateString("var getMethod = function() { return value; };");
        Boolean getResult = (Boolean) runner.call("getMethod", null);
        assertTrue(getResult
        );
    }

    @Test(expected = JavaScriptException.class)
    public void testFunctionThrowError() throws Exception {
        runner.evaluateString("var method = " +
                "function() { throw new Error('msg'); };");
        runner.call("method", null);
    }

    @Test(expected = EvaluatorException.class)
    public void testSyntaxException() throws Exception {
        runner.evaluateString("var x = var y;");
    }


}

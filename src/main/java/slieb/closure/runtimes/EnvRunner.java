package slieb.closure.runtimes;


import java.io.IOException;

/**
 * Environment Runner. Powered by a envjs javascript file.
 */
public class EnvRunner extends AbstractRunner {

    private static final String ENV_RHINO_PATH = "/slieb/closure/runtimes/scripts/env.rhino.js";

    private static final String RHINO_LOAD_PATH = "/slieb/closure/runtimes//scripts/load.rhino.js";

    private void evaluateResourceQuietly(final String resourcePath) {
        try {
            evaluateResource(resourcePath);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public void initialize() {
        super.initialize();
        evaluateResourceQuietly(ENV_RHINO_PATH);
    }


    public void doLoad() {
        evaluateResourceQuietly(RHINO_LOAD_PATH);
        doWait();
    }

    private static final String WAIT_COMMAND = "Envjs.wait()";

    public void doWait() {
        evaluateString(WAIT_COMMAND);
    }

    @Override
    public void close() {
        doWait();
        super.close();
    }


}

import org.apache.maven.shared.invoker.InvocationOutputHandler;

/**
 * Handler of output coming from maven.
 */
class StringBuilderOutputHandler implements InvocationOutputHandler {
    private StringBuilder builder = new StringBuilder();

    @Override
    public void consumeLine(String s) {
        builder.append(s + "\n");
    }

    public String toString() {
        return builder.toString();
    }
}

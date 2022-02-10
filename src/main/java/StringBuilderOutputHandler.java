import org.apache.maven.shared.invoker.InvocationOutputHandler;

/**
 * Handler of output coming from maven.
 */
class StringBuilderOutputHandler implements InvocationOutputHandler {
    private StringBuilder builder = new StringBuilder();

    /**
     * Consumes a line by adding it to the builder.
     * @param s line to add.
     */
    @Override
    public void consumeLine(String s) {
        builder.append(s + "\n");
    }

    /**
     * Get the built string.
     * @return the built string.
     */
    public String toString() {
        return builder.toString();
    }
}

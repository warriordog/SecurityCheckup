package net.acomputerdog.securitycheckup.test.step.log;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

/**
 * Step that records the result of a previous step as a test message with a formatted string.
 *
 * The formatMessage should have exactly one string argument (%s) that will be replaced with the
 * string value of the data returned from the passthrough step.
 */
public class AddDataMessageStep<T> extends PassthroughStep<T> {
    private final String formatMessage;

    public AddDataMessageStep(Step<T> passthrough, String formatMessage) {
        super(passthrough);
        this.formatMessage = formatMessage;
    }

    public String getFormatMessage() {
        return formatMessage;
    }

    @Override
    public T run(TestEnvironment environment) {
        T data = super.run(environment);

        environment.getTestMessages().add(String.format(formatMessage, String.valueOf(data)));

        return data;
    }
}

package net.acomputerdog.securitycheckup.test.step.log;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.test.step.data.PushStep;

public class AddEnvironmentMessageStep<T> extends PassthroughStep<T> {
    private final String formatMessage;
    private final Object dataKey;

    public AddEnvironmentMessageStep(String formatMessage, Object dataKey) {
        this(new PushStep<>(null), formatMessage, dataKey);
    }

    public AddEnvironmentMessageStep(Step<T> passthrough, String formatMessage, Object dataKey) {
        super(passthrough);
        this.formatMessage = formatMessage;
        this.dataKey = dataKey;
    }

    public String getFormatMessage() {
        return formatMessage;
    }

    public Object getDataKey() {
        return dataKey;
    }

    @Override
    public T run(TestEnvironment environment) {
        Object data = environment.getSharedResource(dataKey);
        environment.getTestMessages().add(String.format(formatMessage, String.valueOf(data)));

        return super.run(environment);
    }
}

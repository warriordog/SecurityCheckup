package net.acomputerdog.securitycheckup.test.step.log;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.test.step.data.PushStep;
import net.acomputerdog.securitycheckup.util.gson.GenericWrapped;

public class AddEnvironmentMessageStep<T> extends PassthroughStep<T> {
    private final String formatMessage;
    private final GenericWrapped<Object> dataKey;

    public AddEnvironmentMessageStep(String formatMessage, GenericWrapped<Object> dataKey) {
        this(new PushStep<>((T)null), formatMessage, dataKey);
    }

    public AddEnvironmentMessageStep(Step<T> passthrough, String formatMessage, GenericWrapped<Object> dataKey) {
        super(passthrough);
        this.formatMessage = formatMessage;
        this.dataKey = dataKey;
    }

    public String getFormatMessage() {
        return formatMessage;
    }

    public GenericWrapped<Object> getDataKey() {
        return dataKey;
    }

    @Override
    public T run(TestEnvironment environment) {
        Object data = environment.getSharedResource(dataKey.getValue());
        environment.getTestMessages().add(String.format(formatMessage, String.valueOf(data)));

        return super.run(environment);
    }
}

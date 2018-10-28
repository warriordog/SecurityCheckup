package net.acomputerdog.securitycheckup.test.step.log;

import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.PassthroughStep;
import net.acomputerdog.securitycheckup.test.step.Step;

public class AddMessageStep<T> extends PassthroughStep<T> {
    private final String message;

    public AddMessageStep(Step<T> passthrough, String message) {
        super(passthrough);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public T run(TestEnvironment environment) {
        environment.getTestMessages().add(message);

        return super.run(environment);
    }
}

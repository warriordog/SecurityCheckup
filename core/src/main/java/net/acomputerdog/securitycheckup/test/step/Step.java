package net.acomputerdog.securitycheckup.test.step;

import net.acomputerdog.securitycheckup.test.TestEnvironment;

import java.util.Collections;
import java.util.List;

public abstract class Step<T> {

    public abstract T run(TestEnvironment environment);

    public List<Step> getSubsteps() {
        return Collections.emptyList();
    }
}

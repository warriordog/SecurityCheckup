package net.acomputerdog.securitycheckup.test.step;

import net.acomputerdog.securitycheckup.test.TestEnvironment;

public interface Step<T> {

    T run(TestEnvironment environment);

}

package net.acomputerdog.securitycheckup.test.types.reg;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

public class RegKeyTestEmpty extends RegKeyTest {
    /**
     * If true, the registry key and hive must exist for the test to pass.
     * The still must be empty.
     */
    private boolean requireExists = false;

    public RegKeyTestEmpty(String id, String name, String description, WinReg.HKEY hive, String key) {
        super(id, name, description, hive, key);
    }

    public RegKeyTestEmpty(String id, String name, String description, String hive, String key) {
        super(id, name, description, hive, key);
    }

    public boolean isRequireExists() {
        return requireExists;
    }

    public RegKeyTestEmpty setRequireExists(boolean requireExists) {
        this.requireExists = requireExists;

        return this;
    }

    @Override
    protected TestResult runTestSafe(TestEnvironment environment) {
        if (Advapi32Util.registryKeyExists(getHive(), getKey())) {
            if (Advapi32Util.registryGetValues(getHive(), getKey()).isEmpty()) {
                setState(State.FINISHED);
                return new TestResult(this, TestResult.SCORE_PASS).setMessage("Registry key is empty.");
            } else {
                setState(State.FINISHED);
                return new TestResult(this, TestResult.SCORE_FAIL).setMessage("Registry key is not empty.");
            }
        } else if (!requireExists) {
            setState(State.FINISHED);
            return new TestResult(this, TestResult.SCORE_PASS).setMessage("Required registry key was not found.");
        } else {
            setState(State.INCOMPATIBLE);
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("Required registry key is missing.");
        }
    }
}

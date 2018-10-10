package net.acomputerdog.securitycheckup.test.types.reg;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.ex.reg.RegistryException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryKeyMissingException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryValueMissingException;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;
import net.acomputerdog.securitycheckup.util.RegUtil;

/**
 * A test against a registry value
 */
public abstract class RegEntryTest extends RegKeyTest {
    /**
     * Registry key value to access
     */
    private final String value;

    /**
     * If true, a missing registry key or value will be recorded as INCOMPATIBLE.
     * If false, a missing key or value is recorded as NOT_APPLICABLE and passes.
     */
    boolean requireAllKeys = true;

    /**
     * If true, a missing key or value fails.
     * If fase, a missing key or value is incompatible.
     */
    boolean failOnMissingKey = false;

    public RegEntryTest(String id, String name, String description, WinReg.HKEY hive, String key, String value) {
        super(id, name, description, hive, key);
        this.value = value;
    }

    public RegEntryTest(String id, String name, String description, String hive, String key, String value) {
        super(id, name, description, hive, key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isRequireAllKeys() {
        return requireAllKeys;
    }

    public RegEntryTest setRequireAllKeys(boolean requireAllKeys) {
        this.requireAllKeys = requireAllKeys;

        return this;
    }

    public boolean isFailOnMissingKey() {
        return failOnMissingKey;
    }

    public RegEntryTest setFailOnMissingKey(boolean failOnMissingKey) {
        this.failOnMissingKey = failOnMissingKey;
        return this;
    }

    @Override
    protected TestResult runTestSafe(TestEnvironment environment) {
        try {
            Object contents = RegUtil.readReg(getHive(), getKey(), value);

            this.setState(State.FINISHED);
            return finishTest(environment, contents);
        } catch (RegistryKeyMissingException | RegistryValueMissingException e) {
            if (requireAllKeys) {
                if (failOnMissingKey) {
                    this.setState(State.FINISHED);
                    return new TestResult(this).setScore(TestResult.SCORE_FAIL).setMessage("Required registry key or value is missing.");
                } else {
                    this.setState(State.INCOMPATIBLE);
                    return new TestResult(this).setScore(TestResult.SCORE_FAIL).setMessage("Required registry key or value is missing.");
                }
            } else {
                this.setState(State.NOT_APPLICABLE);
                return new TestResult(this).setScore(TestResult.SCORE_PASS).setMessage("Optional key or value is missing.");
            }
        } catch (RegistryException e) {
            this.setState(State.ERROR);
            return new TestResult(this).setScore(TestResult.SCORE_FAIL).setMessage("Unknown registry error: " + e.toString());
        }
    }

    /**
     * Implemented by subclasses to finish the test by checking the registry value.
     *
     * @param env Test environment
     * @param regContents Contents of the requested registry key
     * @return return the test results
     */
    protected abstract TestResult finishTest(TestEnvironment env, Object regContents);
}

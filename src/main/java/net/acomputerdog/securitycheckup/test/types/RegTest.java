package net.acomputerdog.securitycheckup.test.types;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.ex.reg.RegistryException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryKeyMissingException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryValueMissingException;
import net.acomputerdog.securitycheckup.test.BasicTest;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;
import net.acomputerdog.securitycheckup.util.RegUtil;

/**
 * A test against a registry value
 */
public abstract class RegTest extends BasicTest {
    /**
     * Registry hive to access
     */
    private final WinReg.HKEY hive;

    /**
     * Registry key to read
     */
    private final String key;

    /**
     * Registry key value to access
     */
    private final String value;

    public RegTest(String id, String name, String description, WinReg.HKEY hive, String key, String value) {
        super(id, name, description);
        this.hive = hive;
        this.key = key;
        this.value = value;
    }

    public RegTest(String id, String name, WinReg.HKEY hive, String key, String value) {
        super(id, name);
        this.hive = hive;
        this.key = key;
        this.value = value;
    }

    public RegTest(String id, WinReg.HKEY hive, String key, String value) {
        super(id);
        this.hive = hive;
        this.key = key;
        this.value = value;
    }

    public WinReg.HKEY getHive() {
        return hive;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    protected TestResult runTestSafe(TestEnvironment environment) {
        try {
            Object contents = RegUtil.readReg(hive, key, value);

            this.setState(State.FINISHED);
            return finishTest(environment, contents);
        } catch (RegistryKeyMissingException e) {
            this.setState(State.INCOMPATIBLE);
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("Required registry key is missing.");
        } catch (RegistryValueMissingException e) {
            this.setState(State.INCOMPATIBLE);
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("Required registry value is missing.");
        } catch (RegistryException e) {
            this.setState(State.ERROR);
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("Unknown registry error: " + e.toString());
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

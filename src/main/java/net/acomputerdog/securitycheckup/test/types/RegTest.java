package net.acomputerdog.securitycheckup.test.types;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.ex.reg.RegistryException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryKeyMissingException;
import net.acomputerdog.securitycheckup.ex.reg.RegistryValueMissingException;
import net.acomputerdog.securitycheckup.test.BasicTest;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

/**
 * A test against a registry value
 */
public abstract class RegTest extends BasicTest {

    /**
     * Name of the "default" value within a key
     */
    public static final String DEFAULT_VALUE = null;

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

    public RegTest(String id, String name, String description, WinReg.HKEY hive, String key) {
        this(id, name, description, hive, key, DEFAULT_VALUE);
    }

    public RegTest(String id, String name, WinReg.HKEY hive, String key) {
        this(id, name, hive, key, DEFAULT_VALUE);
    }

    public RegTest(String id, WinReg.HKEY hive, String key) {
        this(id, hive, key, DEFAULT_VALUE);
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
            Object contents = this.readReg(hive, key, value);

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
     * Reads a value from the registry
     * @param hive      hive to access
     * @param key       key to access
     * @param value     value to access
     *
     * @return          return the contents of the registry key
     *
     * @throws RegistryKeyMissingException if the requested key is missing
     * @throws RegistryValueMissingException if the requested value was not found in the specified key
     */
    protected Object readReg(WinReg.HKEY hive, String key, String value) {
        if (Advapi32Util.registryKeyExists(hive, key)) {
            if (Advapi32Util.registryValueExists(hive, key, value)) {
                return Advapi32Util.registryGetValue(hive, key, value);
            } else {
                throw new RegistryValueMissingException("Required registry value is missing", hive, key, value);
            }
        } else {
            throw new RegistryKeyMissingException("Required registry key is missing.", hive, key);
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

package net.acomputerdog.securitycheckup.test.types.reg;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.TestResult;

/**
 * Tests if the value of a registry key matches an expected value
 */
public class RegTestMatch extends RegTest {
    private final Object expected;

    public RegTestMatch(String id, String name, String description, WinReg.HKEY hive, String key, String value, Object expected) {
        super(id, name, description, hive, key, value);
        this.expected = expected;
    }

    public RegTestMatch(String id, String name, WinReg.HKEY hive, String key, String value, Object expected) {
        super(id, name, hive, key, value);
        this.expected = expected;
    }

    public RegTestMatch(String id, WinReg.HKEY hive, String key, String value, Object expected) {
        super(id, hive, key, value);
        this.expected = expected;
    }

    @Override
    protected TestResult finishTest(TestEnvironment env, Object regContents) {
        if (expected.equals(regContents)) {
            return new TestResult(this, TestResult.SCORE_PASS).setMessage("Registry contents matched.");
        } else {
            return new TestResult(this, TestResult.SCORE_FAIL).setMessage("Registry contents did not match.");
        }
    }
}

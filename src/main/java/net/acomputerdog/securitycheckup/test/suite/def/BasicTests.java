package net.acomputerdog.securitycheckup.test.suite.def;

import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.suite.TestSuite;
import net.acomputerdog.securitycheckup.test.types.WMITestMulti;
import net.acomputerdog.securitycheckup.test.types.WMITestPropBoolean;

/**
 * Suite of tests to analyze basic system security.
 *
 * Currently includes the following tests:
 * * windows_defender_enabled   - Check if windows defender is enabled
 * * av_installed               - Check if a 3rd party (not defender) AV is installed
 */
public class BasicTests extends TestSuite {
    public BasicTests() {
        super("basic_tests", "Basic Tests", "Tests for basic system security");

        // Windows defender enabled
        addTest(new WMITestPropBoolean(
                "windows_defender_enabled",
                "Windows Defender Enabled",
                "Verifies that Windows Defender is enabled.",
                "ROOT\\Microsoft\\SecurityClient",
                "SELECT * FROM ProtectionTechnologyStatus",
                "Enabled",
                true
        ));

        // 3rd party AV installed
        addTest(new WMITestMulti(
                "av_installed",
                "Antivirus Software Installed",
                "Verifies that an antivirus product is installed.",
                "ROOT\\SecurityCenter2",
                "SELECT * FROM AntiVirusProduct",
                false // fail if no AV is found
            ) {

            // This is called for each registered AV product.
            // If we get one that is not windows defender, then we found an AV
            @Override
            public int testObj(WbemClassObject obj) {
                if (!"Windows Defender".equals(obj.getString("displayName"))) {
                    return PASS;
                } else {
                    return CONTINUE;
                }
            }
        });
    }
}

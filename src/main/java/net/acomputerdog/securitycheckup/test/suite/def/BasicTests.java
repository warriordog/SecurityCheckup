package net.acomputerdog.securitycheckup.test.suite.def;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.jwmi.wbem.WbemClassObject;
import net.acomputerdog.securitycheckup.test.suite.TestSuite;
import net.acomputerdog.securitycheckup.test.types.TestUnion;
import net.acomputerdog.securitycheckup.test.types.reg.RegEntryTestMatch;
import net.acomputerdog.securitycheckup.test.types.reg.RegKeyTestEmpty;
import net.acomputerdog.securitycheckup.test.types.wmi.WMITestMulti;
import net.acomputerdog.securitycheckup.test.types.wmi.WMITestPropBoolean;

/**
 * Suite of tests to analyze basic system security.
 *
 * Currently includes the following tests:
 * * windows_defender_enabled   - Check if windows defender is enabled
 * * av_installed               - Check if a 3rd party (not defender) AV is installed
 * * autoplay_disabled          - Check if AutoPlay is disabled for external media
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

        // Autoplay disabled
        addTest(new TestUnion(
                "autoplay_disabled",
                "AutoPlay Disabled",
                "Verifies that AutoPlay is disabled."
        ).addTest(
            new TestUnion.Subtest(
                new RegEntryTestMatch( // This is the newer, more important key
                    "autoplay_disabled_new",
                    "AutoPlay Disabled (new)",
                    "Verifies that AutoPlay is disabled with the newer registry key.",
                    WinReg.HKEY_CURRENT_USER,
                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\AutoplayHandlers",
                    "DisableAutoplay",
                    1
                ).setFailOnMissingKey(true) // Windows settings app deletes key when autoplay is turned on
            ).setScoringMode(TestUnion.ScoringMode.MUST_PASS)
        ).addTest(
            new TestUnion.Subtest(
                new RegEntryTestMatch( // TODO test if necessary
                    "autoplay_disabled_old_system",
                    "AutoPlay Disabled (old, system wide)",
                    "Verifies that AutoPlay is disabled system-wide with the older registry key.",
                    WinReg.HKEY_LOCAL_MACHINE,
                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer",
                    "NoDriveTypeAutoRun",
                    0x255
                ).setRequireAllKeys(false) // Probably optional on new systems
            ).setSkipNotApplicable(true).setScoringMode(TestUnion.ScoringMode.FILTER)
        ).addTest(
            new TestUnion.Subtest(
                    new RegEntryTestMatch( // TODO test if necessary
                    "autoplay_disabled_old_user",
                    "AutoPlay Disabled (old, user-specific)",
                    "Verifies that AutoPlay is disabled for this user with the older registry key.",
                    WinReg.HKEY_CURRENT_USER,
                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\Explorer",
                    "NoDriveTypeAutoRun",
                    0x255
                ).setRequireAllKeys(false) // Probably optional on new systems
            ).setSkipNotApplicable(true).setScoringMode(TestUnion.ScoringMode.FILTER)
        ));

        // Windows defender exclusions set
        addTest(new TestUnion(
                "defender_exclusions",
                "Windows Defender Exclusions",
                "Checks for windows defender exclusions"
            ).addTest(new TestUnion.Subtest(new RegKeyTestEmpty(
                    "defender_exclusions_extensions",
                    "Windows Defender Exclusions (Filename Extensions)",
                    "Checks for file extensions excluded from windows defender",
                    WinReg.HKEY_LOCAL_MACHINE,
                    "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Extensions"
                ).setRequireExists(true)
            ).setScoringMode(TestUnion.ScoringMode.MUST_PASS)
            ).addTest(new TestUnion.Subtest(new RegKeyTestEmpty(
                        "defender_exclusions_paths",
                        "Windows Defender Exclusions (Paths)",
                        "Checks for paths excluded from windows defender",
                        WinReg.HKEY_LOCAL_MACHINE,
                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Paths"
                ).setRequireExists(true)
            ).setScoringMode(TestUnion.ScoringMode.MUST_PASS)
            ).addTest(new TestUnion.Subtest(new RegKeyTestEmpty(
                        "defender_exclusions_processes",
                        "Windows Defender Exclusions (Process)",
                        "Checks for processes excluded from windows defender",
                        WinReg.HKEY_LOCAL_MACHINE,
                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Processes"
                ).setRequireExists(true)
            ).setScoringMode(TestUnion.ScoringMode.MUST_PASS)
            ).addTest(new TestUnion.Subtest(new RegKeyTestEmpty(
                        "defender_exclusions_temppaths",
                        "Windows Defender Exclusions (Temporary Paths)",
                        "Checks for temporary paths excluded from windows defender",
                        WinReg.HKEY_LOCAL_MACHINE,
                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\TemporaryPaths"
                ).setRequireExists(true)
            ).setScoringMode(TestUnion.ScoringMode.MUST_PASS)
        ));
    }
}

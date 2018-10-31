package net.acomputerdog.securitycheckup.profiles;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestInfo;
import net.acomputerdog.securitycheckup.test.comparison.EqualsComparison;
import net.acomputerdog.securitycheckup.test.comparison.WMIPropertyComparison;
import net.acomputerdog.securitycheckup.test.registry.TestRegistry;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.test.step.compare.CompareStep;
import net.acomputerdog.securitycheckup.test.step.compare.ConstCompareStep;
import net.acomputerdog.securitycheckup.test.step.compare.MatchAnyStep;
import net.acomputerdog.securitycheckup.test.step.data.InvertStep;
import net.acomputerdog.securitycheckup.test.step.data.PushStep;
import net.acomputerdog.securitycheckup.test.step.flow.AverageEveryStep;
import net.acomputerdog.securitycheckup.test.step.flow.PassAnyStep;
import net.acomputerdog.securitycheckup.test.step.flow.PassEveryStep;
import net.acomputerdog.securitycheckup.test.step.flow.RequireThenStep;
import net.acomputerdog.securitycheckup.test.step.log.AddDataMessageStep;
import net.acomputerdog.securitycheckup.test.step.password.UserPasswordIsEmptyStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegEntryExistsStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegKeyEmptyStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegKeyExistsStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegValueStep;
import net.acomputerdog.securitycheckup.test.step.score.BoolToScoreStep;
import net.acomputerdog.securitycheckup.test.step.score.FinalStep;
import net.acomputerdog.securitycheckup.test.step.wmi.ClsPropertyStep;
import net.acomputerdog.securitycheckup.test.step.wmi.GetFirstClsObjStep;
import net.acomputerdog.securitycheckup.test.step.wmi.WMIStep;

public class BasicTests extends Profile {

    public static final String ID_BASIC_TESTS = "basic_tests";

    public static final String ID_WIN_DEFENDER_ENABLED = "windows_defender_enabled";
    public static final String ID_AV_INSTALLED = "av_installed";
    public static final String ID_AUTOPLAY_DISABLED = "autoplay_disabled";
    public static final String ID_DEFENDER_EXCLUSIONS = "defender_exclusions";
    public static final String ID_UAC_ENABLED = "uac_enabled";
    public static final String ID_PASSWORD_SET = "password_set";

    public BasicTests(TestRegistry testRegistry) {
        super(ID_BASIC_TESTS, "Basic Tests", "Tests for basic system security.");

        addTest(getOrCreateWinDefenderEnabled(testRegistry));
        addTest(getOrCreateAVInstalled(testRegistry));
        addTest(getOrCreateAutoPlayDisabled(testRegistry));
        addTest(getOrCreateDefenderExclusions(testRegistry));
        addTest(getOrCreateUACEnabled(testRegistry));
        addTest(getOrCreatePasswordSet(testRegistry));
    }

    private static Test getOrCreateWinDefenderEnabled(TestRegistry testRegistry) {
        Test winDefenderEnabled = testRegistry.getTest(ID_WIN_DEFENDER_ENABLED);

        if (winDefenderEnabled == null) {
            TestInfo testInfo = new TestInfo(
                    ID_WIN_DEFENDER_ENABLED,
                    "Windows Defender Enabled",
                    "Verifies that Windows Defender is enabled.",
                    "Enable Windows Defender in the Settings app.");

            Step<Float> rootStep =
                    new RequireThenStep(
                            new AddDataMessageStep<>(
                                    new ConstCompareStep<>(
                                            new EqualsComparison<>(),
                                            new ClsPropertyStep<>(
                                                    new GetFirstClsObjStep(
                                                            new WMIStep(
                                                                    "ROOT\\Microsoft\\SecurityClient",
                                                                    "SELECT * FROM ProtectionTechnologyStatus"
                                                            )
                                                    ),
                                                    "Enabled"
                                            ),
                                            true
                                    ),
                                    "Protection technologies enabled: %s"
                            ),
                            new AverageEveryStep(
                                    new BoolToScoreStep(
                                            new AddDataMessageStep<>(
                                                    new ConstCompareStep<>(
                                                            new EqualsComparison<>(),
                                                            new RegValueStep<>(
                                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                                    "SOFTWARE\\Microsoft\\Windows Defender",
                                                                    "DisableAntiSpyware"
                                                            ),
                                                            0
                                                    ),
                                                    "Antispyware enabled: %s"
                                            )
                                    ),
                                    new BoolToScoreStep(
                                            new AddDataMessageStep<>(
                                                    new ConstCompareStep<>(
                                                            new EqualsComparison<>(),
                                                            new RegValueStep<>(
                                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                                    "SOFTWARE\\Microsoft\\Windows Defender",
                                                                    "DisableAntiVirus"
                                                            ),
                                                            0
                                                    ),
                                                    "Antivirus enabled: %s"
                                            )
                                    )
                            )
                    );
            winDefenderEnabled = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(winDefenderEnabled);
        }

        return winDefenderEnabled;
    }

    private static Test getOrCreateAVInstalled(TestRegistry testRegistry) {
        Test avInstalled = testRegistry.getTest(ID_AV_INSTALLED);

        if (avInstalled == null) {
            TestInfo testInfo = new TestInfo(
                    ID_AV_INSTALLED,
                    "Antivirus Software Installed",
                    "Verifies that an antivirus product is installed.",
                    "Install an antivirus application such as Avast, AVG, MalwareBytes, etc.");

            // TODO resource leak
            Step<Float> rootStep =
                    new BoolToScoreStep(
                            new PassEveryStep(
                                    new AddDataMessageStep<>(
                                            new MatchAnyStep<>(
                                                    new WMIStep(
                                                            "ROOT\\SecurityCenter2",
                                                            "SELECT * FROM AntiVirusProduct"
                                                    ),
                                                    new PushStep<>("Windows Defender"),
                                                    new WMIPropertyComparison<>(
                                                            new EqualsComparison<String>().setInverted(true),
                                                            "displayName"
                                                    )

                                            ),
                                            "Antivirus installed: %s"
                                    ),
                                    new RegKeyExistsStep(
                                            WinReg.HKEY_LOCAL_MACHINE,
                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\QualityCompat"
                                    ),
                                    new RegEntryExistsStep(
                                            WinReg.HKEY_LOCAL_MACHINE,
                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\QualityCompat",
                                            "cadca5fe-87d3-4b96-b7fb-a231484277cc"
                                    ),
                                    new ConstCompareStep<>(
                                            new EqualsComparison<>(),
                                            new RegValueStep<>(
                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\QualityCompat",
                                                    "cadca5fe-87d3-4b96-b7fb-a231484277cc"
                                            ),
                                            0
                                    )
                            )
                    );

            avInstalled = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(avInstalled);
        }

        return avInstalled;
    }

    private static Test getOrCreateAutoPlayDisabled(TestRegistry testRegistry) {
        Test autoPlayDisabled = testRegistry.getTest(ID_AUTOPLAY_DISABLED);

        if (autoPlayDisabled == null) {
            TestInfo testInfo = new TestInfo(
                    ID_AUTOPLAY_DISABLED,
                    "AutoPlay Disabled",
                    "Verifies that AutoPlay is disabled.",
                    "Disable AutoPlay in the Settings app.");

            Step<Float> rootStep =
                    new BoolToScoreStep(
                            new PassEveryStep(
                                    new RegEntryExistsStep(
                                            WinReg.HKEY_CURRENT_USER,
                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\AutoplayHandlers",
                                            "DisableAutoplay"
                                    ),
                                    new CompareStep<>(
                                            new EqualsComparison<>(),
                                            new RegValueStep<>(
                                                    WinReg.HKEY_CURRENT_USER,
                                                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\AutoplayHandlers",
                                                    "DisableAutoplay"
                                            ),
                                            new PushStep<>(1)
                                    )
                            )
                    );

            autoPlayDisabled = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(autoPlayDisabled);
        }

        return autoPlayDisabled;
    }

    private static Test getOrCreateDefenderExclusions(TestRegistry testRegistry) {
        Test defenderExclusions = testRegistry.getTest(ID_DEFENDER_EXCLUSIONS);

        if (defenderExclusions == null) {
            TestInfo testInfo = new TestInfo(
                    ID_DEFENDER_EXCLUSIONS,
                    "Windows Defender Exclusions",
                    "Checks for files and applications excluded from Windows Defender scans.",
                    "Remove any exclusions through the Windows Defender Security Console.");

        Step<Float> rootStep =
                new BoolToScoreStep(
                        new PassAnyStep(
                                new InvertStep(
                                    new AddDataMessageStep<>(
                                            new RegKeyExistsStep(
                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                    "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Extensions"
                                            ),
                                            "Exclusions registry key exists: %s"
                                    )
                                ),
                                new PassEveryStep(
                                        new AddDataMessageStep<>(
                                                new RegKeyEmptyStep(
                                                        WinReg.HKEY_LOCAL_MACHINE,
                                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Extensions"
                                                ),
                                                "Excluded extensions: %s"
                                        ),
                                        new AddDataMessageStep<>(
                                                new RegKeyEmptyStep(
                                                        WinReg.HKEY_LOCAL_MACHINE,
                                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Paths"
                                                ),
                                                "Excluded paths: %s"
                                        ),
                                        new AddDataMessageStep<>(
                                                new RegKeyEmptyStep(
                                                        WinReg.HKEY_LOCAL_MACHINE,
                                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Processes"
                                                ),
                                                "Excluded processes: %s"
                                        ),
                                        new AddDataMessageStep<>(
                                                new RegKeyEmptyStep(
                                                        WinReg.HKEY_LOCAL_MACHINE,
                                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\TemporaryPaths"
                                                ),
                                                "Excluded temporary paths: %s"
                                        )
                                )
                        )
                );

            defenderExclusions = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(defenderExclusions);
        }

        return defenderExclusions;
    }

    private static Test getOrCreateUACEnabled(TestRegistry testRegistry) {
        Test uacEnabled = testRegistry.getTest(ID_UAC_ENABLED);

        if (uacEnabled == null) {
            TestInfo testInfo = new TestInfo(
                    ID_UAC_ENABLED,
                    "UAC Enabled",
                    "Checks if User Account Control is enabled.",
                    "Enable User Account Control in the Control Panel.");

            Step<Float> rootStep = new BoolToScoreStep(
                    new PassEveryStep(
                            new AddDataMessageStep<>(
                                    new CompareStep<>(
                                            new EqualsComparison<>(),
                                            new RegValueStep<>(
                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\System",
                                                    "EnableLUA"
                                            ),
                                            new PushStep<>(1)
                                    ),
                                    "UAC enabled: %s"
                            ),
                            new AddDataMessageStep<>(
                                    new CompareStep<>(
                                            new EqualsComparison<>()
                                                    .setInverted(true),
                                            new RegValueStep<>(
                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                    "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\System",
                                                    "ConsentPromptBehaviorAdmin"
                                            ),
                                            new PushStep<>(0)
                                    ),
                                    "UAC prompt enabled: %s"
                            )
                    )

            );

            uacEnabled = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(uacEnabled);
        }

        return uacEnabled;
    }

    private static Test getOrCreatePasswordSet(TestRegistry testRegistry) {
        Test passwordSet = testRegistry.getTest(ID_PASSWORD_SET);

        if (passwordSet == null) {
            TestInfo testInfo = new TestInfo(
                    ID_PASSWORD_SET,
                    "Password Set",
                    "Verifies that a password is set on the current Windows account.",
                    "Set a Windows password in the Settings app or Control Panel.");

            Step<Float> rootStep =
                    new BoolToScoreStep(
                            new InvertStep(
                                    new UserPasswordIsEmptyStep()
                            )
                    );

            passwordSet = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(passwordSet);
        }

        return passwordSet;
    }

    public static Profile lookupOrRegister(TestRegistry testRegistry) {
        Profile basicTests = testRegistry.getProfile(ID_BASIC_TESTS);

        if (basicTests == null) {
            basicTests = new BasicTests(testRegistry);
            testRegistry.addProfile(basicTests);
        }

        return basicTests;
    }
}

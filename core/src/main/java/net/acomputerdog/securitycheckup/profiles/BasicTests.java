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
import net.acomputerdog.securitycheckup.test.step.flow.RequireThenStep;
import net.acomputerdog.securitycheckup.test.step.log.AddDataMessageStep;
import net.acomputerdog.securitycheckup.test.step.password.UserPasswordIsEmptyStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegDefValueStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegKeyEmptyStep;
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
    public static final String ID_DEFAULT_BROWSER_IE = "default_browser_ie";

    public BasicTests(TestRegistry testRegistry) {
        super(ID_BASIC_TESTS, "Basic Tests", "Tests for basic system security.");

        addTest(getOrCreateWinDefenderEnabled(testRegistry));
        addTest(getOrCreateAVInstalled(testRegistry));
        addTest(getOrCreateAutoPlayDisabled(testRegistry));
        addTest(getOrCreateDefenderExclusions(testRegistry));
        addTest(getOrCreateUACEnabled(testRegistry));
        addTest(getOrCreatePasswordSet(testRegistry));
        addTest(getOrCreateDefaultBrowserIE(testRegistry));
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
                                                            new RegDefValueStep<>(
                                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                                    "SOFTWARE\\Microsoft\\Windows Defender",
                                                                    "DisableAntiSpyware",
                                                                    0 // False (enabled) if the value is missing
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
                                                            new RegDefValueStep<>(
                                                                    WinReg.HKEY_LOCAL_MACHINE,
                                                                    "SOFTWARE\\Microsoft\\Windows Defender",
                                                                    "DisableAntiVirus",
                                                                    0 // False (enabled) if the value is missing
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
                    new RequireThenStep(
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
                            new BoolToScoreStep(
                                    new AddDataMessageStep<>(
                                            new ConstCompareStep<>(
                                                    new EqualsComparison<>(),
                                                    new RegDefValueStep<>(
                                                            WinReg.HKEY_LOCAL_MACHINE,
                                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\QualityCompat",
                                                            "cadca5fe-87d3-4b96-b7fb-a231484277cc",
                                                            1 // not compatible if not set
                                                    ),
                                                    0
                                            ),
                                            "Antivirus spectre-compatible: %s"
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
                            new CompareStep<>(
                                    new EqualsComparison<>(),
                                    new RegDefValueStep<>(
                                            WinReg.HKEY_CURRENT_USER,
                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\AutoplayHandlers",
                                            "DisableAutoplay",
                                            0 // enabled if not set
                                    ),
                                    new PushStep<>(1)
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
                new AverageEveryStep(
                        new BoolToScoreStep(
                                new AddDataMessageStep<>(
                                        new RegKeyEmptyStep(
                                                WinReg.HKEY_LOCAL_MACHINE,
                                                "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Extensions"
                                        ),
                                        "Excluded extensions: %s"
                                )
                        ),
                        new BoolToScoreStep(
                                new AddDataMessageStep<>(
                                        new RegKeyEmptyStep(
                                                WinReg.HKEY_LOCAL_MACHINE,
                                                "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Paths"
                                        ),
                                        "Excluded paths: %s"
                                )
                        ),
                        new BoolToScoreStep(
                                new AddDataMessageStep<>(
                                        new RegKeyEmptyStep(
                                                WinReg.HKEY_LOCAL_MACHINE,
                                                "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Processes"
                                        ),
                                        "Excluded processes: %s"
                                )
                        ),
                        new BoolToScoreStep(
                                new AddDataMessageStep<>(
                                        new RegKeyEmptyStep(
                                                WinReg.HKEY_LOCAL_MACHINE,
                                                "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\TemporaryPaths"
                                        ),
                                        "Excluded temporary paths: %s"
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

            Step<Float> rootStep =
                    new AverageEveryStep(
                            new BoolToScoreStep(
                                    new AddDataMessageStep<>(
                                            new CompareStep<>(
                                                    new EqualsComparison<>(),
                                                    new RegDefValueStep<>(
                                                            WinReg.HKEY_LOCAL_MACHINE,
                                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\System",
                                                            "EnableLUA",
                                                            0 // disabled if missing
                                                    ),
                                                    new PushStep<>(1)
                                            ),
                                            "UAC enabled: %s"
                                    )
                            ),
                            new BoolToScoreStep(
                                    new AddDataMessageStep<>(
                                            new CompareStep<>(
                                                    new EqualsComparison<>()
                                                            .setInverted(true),
                                                    new RegDefValueStep<>(
                                                            WinReg.HKEY_LOCAL_MACHINE,
                                                            "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\System",
                                                            "ConsentPromptBehaviorAdmin",
                                                            0 // no notification if missing
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

    private static Test getOrCreateDefaultBrowserIE(TestRegistry testRegistry) {
        Test defaultBrowserIE = testRegistry.getTest(ID_DEFAULT_BROWSER_IE);

        if (defaultBrowserIE == null) {
            TestInfo testInfo = new TestInfo(
                    ID_DEFAULT_BROWSER_IE,
                    "Default Web Browser",
                    "Makes sure that the default web browser is not Internet Explorer.",
                    "Change the default browser in the Settings app.");

            Step<Float> rootStep =
                    new AverageEveryStep(
                            new BoolToScoreStep(
                                    new AddDataMessageStep<>(
                                            new CompareStep<>(
                                                    new EqualsComparison<>()
                                                            .setInverted(true),
                                                    new RegDefValueStep<>(
                                                            WinReg.HKEY_CURRENT_USER,
                                                            "SOFTWARE\\Microsoft\\Windows\\Shell\\Associations\\URLAssociations\\http\\UserChoice",
                                                            "ProgId",
                                                            "" // empty string (pass) if missing
                                                    ),
                                                    new PushStep<>("IE.HTTP")
                                            ),
                                            "HTTP handler not IE: %s"
                                    )
                            ),
                            new BoolToScoreStep(
                                    new AddDataMessageStep<>(
                                            new CompareStep<>(
                                                    new EqualsComparison<>()
                                                            .setInverted(true),
                                                    new RegDefValueStep<>(
                                                            WinReg.HKEY_CURRENT_USER,
                                                            "SOFTWARE\\Microsoft\\Windows\\Shell\\Associations\\URLAssociations\\https\\UserChoice",
                                                            "ProgId",
                                                            "" // empty string (pass) if missing
                                                    ),
                                                    new PushStep<>("IE.HTTP")
                                            ),
                                            "HTTPS handler not IE: %s"
                                    )
                            )
                    );

            defaultBrowserIE = new Test(testInfo, new FinalStep(rootStep));
            testRegistry.addTest(defaultBrowserIE);
        }

        return defaultBrowserIE;
    }

    public static void addToProfile(TestRegistry testRegistry) {
        if (!testRegistry.hasProfile(ID_BASIC_TESTS)) {
            testRegistry.addProfile(new BasicTests(testRegistry));
        }
    }
}

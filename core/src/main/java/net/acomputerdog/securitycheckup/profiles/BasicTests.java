package net.acomputerdog.securitycheckup.profiles;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.Profile;
import net.acomputerdog.securitycheckup.test.Test;
import net.acomputerdog.securitycheckup.test.TestInfo;
import net.acomputerdog.securitycheckup.test.comparison.EqualsComparison;
import net.acomputerdog.securitycheckup.test.comparison.WMIPropertyComparison;
import net.acomputerdog.securitycheckup.test.step.Step;
import net.acomputerdog.securitycheckup.test.step.compare.CompareStep;
import net.acomputerdog.securitycheckup.test.step.compare.ConstCompareStep;
import net.acomputerdog.securitycheckup.test.step.compare.MatchAnyStep;
import net.acomputerdog.securitycheckup.test.step.data.PushStep;
import net.acomputerdog.securitycheckup.test.step.flow.PassEveryStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegEntryExistsStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegKeyEmptyStep;
import net.acomputerdog.securitycheckup.test.step.reg.RegValueStep;
import net.acomputerdog.securitycheckup.test.step.score.BoolToScoreStep;
import net.acomputerdog.securitycheckup.test.step.score.FinalStep;
import net.acomputerdog.securitycheckup.test.step.wmi.ClsPropertyStep;
import net.acomputerdog.securitycheckup.test.step.wmi.GetFirstClsObjStep;
import net.acomputerdog.securitycheckup.test.step.wmi.WMIStep;

public class BasicTests extends Profile {
    public BasicTests() {
        super(createInfo());

        addTest(createWinDefenderEnabled());
        addTest(createAVInstalled());
        addTest(createAutoPlayDisabled());
        addTest(createDefenderExclusions());
    }

    private static TestInfo createInfo() {
        return new TestInfo("basic_tests", "Basic Tests", "Tests for basic system security.");
    }

    private static Test createWinDefenderEnabled() {
        TestInfo testInfo = new TestInfo(
                "windows_defender_enabled",
                "Windows Defender Enabled",
                "Verifies that Windows Defender is enabled."
        );

        Step<Float> windowsDefenderEnabled =
                new BoolToScoreStep(
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
                        )
                );

        return new Test(testInfo, new FinalStep(windowsDefenderEnabled));
    }

    private static Test createAVInstalled() {
        TestInfo testInfo = new TestInfo(
                "av_installed",
                "Antivirus Software Installed",
                "Verifies that an antivirus product is installed."
        );

        Step<Float> avInstalled =
                new BoolToScoreStep(
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

                        )
                );

        return new Test(testInfo, new FinalStep(avInstalled));
    }

    private static Test createAutoPlayDisabled() {
        TestInfo testInfo = new TestInfo(
                "autoplay_disabled",
                "AutoPlay Disabled",
                "Verifies that AutoPlay is disabled."
        );

        Step<Float> autoPlayDisabled =
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

        return new Test(testInfo, new FinalStep(autoPlayDisabled));
    }

    private static Test createDefenderExclusions() {
        TestInfo testInfo = new TestInfo(
                "defender_exclusions",
                "Windows Defender Exclusions",
                "Checks for windows defender exclusions"
        );

        Step<Float> defenderExclusions =
                new BoolToScoreStep(
                        new PassEveryStep(
                                new RegKeyEmptyStep(
                                        WinReg.HKEY_LOCAL_MACHINE,
                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Extensions"
                                ),
                                new RegKeyEmptyStep(
                                        WinReg.HKEY_LOCAL_MACHINE,
                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Paths"
                                ),
                                new RegKeyEmptyStep(
                                        WinReg.HKEY_LOCAL_MACHINE,
                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\Processes"
                                ),
                                new RegKeyEmptyStep(
                                        WinReg.HKEY_LOCAL_MACHINE,
                                        "SOFTWARE\\Microsoft\\Windows Defender\\Exclusions\\TemporaryPaths"
                                )
                        )
                );

        return new Test(testInfo, new FinalStep(defenderExclusions));
    }
}

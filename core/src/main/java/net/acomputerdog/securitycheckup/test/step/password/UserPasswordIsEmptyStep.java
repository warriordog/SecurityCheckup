package net.acomputerdog.securitycheckup.test.step.password;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import net.acomputerdog.securitycheckup.test.TestEnvironment;
import net.acomputerdog.securitycheckup.test.step.Step;

public class UserPasswordIsEmptyStep implements Step<Boolean> {
    private static final int LOGON32_LOGON_NETWORK = 3;
    private static final int LOGON32_PROVIDER_DEFAULT = 0;

    @Override
    public Boolean run(TestEnvironment environment) {
        String username = Advapi32Util.getUserName();

        WinNT.HANDLEByReference handle = new WinNT.HANDLEByReference();
        boolean result = Advapi32.INSTANCE.LogonUser(username, ".", "", LOGON32_LOGON_NETWORK, LOGON32_PROVIDER_DEFAULT, handle);

        // login with a blank password can return a security error instead of success if a registry key is set
        if (!result) {
            int error = Native.getLastError();

            if (error == WinError.ERROR_ACCOUNT_RESTRICTION) {
                // password is blank but can't login to account with blank password
                return true;
            } else if (error == WinError.ERROR_LOGON_FAILURE) {
                // wrong password, so not blank
                return false;
            } else {
                // TODO record errors
                // some other error
                return false;
            }
        } else {
            // Make sure to close the handle, but only if login succeeded
            Kernel32.INSTANCE.CloseHandle(handle.getValue());

            // login succeeded, password is blank
            return true;
        }

    }
}

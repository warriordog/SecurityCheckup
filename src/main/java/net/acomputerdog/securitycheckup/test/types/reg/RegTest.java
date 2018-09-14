package net.acomputerdog.securitycheckup.test.types.reg;

import com.sun.jna.platform.win32.WinReg;
import net.acomputerdog.securitycheckup.test.types.BasicTest;
import net.acomputerdog.securitycheckup.util.RegUtil;

public abstract class RegTest extends BasicTest {
    /**
     * Registry hive to access
     */
    private final WinReg.HKEY hive;

    public RegTest(String id, String name, String description, WinReg.HKEY hive) {
        super(id, name, description);
        this.hive = hive;
    }

    public RegTest(String id, String name, String description, String hive) {
        this(id, name, description, RegUtil.getHiveByName(hive));
    }

    public WinReg.HKEY getHive() {
        return hive;
    }
}

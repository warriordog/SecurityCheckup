Security Checkup - a windows utility that checks for and recommends secure settings and policies in place on your PC
---

Security Checkup is a simple tool to analyze and rate a Windows system's use of security best practices.  It checks for key "security indicators" then generates a report with a calculated "security score" and a list of recommended changes to system settings or usage patterns.

This project is split up into three parts - a core engine that acts as a test bed to execute test scripts, a simple CLI, and a JavaFX-based GUI.

The core is a set of utilities for querying information from native windows systems (registry, WMI, etc) and a tree-based test framework.  Tests can be created programmatically or imported from JSON. 

The CLI runs a single profile or test and also acts a debugging tool to test the native features of the core.  It is depreciated, but still functional for now.

The GUI is a JavaFX-based interface that can create, run, and manage multiple profiles.  It comes with a set of profiles ready to use out of the box.

Currently, the following profiles are included in the default set:
* Basic Tests - Basic, mostly universal security.
  * Windows defender is enabled
  * No exclusions are set in windows defender
  * A 3rd party AV is installed
  * UAC is enabled
  * Windows SmartScreen is enabled
* Power Users - More comprehensive security aimed at experienced users.
  * AutoPlay is disabled
  * A password is required for login
  * Default browser is not Internet Explorer

The default tests are a work in progress, so keep an eye out for updates!

All modules of Security Checkup are written in pure java, however some 3rd party libraries include native code to interface to low-level Windows features.  The following libraries are currently used:
* [Java Native Access](https://github.com/java-native-access/jna)
* [jWMI](https://github.com/warriordog/jwmi)
* [Gson](https://github.com/google/gson)

Java 8, 9, and 10 are supported.  Make sure to install a 64 bit JVM and include Java FX (so no headless versions).

Please note that this software is being developed for class credit and may not be supported long-term.

Screenshots:

![Screenshot 1](doc/resources/doc/screenshots/ss1.png?raw=true)

![Screenshot 2](doc/resources/doc/screenshots/ss2.png?raw=true)

![Screenshot 3](doc/resources/doc/screenshots/ss3.png?raw=true)
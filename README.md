Security Checkup - a windows utility that checks for and recommends secure settings and policies in place on your PC
---

Security Checkup is a simple tool to analyze and rate a Windows system's use of security best practices.  It checks for key "security indicators" then generates a report with a calculated "security score" and a list of recommended changes to system settings or usage patterns.

Currently, the following indicators are checked:
* Windows defender is enabled
* No exclusions are set in windows defender
* A 3rd party AV is installed
* UAC is enabled
* AutoPlay is disabled
* A password is required for login

Security Checkup is written in pure java, however some 3rd party libraries include native code to interface to low-level Windows features.  The following libraries are currently used:
* [Java Native Access](https://github.com/java-native-access/jna)
* [jWMI](https://github.com/warriordog/jwmi)

Java 8, 9, and 10 are supported.  Make sure to install a 64 bit JVM and include Java FX (so no headless versions).

Please note that this software is being developed for class credit and may not be supported long-term.

Screenshots:

![Screenshot 1](doc/img/ss1.png?raw=true)

![Screenshot 2](doc/img/ss2.png?raw=true)
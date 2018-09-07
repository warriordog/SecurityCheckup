Security Checkup - a windows utility that checks for and recommends secure settings and policies in place on your PC
---

Security Checkup is a simple tool to analyze and rate a Windows system's use of security best practices.  It checks for key "security indicators" like the use of an Anti Virus product, AutoRun / AutoPlay being disabled, Ad Blockers installed in all browsers, and others.  Then it generates a report with a calculated "security score" and a list of recommended changes to system settings or usage patterns.

Security Checkup is java-based, however some 3rd party libraries include native code to interface to low-level Windows features.  The following libraries are currently used:
* [Java Native Access](https://github.com/java-native-access/jna)
* [wmi4j](https://github.com/chenlichao-cn/wmi4j)

Please note that this program is being developed for class credit and may not be supported long-term.

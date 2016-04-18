package com.mzlion.core.system;

/**
 * Created by mzlion on 2016/4/17.
 */
public class OSInfoServerParser {
    private static final String OS_ARCH;
    private static final String OS_NAME;
    private static final String OS_VERSION;
    private static final OSInfo OS_INFO;

    static {
        OS_ARCH = getSystemProperty("os.arch");
        OS_NAME = getSystemProperty("os.name");
        OS_VERSION = getSystemProperty("os.version");
        OS_INFO = new OSInfo() {
            @Override
            public String getOsName() {
                return OS_NAME;
            }

            @Override
            public String getOsVersion() {
                return OS_VERSION;
            }

            @Override
            public String getArch() {
                return OS_ARCH;
            }

            @Override
            public OSType getOsType() {
                return OSInfoServerParser.getOSType();
            }

            @Override
            public boolean isWindows10() {
                return OSType.WINDOWS.equals(getOsType()) && OS_VERSION.startsWith("6.3");
            }

            @Override
            public boolean isWindows8() {
                return OSType.WINDOWS.equals(getOsType()) && OS_VERSION.startsWith("6.2");
            }

            @Override
            public boolean isWindows7() {
                return OSType.WINDOWS.equals(getOsType()) && OS_VERSION.startsWith("6.1");
            }

            @Override
            public boolean isWindows() {
                return OSType.WINDOWS.equals(getOsType());
            }

            @Override
            public boolean isLinux() {
                return OSType.LINUX.equals(getOsType());
            }

            @Override
            public boolean isMacOS() {
                return OSType.MACOS.equals(getOsType());
            }
        };

    }

    private static String getSystemProperty(String property) {
        return System.getProperty(property);
    }

    private static OSInfo.OSType getOSType() {
        String osName = OS_NAME;
        if (osName.startsWith("Windows")) {
            return OSInfo.OSType.WINDOWS;
        }
        if (osName.startsWith("Linux")) {
            return OSInfo.OSType.LINUX;
        }
        if (osName.startsWith("Solaris") || osName.contains("SunOS")) {
            return OSInfo.OSType.SOLARIS;
        }
        if (osName.startsWith("Mac") || osName.contains("darwin")) {
            return OSInfo.OSType.MACOS;
        }
        return OSInfo.OSType.UNKNOWN;
    }

    public static OSInfo getOSInfo() {
        return OS_INFO;
    }
}

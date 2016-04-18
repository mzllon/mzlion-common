package com.mzlion.core.system;

/**
 * Created by mzlion on 2016/4/17.
 */
public interface OSInfo {

    enum OSType {
        WINDOWS,
        LINUX,
        SOLARIS,
        MACOS,
        ANDROID,
        UNKNOWN;

        private OSType() {
        }
    }

    String getOsName();

    String getOsVersion();

    String getArch();

    OSType getOsType();

    boolean isWindows10();

    boolean isWindows8();

    boolean isWindows7();

    boolean isWindows();

    boolean isLinux();

    boolean isMacOS();
}

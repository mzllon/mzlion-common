package com.mzlion.core.system;

/**
 * Created by mzlion on 2016/4/17.
 */
public class UserAgentUtils {

    public static String generate() {
        return generate(OSInfoServerParser.getOSInfo());
    }

    /**
     * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36
     * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
     * User-Agent: Mozilla/5.0 (Linux; U; Android 5.0.2; zh-cn; Redmi Note 3 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36
     */
    public static String generate(OSInfo osInfo) {
        if (osInfo == null) {
            throw new NullPointerException("OsInfo must not be null.");
        }
        StringBuilder sb = new StringBuilder(200);
        sb.append("Mozilla/5.0 (");
        if (osInfo.isWindows()) {
            if (osInfo.isWindows10()) {
                sb.append("Windows NT 10.0;");
            } else if (osInfo.isWindows8()) {
                sb.append("Windows NT 8.0;");
            } else if (osInfo.isWindows7()) {
                sb.append("Windows NT 7.0;");
            } else {
                sb.append("Windows NT 10.0;");
            }
            if ("amd64".equals(osInfo.getArch())) {
                sb.append(" Win64; x64");
            } else {
                sb.append(" Win86; x86");
            }
        } else if (osInfo.isLinux()) {
            sb.append("Linux; U; ");
            // TODO: 2016/4/17 未完善
        } else if (osInfo.isMacOS()) {
            sb.append("Linux; U; ");
            // TODO: 2016/4/17 未完善
        } else {
            sb.append("Windows NT 10.0; Win64; x64");
        }
        sb.append(" AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
        return sb.toString();
    }

}

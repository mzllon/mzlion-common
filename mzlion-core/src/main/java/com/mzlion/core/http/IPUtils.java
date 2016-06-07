package com.mzlion.core.http;


import com.mzlion.core.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 操作和IP相关的工具类
 * </p>
 *
 * @author mzlion
 */
public abstract class IPUtils {
    /**
     * 获取用户的真正IP地址
     *
     * @param request request对象
     * @return 返回用户的IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.trim().isEmpty()) || ("unknown".equalsIgnoreCase(ip)))
            ip = request.getHeader("Proxy-Client-IP");
        if ((ip == null) || (ip.trim().isEmpty()) || ("unknown".equalsIgnoreCase(ip)))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if ((ip == null) || (ip.trim().isEmpty()) || ("unknown".equalsIgnoreCase(ip)))
            ip = request.getRemoteAddr();
        return ip;
    }


    /**
     * 校验字符串是否符合IPv4规则
     *
     * @param ipv4 IP地址
     * @return 如果符合IPv4的规则返回{@code true},否则返回{@code false}
     */
    public static boolean matchesIpv4(String ipv4) {
        if (StringUtils.isEmpty(ipv4)) {
            return false;
        }
        Pattern pattern = Pattern.compile(IPV4_REGEX);
        Matcher matcher = pattern.matcher(ipv4);
        return matcher.matches();
    }

    /**
     * 校验字符串是否符合IPv6规则
     *
     * @param ipv6 IP地址
     * @return 如果符合IPv6的规则返回{@code true},否则返回{@code false}
     */
    public static boolean matchesIpv6(String ipv6) {
        if (StringUtils.isEmpty(ipv6)) {
            return false;
        }
        Pattern pattern = Pattern.compile(IPV6_REGEX);
        Matcher matcher = pattern.matcher(ipv6);
        return matcher.matches();
    }

    /**
     * IPv4的正则
     */
    public static final String IPV4_REGEX = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * IPv6的正则
     */
    public static final String IPV6_REGEX = "^([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:$";

}

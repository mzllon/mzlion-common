package com.mzlion.core.http;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 测试类
 * <p>
 * Created by mzlion on 2016/5/29.
 */
public class IPUtilsTest {

    private HttpServletRequest request;

    @Before
    public void before() throws Exception {
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void getClientIp() throws Exception {
        String xForwardForIp = "127.0.0.1";
        when(request.getHeader("x-forwarded-for")).thenReturn(xForwardForIp);
        String clientIp = IPUtils.getClientIp(request);
        assertEquals(xForwardForIp, clientIp);
    }

    @Test
    public void getClientIp1() throws Exception {
        String proxyClientIP = "192.168.0.4";
        when(request.getHeader("Proxy-Client-IP")).thenReturn(proxyClientIP);
        String clientIp = IPUtils.getClientIp(request);
        assertEquals(proxyClientIP, clientIp);
    }

    @Test
    public void getClientIp2() throws Exception {
        String wlProxyClientIP = "10.168.0.4";
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(wlProxyClientIP);
        String clientIp = IPUtils.getClientIp(request);
        assertEquals(wlProxyClientIP, clientIp);
    }

    @Test
    public void matchesIpv4() throws Exception {
        String ipv4 = "192.168.0.4";
        assertTrue(IPUtils.matchesIpv4(ipv4));
    }

    @Test
    public void matchesIpv6() throws Exception {
        String ipv6 = "192.168.0.4";
        assertFalse(IPUtils.matchesIpv6(ipv6));
    }

}
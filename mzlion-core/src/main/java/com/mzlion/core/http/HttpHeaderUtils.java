package com.mzlion.core.http;

import com.mzlion.core.lang.StringUtils;

import java.util.Locale;

/**
 * Created by mzlion on 2016/4/17.
 */
public class HttpHeaderUtils {

    public static final String HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language";

    /**
     * 返回支持支持的语言,格式为:Accept-Language: zh-CN,zh;q=0.8
     */
    public static String getAcceptLanguage() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        StringBuilder acceptLanguageBuilder = new StringBuilder(language);
        if (!StringUtils.isEmpty(country))
            acceptLanguageBuilder.append('-').append(country).append(',').append(language).append(";q=0.8");
        return acceptLanguageBuilder.toString();
    }



    public static String getUserAgent() {
        String webUserAgent = null;
        /*try {
            Class<?> sysResCls = Class.forName("com.android.internal.R$string");
            Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
            Integer resId = (Integer) webUserAgentField.get(null);
            webUserAgent = OkHttpUtils.getContext().getString(resId);
        } catch (Exception e) {
            // We have nothing to do
        }
        if (TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/5.0 %sSafari/533.1";
        }

        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");
        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase(locale));
            final String country = locale.getCountry();
            if (!TextUtils.isEmpty(country)) {
                buffer.append("-");
                buffer.append(country.toLowerCase(locale));
            }
        } else {
            // default to "en"
            buffer.append("en");
        }
        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }
        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }
        userAgent = String.format(webUserAgent, buffer, "Mobile ");
        return userAgent;*/
        return null;
    }

}

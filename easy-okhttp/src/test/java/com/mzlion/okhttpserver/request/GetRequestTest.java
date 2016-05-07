package com.mzlion.okhttpserver.request;

import com.mzlion.okhttpserver.HttpClient;
import com.mzlion.okhttpserver.response.HttpResponse;
import com.mzlion.okhttpserver.response.convert.StringResponseConvert;
import org.junit.Test;

/**
 * Created by mzlion on 2016/4/17.
 */
public class GetRequestTest {

    @Test
    public void get() throws Exception {
        final HttpResponse<String> httpResponse = HttpClient
                .get("http://wwww.baidu.com")
                .execute(new StringResponseConvert());
        System.out.println(httpResponse.toString());
    }

}
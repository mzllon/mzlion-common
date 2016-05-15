package com.mzlion.okhttpserver;

import com.mzlion.okhttpserver.entity.CommonResponse;
import com.mzlion.okhttpserver.entity.UserInfo;
import com.mzlion.okhttpserver.response.HttpResponse;
import com.mzlion.okhttpserver.response.convert.GsonResponseConverter;
import com.mzlion.okhttpserver.response.convert.TypeReference;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/4/26.
 */
public class UseInfoTest {
    private String uri = "http://localhost:8080/okhttp-server-test/userInfo";

    @Test
    public void testPageSelect() throws Exception {
        String url = uri + "/pageSelect";

        GsonResponseConverter<CommonResponse<List<UserInfo>>> converter =
                new GsonResponseConverter<>(new TypeReference<CommonResponse<List<UserInfo>>>() {
                });
        HttpResponse<CommonResponse<List<UserInfo>>> httpResponse = HttpClient.get(url).execute(converter);
        Assert.assertNotNull(httpResponse);
        Assert.assertTrue(httpResponse.isSuccess());
        CommonResponse<List<UserInfo>> entity = httpResponse.getEntity();
        Assert.assertNotNull(entity);
        Assert.assertEquals("0000", entity.getResultCode());
        System.out.println(httpResponse.getEntity());
    }

    @Test
    public void testPageSelectForNickName() throws Exception {
        String url = uri + "/pageSelect";

        GsonResponseConverter<CommonResponse<List<UserInfo>>> converter =
                new GsonResponseConverter<>(new TypeReference<CommonResponse<List<UserInfo>>>() {
                });
        HttpResponse<CommonResponse<List<UserInfo>>> httpResponse = HttpClient.get(url)
//                .parameter("nickName","张三")
                .parameter("nickName", "%E5%BC%A0%E4%B8%89")
                .execute(converter);
        Assert.assertNotNull(httpResponse);
        Assert.assertTrue(httpResponse.isSuccess());
        CommonResponse<List<UserInfo>> entity = httpResponse.getEntity();
        Assert.assertNotNull(entity);
        Assert.assertEquals("0000", entity.getResultCode());
        System.out.println(httpResponse.getEntity());
    }

    @Test
    public void testCreate() throws Exception {
        String url = uri + "/create";

        GsonResponseConverter<CommonResponse<String>> responseConverter =
                new GsonResponseConverter<>(new TypeReference<CommonResponse<String>>() {
                });

        HttpResponse<CommonResponse<String>> httpResponse = HttpClient.post(url)
                .parameter("userName", "zhangsan")
                .parameter("userPwd", "123456")
                .parameter("nickName", "张三")
                .parameter("realName", "张三三")
                .parameter("birth", "19901010")
                .parameter("gender", "1")
                .parameter("hobby", "爱好游泳，睡觉、看电视剧.")
                .execute(responseConverter);
        Assert.assertNotNull(httpResponse);
        Assert.assertTrue(httpResponse.isSuccess());
        CommonResponse<String> entity = httpResponse.getEntity();
        Assert.assertEquals("0000", entity.getResultCode());
        System.out.println(httpResponse.getEntity());
    }

    @Test
    public void test() throws Exception {
        //http://unirest.io/java.html
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8080/okhttp-server-test/userInfo/pageSelect?userName=%E5%BC%A0%E4%B8%89")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "2ee77f2d-1ee2-7d58-3aa5-a53971cf444a")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}

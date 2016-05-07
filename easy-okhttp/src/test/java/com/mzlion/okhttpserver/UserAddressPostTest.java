package com.mzlion.okhttpserver;

import com.mzlion.okhttpserver.entity.CommonResponse;
import com.mzlion.okhttpserver.entity.UserAddress;
import com.mzlion.okhttpserver.response.HttpResponse;
import com.mzlion.okhttpserver.response.convert.GsonResponseConverter;
import com.mzlion.okhttpserver.response.convert.TypeReference;
import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/4/26.
 */
public class UserAddressPostTest {
    private String uri = "http://paydev.ipayfor.me:8030/c1-app-sys/api/user/address";


    @Test
    public void testList() throws Exception {
        String url = uri + "/list";

        GsonResponseConverter<CommonResponse<List<UserAddress>>> converter =
                new GsonResponseConverter<>(new TypeReference<CommonResponse<List<UserAddress>>>() {
        });
        HttpResponse<CommonResponse<List<UserAddress>>> httpResponse = HttpClient.post(url)
                .parameter("userId", "3000000023").parameter("version", "1.0").execute(converter);
        System.out.println(httpResponse.toString());
    }

    @Test
    public void test() throws Exception {

    }

}

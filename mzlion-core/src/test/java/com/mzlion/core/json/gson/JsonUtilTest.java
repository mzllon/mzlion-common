package com.mzlion.core.json.gson;

import com.google.gson.reflect.TypeToken;
import com.mzlion.core.vo.Person;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 测试类
 * <p>
 * Created by mzlion on 2016/4/14.
 */
public class JsonUtilTest {

    private List<Person> beanList;
    private String jsonList;
    private Person bean;

    private String jsonStr = "{\"userId\":\"20160629\",\"username\":\"mzlion\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":2,\"birth\":null,\"mobile\":\"18321884000\",\"email\":null}";


    @Before
    public void before() throws Exception {
        beanList = new ArrayList<>(4);

        Person person = new Person();
        person.setUserId("uid-9900-01");
        person.setUsername("张三");
        person.setSex("男");
        person.setMobile("17715679802");
        person.setEmail("and@gmail.com");
        person.setBirth("19900010");
        person.setPassword("123456");
        beanList.add(person);

        person.setUserId("uid-9900-02");
        person.setUsername("李四");
        person.setSex("男");
        person.setMobile("17715679803");
        person.setEmail("andrew@gmail.com");
        person.setBirth("19900010");
        person.setPassword("123456");
        beanList.add(person);

        jsonList = "[{\"userId\":\"uid-9900-02\",\"username\":\"李四\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679803\",\"email\":\"andrew@gmail.com\"},{\"userId\":\"uid-9900-02\",\"username\":\"李四\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679803\",\"email\":\"andrew@gmail.com\"}]";

        bean = new Person();
        bean.setUserId("20160629");
        bean.setUsername("mzlion");
        bean.setPassword("123456");
        bean.setSex("男");
        bean.setSort(2);
        bean.setMobile("18321884000");
    }

    @Test
    public void toJson() throws Exception {
        assertEquals(jsonList, JsonUtil.toJson(beanList));
    }

    @Test
    public void toBean() throws Exception {
        Person person = JsonUtil.toBean(jsonStr, Person.class);
        assertEquals(bean, person);
    }

    @Test
    public void toBean1() throws Exception {
        Person person = JsonUtil.toBean(jsonStr, (Type) Person.class);
        assertEquals(bean, person);
    }

    @Test
    public void toBean2() throws Exception {
        TypeToken<List<Person>> typeToken = new TypeToken<List<Person>>() {

        };
        List<Person> personList = JsonUtil.toBean(jsonList, typeToken);
        assertEquals(beanList, personList);
    }

}
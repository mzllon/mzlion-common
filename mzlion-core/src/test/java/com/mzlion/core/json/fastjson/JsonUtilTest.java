package com.mzlion.core.json.fastjson;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mzlion.core.vo.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * 测试类
 *
 * @author mzlion
 */
public class JsonUtilTest {

    private List<Person> personList;
    private String jsonList;
    private Person personProperty;

    private String jsonStr = "{\"email\":\"email@163.com\",\"mobile\":\"18321884000\",\"password\":\"123456\",\"sex\":\"\\u7537\",\"sort\":2,\"userId\":\"20160629\",\"username\":\"mzlion\"}";


    @Before
    public void setup() throws Exception {
        personList = new ArrayList<>(4);

        Person person = new Person();
        person.setUserId("uid-9900-01");
        person.setUsername("张三");
        person.setSex("男");
        person.setMobile("17715679802");
        person.setEmail("and@gmail.com");
        person.setBirth("19900010");
        person.setPassword("123456");
        personList.add(person);

        person.setUserId("uid-9900-02");
        person.setUsername("李四");
        person.setSex("男");
        person.setMobile("17715679803");
        person.setEmail("andrew@gmail.com");
        person.setBirth("19900010");
        person.setPassword("123456");
        personList.add(person);

        jsonList = "[{\"userId\":\"uid-9900-02\",\"username\":\"李四\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679803\",\"email\":\"andrew@gmail.com\"},{\"userId\":\"uid-9900-02\",\"username\":\"李四\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679803\",\"email\":\"andrew@gmail.com\"}]";

        personProperty = new Person();
        personProperty.setUserId("20160629");
        personProperty.setUsername("mzlion");
        personProperty.setPassword("123456");
        personProperty.setSex("男");
        personProperty.setSort(2);
        personProperty.setMobile("18321884000");
        personProperty.setEmail("email@163.com");
    }

    @Test
    public void toJson() throws Exception {
        String json = JsonUtil.toJson(personProperty);
        assertEquals(jsonStr, json);
    }

    @Test
    public void toJson1() throws Exception {
        String toJson = JsonUtil.toJson(personProperty, "email");
        assertEquals("{\"mobile\":\"18321884000\",\"password\":\"123456\",\"sex\":\"\\u7537\",\"sort\":2,\"userId\":\"20160629\",\"username\":\"mzlion\"}", toJson);
    }

    @Test
    public void toJson2() throws Exception {
        Map<Class<?>, List<String>> classOfProps = new HashMap<>(4);
        classOfProps.put(Person.class, Collections.singletonList("email"));
        String toJson = JsonUtil.toJson(personProperty, classOfProps);
        assertEquals("{\"mobile\":\"18321884000\",\"password\":\"123456\",\"sex\":\"\\u7537\",\"sort\":2,\"userId\":\"20160629\",\"username\":\"mzlion\"}", toJson);
    }

    @Test
    public void toJson3() throws Exception {
        String toJson = JsonUtil.toJson(personProperty, new SerializerFeature[]{});
        assertEquals("{\"email\":\"email@163.com\",\"mobile\":\"18321884000\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":2,\"userId\":\"20160629\",\"username\":\"mzlion\"}", toJson);
    }

    @Test
    public void toJsonWithDateFormat() throws Exception {
        String toJson = JsonUtil.toJsonWithDateFormat(personProperty, "yyyMMdd");
        assertEquals(jsonStr, toJson);
    }

    @Test
    public void toBean() throws Exception {
        Person person = JsonUtil.toBean(jsonStr, Person.class);
        assertEquals(personProperty, person);
    }

    @Test
    public void toBean2() throws Exception {
        List<Person> list = JsonUtil.toBean(jsonList, new TypeReference<List<Person>>() {

        });
        assertEquals(personList, list);
    }

    @Test
    public void println() throws Exception {
        //ignore
    }

}
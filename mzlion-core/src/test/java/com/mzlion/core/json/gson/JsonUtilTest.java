package com.mzlion.core.json.gson;

import com.mzlion.core.vo.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzlion on 2016/4/14.
 */
public class JsonUtilTest {
    @Test
    public void toJson() throws Exception {
        List<Person> personList = new ArrayList<>(4);

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


        System.out.println(JsonUtil.toJson(personList));
    }

    @Test
    public void toBean() throws Exception {
        String json = "{\"userId\":\"uid-9900-x\",\"username\":\"张三\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679802\",\"email\":\"and@gmail.com\"}";
        System.out.println(JsonUtil.toBean(json, Person.class));
    }

    @Test
    public void toList() throws Exception {
        String json = "[{\"userId\":\"uid-9900-02\",\"username\":\"李四\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679803\",\"email\":\"andrew@gmail.com\"},{\"userId\":\"uid-9900-02\",\"username\":\"李四\",\"password\":\"123456\",\"sex\":\"男\",\"sort\":0,\"birth\":\"19900010\",\"mobile\":\"17715679803\",\"email\":\"andrew@gmail.com\"}]";
        List<Person> personList = JsonUtil.toList(json, Person.class);
        System.out.println(personList.size());
        for (Person person : personList) {
            System.out.println(person);
        }
    }


}
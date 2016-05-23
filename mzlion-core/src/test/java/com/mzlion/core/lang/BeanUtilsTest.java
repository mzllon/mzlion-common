package com.mzlion.core.lang;

import com.mzlion.core.vo.Person;
import org.junit.Test;

import java.util.Map;

/**
 * Created by mzlion on 2016/5/22.
 */
public class BeanUtilsTest {
    @Test
    public void toMapAsValueObject() throws Exception {

    }

    @Test
    public void toMapAsValueString() throws Exception {
        Person person = new Person();
        person.setUserId("uid-9900-01");
        person.setUsername("张三");
        person.setSex("男");
        person.setMobile("17715679802");
        person.setEmail("and@gmail.com");
        person.setBirth("19900010");
        person.setPassword("123456");

        Map<String, String> resultMap = BeanUtils.toMapAsValueString(person, false);
        System.out.println(resultMap);
    }

    @Test
    public void toCopyProperties() throws Exception {
        Person person1 = new Person();
        person1.setUserId("uid-9900-01");
        person1.setUsername("张三");
        person1.setSex("男");
        person1.setMobile("17715679802");
        person1.setEmail("and@gmail.com");
        person1.setBirth("19900010");
        System.out.println(person1);

        Person person2 = new Person();
        BeanUtils.copyProperties(person1, person2);
        System.out.println(person2);
    }
}
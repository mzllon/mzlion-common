package com.mzlion.core.lang;

import com.mzlion.core.vo.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Testing for {@link BeanUtils}
 * <p>
 * Created by mzlion on 2016/5/22.
 */
public class BeanUtilsTest {

    private Person person;

    @Before
    public void before() throws Exception {
        person = new Person();
        person.setUserId("uid-9900-01");
        person.setUsername("张三");
        person.setSex("男");
        person.setMobile("17715679802");
        person.setEmail("and@gmail.com");
        person.setBirth("19900010");
        person.setSort(2);
    }

    @Test
    public void toMapAsValueObject() throws Exception {
        Map<String, Object> expectResult = new HashMap<>(10);
        expectResult.put("userId", "uid-9900-01");
        expectResult.put("username", "张三");
        expectResult.put("sex", "男");
        expectResult.put("mobile", "17715679802");
        expectResult.put("email", "and@gmail.com");
        expectResult.put("birth", "19900010");
        expectResult.put("password", null);
        expectResult.put("sort", 2);
        Map<String, Object> actualResult = BeanUtils.toMapAsValueObject(person);
        assertNotNull(actualResult);
        assertEquals(expectResult.size(), actualResult.size());
        for (String key : expectResult.keySet()) {
            assertEquals(expectResult.get(key), actualResult.get(key));
        }
    }

    @Test
    public void toMapAsValueString() throws Exception {
        Map<String, String> expectResult = new HashMap<>(10);
        expectResult.put("userId", "uid-9900-01");
        expectResult.put("username", "张三");
        expectResult.put("sex", "男");
        expectResult.put("mobile", "17715679802");
        expectResult.put("email", "and@gmail.com");
        expectResult.put("birth", "19900010");
        expectResult.put("sort", "2");
        Map<String, String> actualResult = BeanUtils.toMapAsValueString(person);
        assertNotNull(actualResult);
        assertEquals(expectResult.size(), actualResult.size());
        for (String key : expectResult.keySet()) {
            assertEquals(expectResult.get(key), actualResult.get(key));
        }
    }

    @Test
    public void toMapAsValueString2() throws Exception {
        Map<String, String> expectResult = new HashMap<>(10);
        expectResult.put("userId", "uid-9900-01");
        expectResult.put("username", "张三");
        expectResult.put("sex", "男");
        expectResult.put("mobile", "17715679802");
        expectResult.put("email", "and@gmail.com");
        expectResult.put("birth", "19900010");
        expectResult.put("sort", "2");
        Map<String, String> actualResult = BeanUtils.toMapAsValueString(person, true);
        assertNotNull(actualResult);
        assertEquals(expectResult.size(), actualResult.size());
        assertEquals(expectResult, actualResult);
    }

    @Test
    public void copyProperties() throws Exception {
        Person personCopy = new Person();
        BeanUtils.copyProperties(person, personCopy);
        assertEquals(person, personCopy);
    }

    @Test
    public void copyProperties2() throws Exception {
        Person personCopy = new Person();
        BeanUtils.copyProperties(person, personCopy, "password");
        assertEquals(person, personCopy);
    }

}
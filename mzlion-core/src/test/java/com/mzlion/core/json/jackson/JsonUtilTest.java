package com.mzlion.core.json.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mzlion.core.bean.Person;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/4/14.
 */
public class JsonUtilTest {
    @Test
    public void fromJSON() throws Exception {
        String json = "[{\"userId\":\"7adbere9cm8a7ckttcv6i3f5tj\",\"username\":\"张三\",\"password\":\"n9inuc5gkujtv7btm7o5j8hbh\",\"sex\":\"男\",\"sort\":-1210225942,\"birth\":\"228i69913ff69q1c8kdursbdep\",\"mobile\":\"36gakmlr4htlpjk6031jbcscte\",\"email\":\"44bgpor6n7f3ji4528h75oir7e\"},{\"userId\":\"p37g4ni2bbh7att5sq38bbs9n\",\"username\":\"4l3rucgm6p99d136n5b1ha398l\",\"password\":\"1ur04r5opnac6dofnm89u8vrho\",\"sex\":\"2jgm02m496d2ochlddfk3rp7n9\",\"sort\":-1908238811,\"birth\":\"3laq39qaml6o7r5vqhb28b7spt\",\"mobile\":\"22fliedrf9pr9ednb5t8tv41ij\",\"email\":\"2328tb581mh4v9uh45hrrvqd4l\"}]";
        List<Person> personList = JsonUtil.toBean(json, new TypeReference<List<Person>>() {
        });
        System.out.println(personList);
    }

    @Test
    public void fromJSON1() throws Exception {

    }

    @Test
    public void toJSONWithCn() throws Exception {
        Person person = new Person();
        person.setUserId("1");
        person.setUsername("炸昂三");
        person.setBirth("1990-10-10");
        person.setEmail("and.mz@gmail.com");
        person.setMobile("8132199");
        person.setSex("男");
        person.setSort(1);
        String json = JsonUtil.toJson(person);
        System.out.println(json);
        json = JsonUtil.toJsonWithUnicode(person);
        System.out.println(json);
        json = JsonUtil.toJson(person);
        System.out.println(json);
    }

    @Test
    public void toJSON() throws Exception {
        EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        List<Person> personList = random.nextObjects(Person.class, 100000);
        long start = System.currentTimeMillis();
        String json = JsonUtil.toJson(personList);
        long end = System.currentTimeMillis();
        System.out.println("use time->" + (end - start));
//        System.out.println(json);
    }

    @Test
    public void toPerson() throws Exception {
        EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        List<Person> personList = random.nextObjects(Person.class, 100);
        for (Person person : personList) {
            System.out.println(person);
        }
    }

}
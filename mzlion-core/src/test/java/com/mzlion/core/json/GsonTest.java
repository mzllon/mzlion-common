package com.mzlion.core.json;

import com.google.gson.Gson;
import com.mzlion.core.bean.Person;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/4/14.
 */
public class GsonTest {

    @Test
    public void test() throws Exception {
        EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        List<Person> personList = random.nextObjects(Person.class, 100000);//
        long start = System.currentTimeMillis();
        Gson gson = new Gson();
        String json = gson.toJson(personList);
        long end = System.currentTimeMillis();
        System.out.println(end-start);
//        System.out.println(json);
    }

}

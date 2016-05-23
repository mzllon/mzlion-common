package com.mzlion.core.json.fastjson;

import com.mzlion.core.vo.Person;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * 测试
 * <p>
 * Created by mzlion on 2016/4/14.
 *
 * @author mzlion
 */
public class JsonUtilTest {
    private static List<Person> personList;

    @BeforeClass
    public static void setup() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
            personList = enhancedRandom.nextObjects(Person.class, 100000);
    }

    @Test
    public void toJSON() throws Exception {
        long start = System.currentTimeMillis();
        String json = JsonUtil.toJson(personList);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void toJSON1() throws Exception {

    }

    @Test
    public void toJSON2() throws Exception {

    }

    @Test
    public void toJSON3() throws Exception {

    }

    @Test
    public void toJSONWithDateFormat() throws Exception {

    }

    @Test
    public void fromJSON() throws Exception {

    }

    @Test
    public void fromJSON1() throws Exception {

    }

    @Test
    public void fromJSONList() throws Exception {

    }

    @Test
    public void fromJSON2() throws Exception {

    }

    @Test
    public void println() throws Exception {

    }

}
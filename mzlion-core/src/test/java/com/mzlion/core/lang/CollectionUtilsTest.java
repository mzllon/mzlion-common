package com.mzlion.core.lang;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Testing for {@linkplain CollectionUtils}
 * <p>
 * Created by mzlion on 2016/5/29.
 */
public class CollectionUtilsTest {

    @Test
    public void isEmpty() throws Exception {
        assertTrue(CollectionUtils.isEmpty(Collections.emptyMap()));
    }

    @Test
    public void isEmpty1() throws Exception {
        assertTrue(CollectionUtils.isEmpty(Collections.emptyList()));
    }

    @Test
    public void isNotEmpty() throws Exception {
        assertTrue(CollectionUtils.isNotEmpty(Collections.singletonMap("author", "mzlion")));
    }

    @Test
    public void isNotEmpty1() throws Exception {
        assertTrue(CollectionUtils.isNotEmpty(Collections.singletonList("mzlion")));
    }

    @Test
    public void asList() throws Exception {
        List<String> list = CollectionUtils.asList("mzlion", "andy");
        List<String> jdkList = Arrays.asList("mzlion", "andy");
        assertEquals(jdkList, list);
        list.add("lucy");
        assertEquals(3, list.size());
    }

    @Test
    public void asMap() throws Exception {
        Map<String, String> map = CollectionUtils.asMap(new String[]{"author", "email"}, new String[]{"mzlion", "and@gmail.com"});
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    @Test
    public void asMap1() throws Exception {
        Map<String, String> map = CollectionUtils.asMap("author,email", "mzlion,and@gmail.com", ",");
        assertNotNull(map);
        assertEquals(2, map.size());
    }

    @Test
    public void urlParam2Map() throws Exception {
        String urlParam = "username=admin&password=123456&code=1234";
        Map<String, String> map = CollectionUtils.urlParam2Map(urlParam);
        assertNotNull(map);
        assertEquals(3, map.size());
    }

}
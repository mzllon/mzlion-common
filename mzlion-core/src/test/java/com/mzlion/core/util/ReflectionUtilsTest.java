package com.mzlion.core.util;

import com.mzlion.core.vo.Student;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by mzlion on 2016/6/7.
 */
public class ReflectionUtilsTest {
    @Test
    public void getDeclaredFields() throws Exception {
        System.out.println("=============Student=============");
        List<Field> fields = ReflectionUtils.getDeclaredFields(Student.class);
        for (Field field : fields) {
            System.out.println(field);
        }
        System.out.println("=============Student=============");
    }

}
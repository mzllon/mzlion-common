package com.mzlion.poi;

import org.junit.Test;

import java.util.List;

/**
 * Created by mzlion on 2016/6/12.
 */
public class TypeReferenceTest {
    @Test
    public void getGenericsType() throws Exception {
        FooTypeReference fooTypeReference = new FooTypeReference();
    }


}

class Foo {

}

class FooTypeReference extends TypeReference<List<Foo>> {

}

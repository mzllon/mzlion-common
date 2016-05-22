package com.mzlion.core.lang;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 * 2016-05-21 22:58 针对{@link StringUtils}类的测试
 * </p>
 *
 * @author mzlion
 */
public class StringUtilsTest {

    @Test
    public void isEmpty() throws Exception {
        assertTrue(StringUtils.isEmpty((CharSequence) ""));
    }

    @Test
    public void isEmpty1() throws Exception {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    public void hasLength() throws Exception {
        assertFalse(StringUtils.hasLength((CharSequence) null));
    }

    @Test
    public void hasLength1() throws Exception {
        assertFalse(StringUtils.hasLength(""));
        assertTrue(StringUtils.hasLength(" "));
        assertTrue(StringUtils.hasLength("Hi"));
    }

    @Test
    public void hasText() throws Exception {
        assertFalse(StringUtils.hasText((CharSequence) null));
    }

    @Test
    public void hasText1() throws Exception {
        assertFalse(StringUtils.hasText(""));
        assertFalse(StringUtils.hasText(" "));
        assertTrue(StringUtils.hasText(" H"));
        assertTrue(StringUtils.hasText("HH"));
    }

    @Test
    public void containsWhitespace() throws Exception {
        assertFalse(StringUtils.containsWhitespace(""));
    }

    @Test
    public void containsWhitespace1() throws Exception {
        assertTrue(StringUtils.containsWhitespace((CharSequence) "Hi llo"));
        assertTrue(StringUtils.containsWhitespace((CharSequence) " "));
    }

    @Test
    public void trim() throws Exception {
        String src = " mzl ion ";
        String expected = "mzl ion";
        assertEquals(expected, StringUtils.trim(src));
    }

    @Test
    public void trimLeftWhiteSpace() throws Exception {
        String src = " mzlion ";
        String expected1 = "mzlion ", expected2 = "mzlion";
        assertEquals(expected1, StringUtils.trimLeftWhiteSpace(src));
        assertNotSame(expected2, StringUtils.trimLeftWhiteSpace(src));
    }

    @Test
    public void trimRightWhiteSpace() throws Exception {
        String src = " mzlion ";
        String expected1 = " mzlion", expected2 = "mzlion";
        assertEquals(expected1, StringUtils.trimRightWhiteSpace(src));
        assertNotSame(expected2, StringUtils.trimRightWhiteSpace(src));
    }

    @Test
    public void startsWithIgnoreCase() throws Exception {
        String src = "Hello kitty.";
        String startWith = "hello";
        assertTrue(StringUtils.startsWithIgnoreCase(src, startWith));
        assertFalse(StringUtils.startsWithIgnoreCase(src, "Hi"));
    }

    @Test
    public void endsWithIgnoreCase() throws Exception {
        String src = "world";
        assertTrue(StringUtils.endsWithIgnoreCase(src, "ld"));
        assertTrue(StringUtils.endsWithIgnoreCase(src, "lD"));
        assertFalse(StringUtils.endsWithIgnoreCase(src, "xld"));
    }

    @Test
    public void replace() throws Exception {
        String src = "hibi";
        String replace = StringUtils.replace(src, "i", "e");
        assertEquals("hebe", replace);
    }

    @Test
    public void delete() throws Exception {
        String str = "kitty";
        String delete = StringUtils.delete(str, "t");
        assertEquals("kiy", delete);
        str = "I am a  coder";
        delete = StringUtils.delete(str, "\\s");
        assertNotSame("Iamacoder", delete);
    }

    @Test
    public void capitalize() throws Exception {
        assertEquals("Coder", StringUtils.capitalize("coder"));
    }

    @Test
    public void unCapitalize() throws Exception {
        assertEquals("coder", StringUtils.unCapitalize("Coder"));
        assertEquals("1 dollar", StringUtils.unCapitalize("1 dollar"));
    }

    @Test
    public void changeCharacterCase() throws Exception {
        assertEquals("Coder is bad.", StringUtils.changeCharacterCase("Coder is Bad.", 9, false));
    }

    @Test
    public void addStringToArray() throws Exception {
        String[] array = {"I", "am", "a"};
        String[] expect = {"I", "am", "a", "coder"};
        String[] coders = StringUtils.addStringToArray(array, "coder");
        assertArrayEquals(expect, coders);
    }

    @Test
    public void concatStringArrays() throws Exception {
        String[] array1 = {"lily", "lucy"};
        String[] array2 = {"mzlion", "white"};

        String[] arrays = StringUtils.concatStringArrays(array1, array2);
        assertArrayEquals(new String[]{"lily", "lucy", "mzlion", "white"}, arrays);
    }

    @Test
    public void mergeStringArrays() throws Exception {
        String[] array1 = {"lily", "lucy", "mzlion"};
        String[] array2 = {"mzlion", "white"};

        String[] arrays = StringUtils.mergeStringArrays(array1, array2);
        assertArrayEquals(new String[]{"lily", "lucy", "mzlion", "white"}, arrays);
    }

    @Test
    public void sortStringArray() throws Exception {
        String[] array = {"lucy", "lily", "andy", "mzlion"};

        String[] arrays = StringUtils.sortStringArray(array);
        assertArrayEquals(new String[]{"andy", "lily", "lucy", "mzlion"}, arrays);
    }

    @Test
    public void toStringArray() throws Exception {
        List<String> list = Arrays.asList("andy", "mzlion");
        assertArrayEquals(new String[]{"andy", "mzlion"}, StringUtils.toStringArray(list));
    }

    @Test
    public void splitAtFirst() throws Exception {
        String src = "xo-ua-fast";
        String[] arrays = StringUtils.splitAtFirst(src, "-");
        assertArrayEquals(new String[]{"xo", "ua-fast"}, arrays);
    }

    @Test
    public void split() throws Exception {
        String src = "xo-ua-fast";
        String[] arrays = StringUtils.split(src, "-");
        assertArrayEquals(new String[]{"xo", "ua", "fast"}, arrays);
    }

    @Test
    public void toUnderline() throws Exception {
        assertEquals("operator_name", StringUtils.toUnderline("OperatorName"));
    }

    @Test
    public void toUnderline1() throws Exception {
        assertEquals("OPERATOR_NAME", StringUtils.toUnderline("OperatorName", true));
    }

    @Test
    public void toCamelCase() throws Exception {
//        assertEquals("operatorName", StringUtils.toCamelCase("OPERATOR_NAME"));
        assertEquals("operatorName", StringUtils.toCamelCase("_OPERATOR____NAME"));
    }

    @Test
    public void isDigital() throws Exception {
        assertFalse(StringUtils.isDigital(""));
        assertTrue(StringUtils.isDigital("10"));
        assertFalse(StringUtils.isDigital("+10"));
        assertFalse(StringUtils.isDigital("10.0"));
    }

    @Test
    public void isNumeric() throws Exception {
        assertFalse(StringUtils.isNumeric(""));
        assertFalse(StringUtils.isNumeric("str"));
        assertTrue(StringUtils.isNumeric("10"));
        assertTrue(StringUtils.isNumeric("+10"));
        assertTrue(StringUtils.isNumeric("-10"));
        assertTrue(StringUtils.isNumeric("10.0"));
    }

}
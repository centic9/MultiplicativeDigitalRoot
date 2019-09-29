package org.dstadler.multiplication;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplicativeDigitalRootTest {

    @Test
    public void getMultiplicativeDigitalRoot() {
        checkRoot("1", "1");
        checkRoot("1", "11");
        checkRoot("2", "12");
        checkRoot("10", "25");
        checkRoot("25", "55");
        checkRoot("0", "0");
        checkRoot("0", "10");
        checkRoot("0", "5040");
        checkRoot("672", "3784");
        checkRoot("84", "672");
        checkRoot("32", "84");
        checkRoot("6", "32");
    }

    private void checkRoot(String expected, String input) {
        assertEquals(expected, MultiplicativeDigitalRoot.getMultiplicativeDigitalRoot(input).toString());
    }

    @Test
    public void testPersistence() {
        checkPersistence(1, "1");
        checkPersistence(1, "2");
        checkPersistence(1, "10");
        checkPersistence(2, "25");
        checkPersistence(3, "39");
        checkPersistence(4, "77");
        checkPersistence(5, "679");
        checkPersistence(6, "6788");
        checkPersistence(7, "68889");
        checkPersistence(8, "2677889");
        checkPersistence(9, "26888999");
        checkPersistence(10, "3778888999");
        checkPersistence(11, "277777788888899");
    }

    private void checkPersistence(int expected, String input) {
        assertEquals(expected, MultiplicativeDigitalRoot.getPersistence(input));
    }
}

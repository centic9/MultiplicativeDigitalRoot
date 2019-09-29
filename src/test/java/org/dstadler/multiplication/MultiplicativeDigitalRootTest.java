package org.dstadler.multiplication;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplicativeDigitalRootTest {
    @Test
    public void testCandidate() {
        assertFalse(MultiplicativeDigitalRoot.candidate("0"));
        assertFalse(MultiplicativeDigitalRoot.candidate("21"));
        assertFalse(MultiplicativeDigitalRoot.candidate("222222222222222222222234567898"));
        assertFalse(MultiplicativeDigitalRoot.candidate("12345"));
        assertFalse(MultiplicativeDigitalRoot.candidate("15234"));
        assertFalse(MultiplicativeDigitalRoot.candidate("1"));

        // these two known smallest number are not seen as candidates
        // because "0" or "25" cannot appear in any higher persistence
        assertFalse(MultiplicativeDigitalRoot.candidate("10"));
        assertFalse(MultiplicativeDigitalRoot.candidate("25"));

        assertTrue(MultiplicativeDigitalRoot.candidate(""));
        assertTrue(MultiplicativeDigitalRoot.candidate("99999"));

        // all other known smallest numbers need to be candidates
        assertTrue(MultiplicativeDigitalRoot.candidate("39"));
        assertTrue(MultiplicativeDigitalRoot.candidate("77"));
        assertTrue(MultiplicativeDigitalRoot.candidate("679"));
        assertTrue(MultiplicativeDigitalRoot.candidate("6788"));
        assertTrue(MultiplicativeDigitalRoot.candidate("68889"));
        assertTrue(MultiplicativeDigitalRoot.candidate("2677889"));
        assertTrue(MultiplicativeDigitalRoot.candidate("26888999"));
        assertTrue(MultiplicativeDigitalRoot.candidate("3778888999"));
        assertTrue(MultiplicativeDigitalRoot.candidate("277777788888899"));
    }
}
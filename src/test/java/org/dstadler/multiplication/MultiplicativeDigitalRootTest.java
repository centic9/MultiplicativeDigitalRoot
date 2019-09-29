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

        assertTrue(MultiplicativeDigitalRoot.candidate(""));
        assertTrue(MultiplicativeDigitalRoot.candidate("1"));
        assertTrue(MultiplicativeDigitalRoot.candidate("99999"));
    }
}
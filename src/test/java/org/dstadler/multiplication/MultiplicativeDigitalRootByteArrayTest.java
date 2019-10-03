package org.dstadler.multiplication;

import org.junit.Test;

import java.math.BigInteger;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiplicativeDigitalRootByteArrayTest {
    @Test
    public void testCandidate() {
        checkCandidate(false, "0");
        checkCandidate(false, "21");
        checkCandidate(false, "222222222222222222222234567898");
        checkCandidate(false, "12345");
        checkCandidate(false, "15234");
        checkCandidate(false, "1");

        // these two known smallest number are not seen as candidates
        // because "0" or "25" cannot appear in any higher persistence
        checkCandidate(false, "10");
        checkCandidate(false, "25");

        //checkCandidate(true, "");
        checkCandidate(true, "99999");

        // all other known smallest numbers need to be candidates
        checkCandidate(true, "39");
        checkCandidate(true, "77");
        checkCandidate(true, "679");
        checkCandidate(true, "6788");
        checkCandidate(true, "68889");
        checkCandidate(true, "2677889");
        checkCandidate(true, "26888999");
        checkCandidate(true, "3778888999");
        checkCandidate(true, "277777788888899");
    }
    
    private void checkCandidate(boolean expected, String numberStr) {
        byte[] number = new byte[MAX_DIGITS];
        MathUtils.toByteArray(number, new BigInteger(numberStr));
        
        assertEquals("Faild for " + expected + " and " + numberStr, 
                expected, MultiplicativeDigitalRootByteArray.candidate(number));
    }
}

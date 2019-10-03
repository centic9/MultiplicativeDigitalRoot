package org.dstadler.multiplication;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
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
        checkCandidate(false, "1748");
        checkCandidate(false, "87");

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
        assertEquals("Faild for " + expected + " and " + numberStr,
                expected, MultiplicativeDigitalRoot.candidate(numberStr));
    }

    @Test
    public void testCompare() {
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < 100000;i++) {
            String str = RandomStringUtils.randomNumeric(RandomUtils.nextInt(1, 100));
            while(str.startsWith("0")) {
                str = StringUtils.removeStart(str, "0");
            }
            if(str.isEmpty()) {
                continue;
            }

            MathUtils.toByteArray(number, new BigInteger(str));

            assertEquals("Failed after " + i + " for " + str,
                    MultiplicativeDigitalRoot.candidate(str),
                    MultiplicativeDigitalRootByteArray.candidate(number));
        }
    }

    @Test
    public void testIncrement() {
        checkIncrement(new byte[] {1, -1}, new byte[] {-1, -1});
        checkIncrement(new byte[] {1, -1}, new byte[] {0, -1});
        checkIncrement(new byte[] {2, -1}, new byte[] {1, -1});
        checkIncrement(new byte[] {0, 1, -1}, new byte[] {9, -1, 0});
        checkIncrement(new byte[] {0, 0, 1, -1}, new byte[] {9, 9, -1, 0});
        checkIncrement(new byte[] {1, 0, 1, -1}, new byte[] {0, 0, 1, -1});

        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < 1000;i++) {
            MathUtils.toByteArray(number, new BigInteger(Integer.toString(i)));
            MultiplicativeDigitalRootByteArray.increment(number);
            assertEquals(Integer.toString(i+1), MathUtils.toString(number));
        }
    }

    private void checkIncrement(byte[] expected, byte[] number) {
        MultiplicativeDigitalRootByteArray.increment(number);

        for(int i = 0;i < MAX_DIGITS;i++) {
            if(expected[i] == -1 && number[i] == -1) {
                break;
            }

            assertEquals("Failed at " + i,
                    expected[i], number[i]);
        }
    }
}
package org.dstadler.multiplication;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

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

        // ByteArray is optimized to not expect "0" or "1" at all
        if (!numberStr.contains("0") && !numberStr.contains("1")) {
            assertEquals("Faild for " + expected + " and " + numberStr,
                    expected, MultiplicativeDigitalRootByteArray.candidate(number));
        }
        assertEquals("Faild for " + expected + " and " + numberStr,
                expected, MultiplicativeDigitalRoot.candidate(numberStr));
    }

    @Test
    public void testCandidateExhausted() {
        // all nines means we would overflow the buffer next
        byte[] number = new byte[MAX_DIGITS];
        for (int i = 0; i < MAX_DIGITS; i++) {
            number[i] = 9;
        }

        //noinspection ResultOfMethodCallIgnored
        assertThrows(IllegalStateException.class,
                () -> MultiplicativeDigitalRootByteArray.candidate(number));
    }

    @Test
    public void testCompare() {
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < 10000;i++) {
            String str = RandomStringUtils.randomNumeric(RandomUtils.nextInt(1, 100));
            while(str.startsWith("0")) {
                str = StringUtils.removeStart(str, "0");
            }
            if(str.isEmpty()) {
                continue;
            }

            MathUtils.toByteArray(number, new BigInteger(str));

            // ByteArray is optimized to not expect "0" or "1" at all
            if (!str.contains("0") && !str.contains("1")) {
                assertEquals("Failed after " + i + " for " + str,
                        MultiplicativeDigitalRoot.candidate(str),
                        MultiplicativeDigitalRootByteArray.candidate(number));
            }
        }
    }

    @Test
    public void testIncrement() {
        checkIncrement(new byte[] {2, -1}, new byte[] {-1, -1});
        checkIncrement(new byte[] {1, -1}, new byte[] {0, -1, -1});
        checkIncrement(new byte[] {2, -1}, new byte[] {1, -1});
        checkIncrement(new byte[] {2, 2, -1}, new byte[] {9, -1, -1});
        checkIncrement(new byte[] {2, 2, 2, -1}, new byte[] {9, 9, -1, -1});
        checkIncrement(new byte[] {1, 0, 1, -1}, new byte[] {0, 0, 1, -1});
        checkIncrement(new byte[] {3, 2, 2, -1}, new byte[] {2, 2, 2, -1});
        checkIncrement(new byte[] {8, 4, 2, -1}, new byte[] {7, 4, 2, -1});

        /* We now skip many increments
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < 1000;i++) {
            MathUtils.toByteArray(number, new BigInteger(Integer.toString(i)));
            MultiplicativeDigitalRootByteArray.increment(number);
            assertEquals(Integer.toString(i+1), MathUtils.toString(number));
        }*/

        // when we start from 1 we should never encounter "0" or "1" again
        // as both as skipped when incrementing.
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < MAX_DIGITS;i++) {
            number[i] = -1;
        }

        BigInteger current = new BigInteger("1");
        for(int i = 0;i < 1000;i++) {
            MathUtils.toByteArray(number, current);

            MultiplicativeDigitalRootByteArray.increment(number);

            String numberStr = MathUtils.toString(number);
            assertFalse("Failed for " + i + " and " + numberStr,
                    numberStr.contains("0"));
            assertFalse("Failed for " + i + " and " + numberStr,
                    numberStr.contains("1"));

            current = new BigInteger(numberStr);
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

    @Test
    public void testIncrementMultiple() {
        // verify increments with skips
        checkIncrementMultiple("9", "22", "23", "24", "25", "26", "27", "28", "29", "32", "33");
        checkIncrementMultiple("100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "112", "113");
        checkIncrementMultiple("99", "222", "223", "224", "225", "226", "227", "228", "229", "232");
        checkIncrementMultiple("662", "666", "667", "668", "669", "672");
        checkIncrementMultiple("693", "699", "722", "723", "724", "725");
        checkIncrementMultiple("9777777777772", "9777777777777");
        checkIncrementMultiple("77825662428927788", "77825662428927789", "77825662428927792", "77825662428927799");
        checkIncrementMultiple("77825662428927759", "77825662428927772");
    }

    private void checkIncrementMultiple(String initial, String ... expectedNumbers) {
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < MAX_DIGITS;i++) {
            number[i] = -1;
        }

        BigInteger current = new BigInteger(initial);
        for (String expected : expectedNumbers) {
            MathUtils.toByteArray(number, current);

            MultiplicativeDigitalRootByteArray.increment(number);

            assertEquals(expected, MathUtils.toString(number));

            current = new BigInteger(expected);
        }
    }

    @Ignore("Local micro-benchmark")
    @Test
    public void testMicroBenchmarkIncrement() {
        /*
            Took: 5615ms
            Took: 2820ms
            Took: 2794ms
            Took: 2682ms
            Took: 2270ms
            Took: 2268ms
         */
        runBenchmarkIncrement();
        runBenchmarkIncrement();
        runBenchmarkIncrement();
        runBenchmarkIncrement();
        runBenchmarkIncrement();
        runBenchmarkIncrement();
    }

    private void runBenchmarkIncrement() {
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < MAX_DIGITS;i++) {
            number[i] = -1;
        }

        long start = System.currentTimeMillis();

        for(long i = 0;i < 1_000_000_000L;i++) {
            MultiplicativeDigitalRootByteArray.increment(number);
        }

        System.out.println("Took: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Ignore("Local micro-benchmark")
    @Test
    public void testMicroBenchmarkCandidate() {
        /*
            Took: 2353ms
            Took: 2165ms
            Took: 2168ms
            Took: 2152ms
            Took: 2149ms
            Took: 2177ms
         */
        runBenchmarkCandidate();
        runBenchmarkCandidate();
        runBenchmarkCandidate();
        runBenchmarkCandidate();
        runBenchmarkCandidate();
        runBenchmarkCandidate();
    }

    private void runBenchmarkCandidate() {
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < MAX_DIGITS;i++) {
            number[i] = -1;
        }

        long start = System.currentTimeMillis();

        for(long i = 0;i < 300_000_000L;i++) {
            MultiplicativeDigitalRootByteArray.increment(number);
            //noinspection ResultOfMethodCallIgnored
            MultiplicativeDigitalRootByteArray.candidate(number);
        }

        System.out.println("Took: " + (System.currentTimeMillis() - start) + "ms");
    }
}

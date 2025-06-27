package org.dstadler.multiplication;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigInteger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ByteArrayIntegerTest {
	@Test
	public void testConstruct() {
		ByteArrayInteger number = new ByteArrayInteger();
		assertEquals("", number.toString());
		assertEquals(0, number.getDigits());

		number.increment();

		assertEquals("2", number.toString());
		assertEquals(1, number.getDigits());
	}

	@Test
	public void testConstructBigInteger() {
		ByteArrayInteger number = new ByteArrayInteger(BigInteger.valueOf(1));
		assertEquals("1", number.toString());
		assertEquals(1, number.getDigits());

		number = new ByteArrayInteger(BigInteger.valueOf(23));
		assertEquals("23", number.toString());
		assertEquals(2, number.getDigits());

		number.increment();

		assertEquals("24", number.toString());
		assertEquals(2, number.getDigits());
	}

	@Test
	public void testConstructArray() {
		ByteArrayInteger number = new ByteArrayInteger(new byte[] { 3 });
		assertEquals("3", number.toString());
		assertEquals(1, number.getDigits());

		number.increment();

		assertEquals("4", number.toString());
		assertEquals(1, number.getDigits());
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
            number.increment();
            assertEquals(Integer.toString(i+1), number.toString());
        }*/

        // when we start from 1 we should never encounter "0" or "1" again
        // as both as skipped when incrementing.
		ByteArrayInteger number = new ByteArrayInteger();

        BigInteger current = new BigInteger("1");
        for(int i = 0;i < 1000;i++) {
            MathUtils.toByteArray(number.getArray(), current);

			number.increment();

            String numberStr = number.toString();
            assertFalse(numberStr.contains("0"),
                    "Failed for " + i + " and " + numberStr);
            assertFalse(numberStr.contains("1"),
                    "Failed for " + i + " and " + numberStr);

            current = new BigInteger(numberStr);
        }
    }

    private void checkIncrement(byte[] expected, byte[] number) {
		ByteArrayInteger integer = new ByteArrayInteger(number);

		integer.increment();
		number = integer.getArray();

        for(int i = 0;i < MAX_DIGITS;i++) {
            if(expected[i] == -1 && number[i] == -1) {
                break;
            }

            assertEquals(expected[i], number[i], "Failed at " + i + ", having " + integer);
        }
    }

    @Test
    public void testIncrementMultiple() {
        // verify increments with skips
        checkIncrementMultiple("9", "22", "23", "24", "25", "26", "27", "28", "29", "32", "33");
        checkIncrementMultiple("100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "117", "118");
        checkIncrementMultiple("99", "222", "223", "224", "225", "226", "227", "228", "229", "237");
        checkIncrementMultiple("662", "666", "667", "668", "669", "677");
        checkIncrementMultiple("693", "699", "727", "728", "729", "777");
        checkIncrementMultiple("9777777777772", "9777777777777");
        checkIncrementMultiple("77825662428927788", "77825662428927789", "77825662428927797", "77825662428927799");
        checkIncrementMultiple("77825662428927759", "77825662428927777");
        checkIncrementMultiple("229", "237", "238");
        checkIncrementMultiple("277777788888777", "277777788888778", "277777788888779", "277777788888787",
				"277777788888788", "277777788888789", "277777788888797", "277777788888799", "277777788888877", "277777788888878",
				"277777788888879", "277777788888887", "277777788888888", "277777788888889", "277777788888897", "277777788888899");
    }

    private void checkIncrementMultiple(String initial, String ... expectedNumbers) {

        BigInteger current = new BigInteger(initial);
        for (String expected : expectedNumbers) {
			ByteArrayInteger number = new ByteArrayInteger(current);

            number.increment();

            assertEquals(expected, number.toString());

            current = new BigInteger(expected);
        }
    }

	@Test
	public void testOnly789() {
		ByteArrayInteger number = new ByteArrayInteger();

		while (true) {
			String str = number.toString();
			if (str.length() >= 3 && str.compareTo("229") >= 0) {
				break;
			}

			number.increment();
		}

		assertEquals(
				"229",
				number.toString());

		number.increment();

		assertEquals(
				"237",
				number.toString());
	}

    @Disabled("Local micro-benchmark")
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
		ByteArrayInteger number = new ByteArrayInteger();

        long start = System.currentTimeMillis();

        for(long i = 0;i < 1_000_000_000L;i++) {
            number.increment();
        }

        System.out.println("Took: " + (System.currentTimeMillis() - start) + "ms");
    }
}

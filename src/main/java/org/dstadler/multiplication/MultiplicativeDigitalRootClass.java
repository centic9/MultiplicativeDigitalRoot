package org.dstadler.multiplication;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;

import java.math.BigInteger;

/**
 * A sample application which represents the digits of the number in a byte-array which
 * allows to perform some of the operations much quicker.
 *
 * It also optimizes incrementing the number a lot to check
 * much less numbers and skip large sections of numbers that are not relevant anyway.
 *
 * The digits in the byte-array are stored with lowest ones first, i.e 25 is stored as { 5, 2 }.
 */
public class MultiplicativeDigitalRootClass {
    private static int maxPersistence = 1;
    private static int count = 0;
    private static int countCheck = 0;
    private static long countCandidate;
    private static final long start = System.currentTimeMillis();

    public static void main(String[] args) {
		ByteArrayInteger number = new ByteArrayInteger();
        while(true) {
            if (runIteration(number)) {
                break;
            }
        }

        System.out.println("Had max persistence of " + maxPersistence);
    }

    private static boolean runIteration(ByteArrayInteger number) {
        number.increment();

        countCandidate++;

        if (candidate(number.getArray())) {
            //System.out.println("Calculating the persistence of " + input);
            int persistence = MathUtils.getPersistence(number.getArray());
            countCheck++;

            if (persistence > maxPersistence) {
                System.out.printf("Found persistence %2d for %s after %,dms%n",
						persistence, number, System.currentTimeMillis() - start);
                maxPersistence = persistence;
            }
        }

        if (count % 278999992 == 0) {
            long now = System.currentTimeMillis();
            long duration = (now - start)/1000;
            BigInteger bigNumber = number.getAsBigInteger();
            BigInteger nPerSec = duration == 0 ? BigInteger.ZERO : bigNumber.divide(BigInteger.valueOf(duration));
			System.out.printf("%,10ds: Testing(%,3d): %,30d, max: %2d, n/sec: %,28d (%,8d), "
							+ "candidates: %,20d (%,5d), checked: %,10d (%,5d)%n",
                    duration, number.getLength(), bigNumber, maxPersistence,
                    nPerSec, BigInteger.valueOf(100_000).multiply(nPerSec).divide(bigNumber),
					countCandidate, BigInteger.valueOf(10_000_000).multiply(BigInteger.valueOf(countCandidate)).divide(bigNumber),
                    countCheck, BigInteger.valueOf(100_000_000_000L).multiply(BigInteger.valueOf(countCheck)).divide(bigNumber)
            );

			/*if (start + TimeUnit.MINUTES.toMillis(1) < System.currentTimeMillis()) {
				System.out.println("Stopping after 1 Minute");
				System.exit(0);
			}*/
        }

        count++;

        return maxPersistence > 11;
    }

    /**
     * Check if this number is a useful candidate for checking
     * multiplicative persistence to ensure that we do not check
     * numbers that will have the same persistence as smaller numbers anyway.
     *
     * @param number The current number as byte-array with digits in reverse order
     * @return true if this number can be checked for persistence or false if
     *      it should be skipped.
     */
    protected static boolean candidate(byte[] number) {
        // not a candidate if digits are not ordered
        byte prev = 9;
        boolean two = false, three = false, five = false;
        for (int i = 0;i < MAX_DIGITS;i++) {
            byte b = number[i];
            if (b == -1) {
                return true;
            }

            if (b > prev) {
                return false;
            }

            if (b == 2) {
                // "2" and "2" could be replaced by "4" and would give a smaller number
                // "2" and "3" can be replaced by "6" and this would give a smaller number
                if (two || three) {
                    return false;
                }

                // two and five together cause a "10" and thus are not more than 2 steps to get to root "0"
				// irrespective of any other digits in the number
				// this actually causes checking "25" to fail, but we ignore this one special case for now
                if (five) {
                    return false;
                }

				two = true;
            } else if (b == 3) {
                // "3" and "3" can be replaced by "9" and this would give a smaller number
                if (three) {
                    return false;
                }
                three = true;
            } else if (b == 5) {
                five = true;
            }

            prev = b;
        }

        throw new IllegalStateException("Exceeded max number of digits: " + MAX_DIGITS);
    }
}

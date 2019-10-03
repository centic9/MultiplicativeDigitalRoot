package org.dstadler.multiplication;

import java.math.BigInteger;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;

public class MultiplicativeDigitalRootByteArray {
    private static int maxPersistence = 1;
    private static int count = 0;
    private static int countCheck = 0;
    private static long countCandidate;
    private static long start = System.currentTimeMillis();

    public static void main(String[] args) {
        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < MAX_DIGITS;i++) {
            number[i] = -1;
        }

        while(true) {
            if (runIteration(number)) {
                break;
            }
        }

        System.out.println("Had max persistence of " + maxPersistence);
    }

    private static boolean runIteration(byte[] number) {
        increment(number);

        countCandidate++;

        final int persistence;
        if(!candidate(number)) {
            persistence = -1;
        } else {
            //System.out.println("Calculating the persistence of " + input);
            persistence = MathUtils.getPersistence(number);
            countCheck++;

            if(persistence > maxPersistence) {
                System.out.println("Found persistence " + persistence + " for " + MathUtils.toString(number));
            }
        }

        if(count % 278999992 == 0) {
            long now = System.currentTimeMillis();
            long duration = (now - start)/1000;
            BigInteger bigNumber = new BigInteger(MathUtils.toString(number));
            System.out.println(String.format("After %,10ds: Had persistence: %2d for %,19d, max: %2d, n/sec: %,19d, checked: %,10d(%,3d), candidates: %,19d(%,5d)",
                    duration, persistence, bigNumber, maxPersistence,
                    (duration == 0 ? BigInteger.ZERO : bigNumber.divide(BigInteger.valueOf(duration))),
                    countCheck, BigInteger.valueOf(1000000).multiply(BigInteger.valueOf(countCheck)).divide(bigNumber),
                    countCandidate, BigInteger.valueOf(1000000).multiply(BigInteger.valueOf(countCandidate)).divide(bigNumber)
            ));
        }

        maxPersistence = Math.max(persistence, maxPersistence);
        count++;

        return persistence > 11;
    }

    /**
     * Increment to a following number which should be checked further.
     *
     * Note: This will not increment in single steps, but often
     * skip large parts of numbers which are not interesting anyway, e.g.
     * all numbers containing "0" or "1".
     *
     * The byte-array should be initialized fully with "-1" values.
     *
     * This method expects to usually only receive an empty byte-array (all -1s)
     * or numbers which were the result of previous invocations of this method!
     *
     * @param number The current number as byte-array with digits in reverse order
     */
    protected static void increment(byte[] number) {
        for(int i = 0;i < MAX_DIGITS;i++) {
            byte nr = number[i];

            // reached the end and thus should set the current digit to 1 now
            if(nr == -1) {
                // skip 1 as this is not a candidate anyway
                number[i] = 2;
                break;
            }

            // when a digit is 9, set it to 2 and continue incrementing the next digits
            if(nr == 9) {
                // skip 0 and 1 as both are not seen as candidates anyway
                number[i] = 2;
            } else {
                // if the following digit is higher, we can immediately increment to it
                // as otherwise we do not have digits in ascending order any more
                if(number[i + 1] > (nr + 1)) {
                    number[i] = number[i + 1];
                } else {
                    // otherwise simply increment
                    number[i] = (byte) (nr + 1);
                }

                break;
            }
        }
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
            if(b == -1) {
                return true;
            }
            if(b > prev) {
                return false;
            }

            if(b == 0) {
                // zero would lead to persistence 1
                return false;
            } else if (b == 1) {
                // "1" could be removed and thus would give a smaller number
                return false;
            } else if(b == 2) {
                // "2" and "2" could be replaced by "4" and would give a smaller number
                // "2" and "3" can be replaced by "6" and this would give a smaller number
                if (two || three) {
                    return false;
                }
                // two and five together cause a "10" and thus are not more than 2 steps to get to root "0"
                if (five) {
                    return false;
                }
                two = true;
            } else if(b == 3) {
                // "3" and "3" can be replaced by "9" and this would give a smaller number
                if(three) {
                    return false;
                }
                three = true;
            } else if(b == 5) {
                five = true;
            }

            prev = b;
        }

        return true;
    }
}

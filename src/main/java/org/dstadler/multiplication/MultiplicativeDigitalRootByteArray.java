package org.dstadler.multiplication;

import java.math.BigInteger;

import static org.dstadler.multiplication.MathUtils.MAX_DIGITS;

public class MultiplicativeDigitalRootByteArray {
    public static void main(String[] args) {
        int maxPersistence = 1;
        int count = 0;
        int countCheck = 0;

        byte[] number = new byte[MAX_DIGITS];
        for(int i = 0;i < MAX_DIGITS;i++) {
            number[i] = -1;
        }

        long start = System.currentTimeMillis();

        while(true) {
            increment(number);

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

            if(persistence >= 10 || count % 278999992 == 0) {
                long now = System.currentTimeMillis();
                long duration = (now - start)/1000;
                BigInteger bigNumber = new BigInteger(MathUtils.toString(number));
                System.out.println(String.format("Had persistence: %2d for %,15d, max: %2d, n/sec: %,15d, checked: %,15d",
                        persistence, bigNumber, maxPersistence,
                        (duration == 0 ? BigInteger.ZERO : bigNumber.divide(BigInteger.valueOf(duration))), countCheck));
            }

            maxPersistence = Math.max(persistence, maxPersistence);
            count++;

            if(persistence > 11) {
                break;
            }
        }
        System.out.println("Had max persistence of " + maxPersistence);
    }

    protected static void increment(byte[] number) {
        for(int i = 0;i < MAX_DIGITS;i++) {
            byte nr = number[i];

            // reached the end and thus should set the current digit to 1 now
            if(nr == -1) {
                number[i] = 1;
                number[i+1] = -1;
                break;
            }

            // when a digit is 9, set it to 0 and continue incrementing the next digits
            if(nr == 9) {
                number[i] = 0;
            } else {
                // otherwise simply increment and be done
                number[i] = (byte)(nr + 1);
                break;
            }
        }
    }

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

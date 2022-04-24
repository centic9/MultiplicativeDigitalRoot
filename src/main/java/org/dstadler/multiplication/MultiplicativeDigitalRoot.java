package org.dstadler.multiplication;

import java.math.BigInteger;

/**
 * A sample application which uses Strings and BigIntegers to handle large numbers, but this is
 * obviously rather inefficient and limits the number of checks that can be performed.
 */
public class MultiplicativeDigitalRoot {
    public static void main(String[] args) {

        int maxPersistence = 1;
        int count = 0;
        int countCheck = 0;

        BigInteger number = new BigInteger("0");
        long start = System.currentTimeMillis();

        while(true) {
            /*System.out.println("Please enter a number: ");
            String input = System.console().readLine();
            if (StringUtils.isEmpty(input)) {
                break;
            }*/

            //String input = RandomStringUtils.random(RandomUtils.nextInt(16, 100), "12345789");
            number = number.add(BigInteger.ONE);

            final int persistence;
            String input = number.toString();
            if(!candidate(input)) {
                persistence = -1;
            } else {
                //System.out.println("Calculating the persistence of " + input);
                persistence = MathUtils.getPersistence(input);
                countCheck++;

                if(persistence > maxPersistence) {
                    System.out.println("Found persistence " + persistence + " for " + input);
                }
            }

            if(persistence >= 10 || count % 2788999 == 0) {
                long now = System.currentTimeMillis();
                long duration = (now - start)/1000;
                System.out.printf("Had persistence: %2d for %,15d, max: %2d, n/sec: %,15d, checked: %,15d%n",
                        persistence, number, maxPersistence,
                        (duration == 0 ? BigInteger.ZERO : number.divide(BigInteger.valueOf(duration))), countCheck);
            }

            maxPersistence = Math.max(persistence, maxPersistence);
            count++;

            if(persistence > 11) {
                break;
            }
        }
        System.out.println("Had max persistence of " + maxPersistence);
    }

    protected static boolean candidate(String input) {
        // not a candidate if digits are not ordered
        char prev = '0';
        boolean two = false, three = false;
        for (char c :input.toCharArray()){
            if(c < prev) {
                return false;
            }

            if(c == '0') {
                // zero would lead to persistence 1
                return false;
            } else if (c == '1') {
                // "1" could be removed and thus would give a smaller number
                return false;
            } else if(c == '2') {
                // "2" and "2" could be replaced by "4" and would give a smaller number
                if (two) {
                    return false;
                }
                two = true;
            } else if(c == '3') {
                // "2" and "3" can be replaced by "6" and this would give a smaller number
                // "3" and "3" can be replaced by "9" and this would give a smaller number
                if(two || three) {
                    return false;
                }
                three = true;
            } else if(c == '5') {
                // two and five together cause a "10" and thus are not more than 2 steps to get to root "0"
                if(two) {
                    return false;
                }
            }

            prev = c;
        }

        return true;
    }
}

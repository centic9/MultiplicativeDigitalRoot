package org.dstadler.multiplication;

import java.math.BigInteger;

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
            }

            if(persistence >= 10 || count % 2788999 == 0) {
                long now = System.currentTimeMillis();
                long duration = (now - start)/1000;
                System.out.println(String.format("Had persistence: %2d for %,15d, max: %2d, n/sec: %,15d, checked: %,15d",
                        persistence, number, maxPersistence,
                        (duration == 0 ? BigInteger.ZERO : number.divide(BigInteger.valueOf(duration))), countCheck));
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
        boolean two = false, five = false;
        for (char c :input.toCharArray()){
            if(c == '0') {
                return false;
            }

            if(c == '2') {
                two = true;
            } else if(c == '5') {
                five = true;
            }

            // two and five together cause a "10" and thus are done in 2 steps at max
            if((two && five) || c < prev) {
                return false;
            }

            prev = c;
        }

        return true;
    }
}

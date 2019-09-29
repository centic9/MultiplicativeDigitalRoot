package org.dstadler.multiplication;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

public class MultiplicativeDigitalRoot {
    public static void main(String[] args) {

        int maxPersistence = 1;
        int count = 0;

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
            String input = number.toString();

            //System.out.println("Calculating the persistence of " + input);
            int persistence = MathUtils.getPersistence(input);

            if(persistence >= 10 || count % 100000 == 0) {
                long now = System.currentTimeMillis();
                long duration = (now - start)/1000;
                System.out.println(String.format("%15d: Had persistence: %2d for %s, max: %2d, n/sec: %,15d",
                        count, persistence, StringUtils.leftPad(input, 15, " "), maxPersistence,
                        (duration == 0 ? BigInteger.ZERO : number.divide(BigInteger.valueOf(duration)))));
            }

            maxPersistence = Math.max(persistence, maxPersistence);
            count++;

            if(persistence > 11) {
                break;
            }
        }
        System.out.println("Had max persistence of " + maxPersistence);
    }
}

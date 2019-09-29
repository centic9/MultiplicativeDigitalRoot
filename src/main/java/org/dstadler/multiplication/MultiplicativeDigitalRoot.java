package org.dstadler.multiplication;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
            int persistence = getPersistence(input);

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

    protected static int getPersistence(String input) {
        int persistence = 1;
        while(true) {
            BigInteger product = getMultiplicativeDigitalRoot(input);

            //System.out.println(persistence + ": Result of " + input + ": " + product);

            if(product.toString().length() <= 1) {
                return persistence;
            }

            persistence++;
            input = product.toString();
        }
    }

    protected static BigInteger getMultiplicativeDigitalRoot(String input) {
        BigInteger product;
        product = new BigInteger("1");
        for (int i = 0;i < input.length();i++) {
            product = product.multiply(new BigInteger("" + input.charAt(i)));
        }
        return product;
    }

    public static List<BigInteger> primeFactors(String input) {
        /*        int n = numbers;
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors;*/

        BigInteger n = new BigInteger(input);
        List<BigInteger> factors = new ArrayList<>();
        BigInteger i = BigInteger.TWO;
        while(true) {
            BigInteger division = n.divide(i);
            if(i.compareTo(division) > 0) {
                break;
            }

            while(true) {
                BigInteger remainder = n.remainder(i);
                if(remainder.equals(BigInteger.ZERO)) {
                    factors.add(i);
                    n = n.divide(i);
                } else {
                    break;
                }
            }

            i = i.add(BigInteger.ONE);
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            factors.add(n);
        }

        return factors;
    }
}

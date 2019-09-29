package org.dstadler.multiplication;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;

public class MultiplicativeDigitalRoot {
    public static void main(String[] args) {

        int maxPersistence = 1;

        while(true) {
            /*System.out.println("Please enter a number: ");
            String input = System.console().readLine();
            if (StringUtils.isEmpty(input)) {
                break;
            }*/

            String input = RandomStringUtils.random(100, "12345789");

            System.out.println("Calculating the persistence of " + input);
            int persistence = getPersistence(input);

            System.out.println("Had persistence: " + persistence + " for " + input);

            maxPersistence = Math.max(persistence, maxPersistence);

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
}

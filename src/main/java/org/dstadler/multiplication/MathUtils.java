package org.dstadler.multiplication;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for computing multiplicative persistence and multiplicative digital root
 *
 */
public class MathUtils {

    public static int getPersistence(String input) {
        if(input.contains("0")) {
            return 1;
        }

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

    public static BigInteger getMultiplicativeDigitalRoot(String input) {
        // shortcut this calculation
        if(input.contains("0")) {
            return BigInteger.ZERO;
        }

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

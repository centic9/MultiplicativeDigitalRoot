package org.dstadler.multiplication;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for computing multiplicative persistence and multiplicative digital root
 *
 */
public class MathUtils {
    private static final BigInteger[] DIGITS = new BigInteger[10];
    static {
        for(int i = 0;i < 10;i++) {
            DIGITS[i] = new BigInteger("" + i);
        }
    }


    public static int getPersistence(String input) {
        if(input.contains("0")) {
            return 1;
        }

        int persistence = 1;
        while(true) {
            BigInteger product = getMultiplicativeDigitalRoot(input);

            //System.out.println(persistence + ": Result of " + input + ": " + product);

            if(log10(product) == 0) {
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

        BigInteger product = BigInteger.ONE;
        for (char digit: input.toCharArray()) {
            product = product.multiply(DIGITS[digit - 0x30]);
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

    public static int log10(BigInteger input) {
        int digits = 0;
        int bits = input.bitLength();
        while (bits > 4) {
            // 4 > log[2](10) so we should not reduce it too far.
            int reduce = bits / 4;
            // Divide by 10^reduce
            input = input.divide(BigInteger.TEN.pow(reduce));
            // Removed that many decimal digits.
            digits += reduce;
            // Recalculate bitLength
            bits = input.bitLength();
        }
        // Now 4 bits or less - add 1 if necessary.
        if ( input.intValue() > 9 ) {
            digits += 1;
        }
        return digits;
    }
}

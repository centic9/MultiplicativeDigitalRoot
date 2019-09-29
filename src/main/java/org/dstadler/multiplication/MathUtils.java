package org.dstadler.multiplication;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for computing multiplicative persistence and multiplicative digital root
 *
 * See https://en.wikipedia.org/wiki/Multiplicative_digital_root and
 * https://en.wikipedia.org/wiki/Persistence_of_a_number
 */
public class MathUtils {
    // Pre-compute digits from 0-9 to not having to construct them always
    private static final BigInteger[] DIGITS = new BigInteger[10];
    static {
        for(int i = 0;i < 10;i++) {
            DIGITS[i] = new BigInteger("" + i);
        }
    }

    /**
     * Compute the multiplicative persistence of a number, see
     * also https://en.wikipedia.org/wiki/Persistence_of_a_number and
     *
     * E.g. 10 => 1, 25 => 2, 39 => 3, ...
     *
     * See also https://oeis.org/A007954 and https://oeis.org/A003001 for related
     * mathematical series.
     *
     * @param input The number as string
     * @return How many times the multiplicative digital root can be computed on this number.
     */
    public static int getPersistence(String input) {
        if(input.contains("0")) {
            return 1;
        }

        int persistence = 1;
        while(true) {
            BigInteger product = getMultiplicativeDigitalRoot(input);

            //System.out.println(persistence + ": Result of " + input + ": " + product);

            // stop when we have reached a single-digit number
            if(product.compareTo(BigInteger.TEN) < 0) {
                return persistence;
            }

            persistence++;
            input = product.toString();
        }
    }

    /**
     * Compute the multiplicative digital root of the given number.
     *
     * See also https://en.wikipedia.org/wiki/Multiplicative_digital_root
     *
     * E.g. 10 => 0, 25 => 10, 39 => 27, 27 => 14, 14 => 4, ...
     *
     * See also https://oeis.org/A007954 and https://oeis.org/A003001 for related
     * mathematical series.
     *
     * @param input The number as string
     * @return The multiplicative root of the given number.
     */
    public static BigInteger getMultiplicativeDigitalRoot(String input) {
        // shortcut this calculation
        if(input.contains("0")) {
            return BigInteger.ZERO;
        }

        BigInteger product = BigInteger.ONE;
        for (char digit: input.toCharArray()) {
            // multiplying by "1" does not change the result
            if(digit != '1') {
                product = product.multiply(DIGITS[digit - 0x30]);
            }
        }

        return product;
    }

    /**
     * Simple algorithm for computing prime factors for BigIntegers.
     * Mostly used in testing/evaluation of known smallest numbers for
     * multiplicative persistence.
     *
     * @param input The number to factor
     * @return A sorted list of all factors of the given number.
     */
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

        // start with prime factor "2"
        BigInteger i = BigInteger.TWO;
        //noinspection ConditionalBreakInInfiniteLoop
        while(true) {
            // if i is too large for an additional factor we are done here
            if(i.compareTo(n.divide(i)) > 0) {
                break;
            }

            while(true) {
                if(n.remainder(i).equals(BigInteger.ZERO)) {
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

    /**
     * Compute the base-10 logarithm of the given BigInteger.
     * <br/><br/>
     * log10(0) => 0<br/>
     * log10(1) => 0<br/>
     * ...<br/>
     * log10(9) => 0<br/>
     * log10(10) => 1<br/>
     * ...<br/>
     * log10(100) => 2<br/>
     * ...<br/>
     *
     * @param input the number used for computation
     * @return the logarithm of the given number in base 10
     */
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
